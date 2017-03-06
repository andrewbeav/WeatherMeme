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

import static android.content.Context.*;
import static java.security.AccessController.getContext;

/**
 * Created by andrewbeav on 3/3/17.
 */

public class JSONDownloader extends AsyncTask<String, Void, String> {

    private final MainActivity mainActivity;

    public JSONDownloader(MainActivity activity) {
        this.mainActivity = activity;
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
            mainActivity.showToast("No Connection. Can't Get Weather Info", Toast.LENGTH_LONG);
        }

        if (jsonObject != null) {
            this.weatherInfo = new WeatherInfo(jsonObject);

            mainActivity.setCityNameText(weatherInfo.getCityName());
            mainActivity.setTempText(String.valueOf(weatherInfo.getTemperature()));
            mainActivity.setHumidityText("Humidity: " + String.valueOf(weatherInfo.getHumidity()) + "%");

            if (weatherInfo.getMain().equals("Rain")) {
                mainActivity.setRainText(weatherInfo.getMainDescription());
            } else {
                mainActivity.setRainText("No Rain");
            }

            if (weatherInfo.getWindSpeed() != -1 && weatherInfo.getWindDirection() != null) {
                mainActivity.setWindText("Wind: " + String.valueOf(weatherInfo.getWindSpeed()) + "mph, " + weatherInfo.getWindDirection());
            } else {
                mainActivity.setWindText("No Wind");
            }

            WeatherMemeGenerator memeGenerator = new WeatherMemeGenerator(weatherInfo);
            mainActivity.setMeme(memeGenerator.getImageTitle());

        }
    }
}
