package andrewbeav.github.io.weathermeme;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailedWeatherFragment extends Fragment {

    public DetailedWeatherFragment() {
        // Required empty public constructor
    }

    private TextView tempView, humidityView, pressureView, windView, windDirectionView;

    private double latitude, longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        this.latitude = bundle.getDouble("Latitude");
        this.longitude = bundle.getDouble("Longitude");

        View view = inflater.inflate(R.layout.fragment_detailed_weather, container, false);

        tempView = (TextView) view.findViewById(R.id.detailed_temp_value);
        humidityView = (TextView) view.findViewById(R.id.detailed_humidity_value);
        pressureView = (TextView) view.findViewById(R.id.detailed_pressure_value);
        windView = (TextView) view.findViewById(R.id.detailed_wind_value);
        windDirectionView = (TextView) view.findViewById(R.id.detailed_wind_direction_value);

        WeatherInfoGetter weatherInfoGetter = new WeatherInfoGetter();
        weatherInfoGetter.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=bcf98db996d2d93497a184c6af4c3c7a&units=imperial");

        // Inflate the layout for this fragment
        return view;
    }

    public void populateWithWeatherInfo(WeatherInfo weatherInfo) {
        tempView.setText(String.valueOf(weatherInfo.getTemperature()) + (char) 0x00B0 + "F");
        humidityView.setText(String.valueOf(weatherInfo.getHumidity()) + "%");
        pressureView.setText(String.valueOf(weatherInfo.getPressure()) + " hPa");
        windView.setText(String.valueOf(weatherInfo.getWindSpeed()) + " mph");

        String windDirection = "";

        switch(weatherInfo.getWindDirection()) {
            case "N":
                windDirection = "North";
                break;
            case "S":
                windDirection = "South";
                break;
            case "E":
                windDirection = "East";
                break;
            case "W":
                windDirection = "West";
        }
        windDirectionView.setText(windDirection);
    }

    public void showToast(String message, int length) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), message, length);
        toast.show();
    }

    private class WeatherInfoGetter extends AsyncTask<String, Void, String> {
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
                showToast("Something's Not Right. You Either Have No Internet Connection or an Invalid Custom Location", Toast.LENGTH_LONG);
            }

            if (jsonObject != null) {
                populateWithWeatherInfo(new WeatherInfo(jsonObject));
            }
        }
    }

}
