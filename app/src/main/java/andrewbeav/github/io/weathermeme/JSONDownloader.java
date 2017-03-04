package andrewbeav.github.io.weathermeme;

import android.content.res.Resources;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

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

        try {
            JSONObject jsonObject = new JSONObject(result);
            this.weatherInfo = new WeatherInfo(jsonObject);

            mainActivity.cityNameText.setText(weatherInfo.getCityName());
            mainActivity.tempText.setText(String.valueOf(weatherInfo.getTemperature()) + (char) 0x00B0 + "F");
            mainActivity.humidityText.setText("Humidity: " + String.valueOf(weatherInfo.getHumidity()) + "%");
            mainActivity.lowTempText.setText("Low Temp: " + String.valueOf(weatherInfo.getLowTemperature()) + (char) 0x00B0 + "F");
            mainActivity.highTempText.setText("High Temp: " + String.valueOf(weatherInfo.getHighTemperature()) + (char) 0x00B0 + "F");

            if (weatherInfo.getRainVolume() != -1) {
                mainActivity.rainText.setText("Rain Volume: " + String.valueOf(weatherInfo.getRainVolume()));
            } else {
                mainActivity.rainText.setText("No Rain");
            }

            if (weatherInfo.getWindSpeed() != -1 && weatherInfo.getWindDirection() != null) {
                mainActivity.windText.setText("Wind: " + String.valueOf(weatherInfo.getWindSpeed()) + "mph, " + weatherInfo.getWindDirection());
            } else {
                mainActivity.windText.setText("No Wind");
            }

            WeatherMemeGenerator memeGenerator = new WeatherMemeGenerator(weatherInfo);
            mainActivity.setMeme(memeGenerator.getImageTitle());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
