package andrewbeav.github.io.weathermeme;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by andrewbeav on 3/18/17.
 */

public class DetailedWeatherJSONDownloader extends AsyncTask<String, Void, WeatherInfo> {

    WeakReference<DetailedWeatherFragment> weakDetailedWeatherFragment;

    public DetailedWeatherJSONDownloader(DetailedWeatherFragment detailedWeatherFragment) {
        this.weakDetailedWeatherFragment = new WeakReference<DetailedWeatherFragment>(detailedWeatherFragment);
    }

    @Override
    protected WeatherInfo doInBackground(String... params) {
        String result = "";
        URL url;
        HttpURLConnection httpURLConnection;

        try {
            url = new URL(params[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while (data != -1) {
                char resultChar = (char) data;
                result += resultChar;
                data = reader.read();
            }

            JSONObject jsonObject = null;
            if (result != null) {
                try {
                    jsonObject = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return new WeatherInfo(jsonObject);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(WeatherInfo weatherInfo) {
        super.onPostExecute(weatherInfo);

        if (weakDetailedWeatherFragment == null) return;

        weakDetailedWeatherFragment.get().populateWithWeatherInfo(weatherInfo);
    }
}
