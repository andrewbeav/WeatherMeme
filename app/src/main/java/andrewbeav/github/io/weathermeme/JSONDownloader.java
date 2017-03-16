package andrewbeav.github.io.weathermeme;

import android.content.res.Resources;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.awareness.state.Weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import layout.MemePageFragment;

import static android.content.Context.*;
import static java.security.AccessController.getContext;

/**
 * Created by andrewbeav on 3/3/17.
 */

public class JSONDownloader extends AsyncTask<String, Void, String> {

    private final MemePageFragment memePageFragment;

    public JSONDownloader(MemePageFragment memePageFragment) {
        this.memePageFragment = memePageFragment;
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = "";
        URL url;
        HttpURLConnection urlConnection;

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while (data != -1) {
                char resultChar = (char) data;
                result += resultChar;
                data = reader.read();
            }

            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private WeatherInfo weatherInfo;

    public WeatherInfo getWeatherInfo() {
        return this.weatherInfo;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        JSONObject jsonObject = null;
        if (result != null) {
            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            memePageFragment.showToast("Something's Not Right. You Either Have No Internet Connection or an Invalid Custom Location", Toast.LENGTH_LONG);
        }

        if (jsonObject != null) {
            this.weatherInfo = new WeatherInfo(jsonObject);

            memePageFragment.updateCityName(weatherInfo.getCityName());
            memePageFragment.updateTemp(weatherInfo.getTemperature());
            memePageFragment.updateHumidity(weatherInfo.getHumidity());

            if (weatherInfo.getMain() != null) {
                memePageFragment.updateDescription(weatherInfo.getMainDescription());
            } else {
                memePageFragment.noDescription();
            }

            if (weatherInfo.getWindSpeed() != -1 && weatherInfo.getWindDirection() != null) {
                memePageFragment.updateWind("Wind: " + String.valueOf(weatherInfo.getWindSpeed()) + "mph, " + weatherInfo.getWindDirection());
            } else {
                memePageFragment.noWind();
            }

            WeatherMemeGenerator memeGenerator = new WeatherMemeGenerator(weatherInfo);
            memePageFragment.setMeme(memeGenerator.getImageTitle());

        }
    }
}
