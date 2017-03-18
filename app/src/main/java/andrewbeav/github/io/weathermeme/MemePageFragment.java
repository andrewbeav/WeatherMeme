package andrewbeav.github.io.weathermeme;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import static android.R.attr.delay;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemePageFragment extends Fragment implements WeatherUI {

    public MemePageFragment() {
        // Required empty public constructor
    }

    public void showToast(String message, int length) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), message, length);
        toast.show();
    }

    public void setMeme(String imageName) {
        Resources res = getResources();
        int resourceId = res.getIdentifier(imageName, "drawable", getActivity().getPackageName());

        memeView.setImageDrawable(res.getDrawable(resourceId));
    }

    public void updateCityName(String cityName) {
            getActivity().setTitle(cityName);
    }

    public void updateTemp(double temp) {
        this.tempText.setText(temp + ((char) 0x00B0 + "F"));
    }

    public void updateHumidity(double humidity) {
        this.humidityText.setText("Humidity: " + humidity + "%");
    }

    public void noDescription() {
        this.weatherDescriptionText.setText(null);
    }

    public void updateDescription(String weatherDescription) {
        this.weatherDescriptionText.setText(weatherDescription);
    }

    public void updateWind(String windText) {
        this.windText.setText(windText);
    }

    public void noWind() {
        this.windText.setText("No Wind");
    }

    private TextView tempText;
    private TextView humidityText;
    private TextView windText;
    private TextView weatherDescriptionText;
    private ImageView memeView;

    private Button refreshButton;
    private Button editLocationButton;

    private double latitude, longitude;

    private WeatherInfo weatherInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Andrew", "onCreateView");

        Bundle bundle = getArguments();
        this.latitude = bundle.getDouble("Latitude");
        this.longitude = bundle.getDouble("Longitude");

        View view = inflater.inflate(R.layout.fragment_meme_page, container, false);

        tempText = (TextView) view.findViewById(R.id.temp_text);
        humidityText = (TextView) view.findViewById(R.id.humidity_text);
        windText = (TextView) view.findViewById(R.id.wind_text);
        weatherDescriptionText = (TextView) view.findViewById(R.id.weather_description_text);
        memeView = (ImageView) view.findViewById(R.id.memeView);

        refreshButton = (Button) view.findViewById(R.id.refresh_button);
        editLocationButton = (Button) view.findViewById(R.id.edit_location_button);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshWeather(v);
            }
        });

        editLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLocation(v);
            }
        });

        JSONDownloader jsonDownloader = new JSONDownloader(this);
        jsonDownloader.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=bcf98db996d2d93497a184c6af4c3c7a&units=imperial");

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void populateWithWeatherInfo(WeatherInfo weatherInfo) {
            updateCityName(weatherInfo.getCityName());
            updateTemp(weatherInfo.getTemperature());
            updateHumidity(weatherInfo.getHumidity());

            if (weatherInfo.getMain() != null) {
                updateDescription(weatherInfo.getMainDescription());
            } else {
                noDescription();
            }

            if (weatherInfo.getWindSpeed() != -1 && weatherInfo.getWindDirection() != null) {
                updateWind("Wind: " + String.valueOf(weatherInfo.getWindSpeed()) + "mph, " + weatherInfo.getWindDirection());
            } else {
                noWind();
            }

            WeatherMemeGenerator memeGenerator = new WeatherMemeGenerator(weatherInfo);
            setMeme(memeGenerator.getImageTitle());
    }


    public static final int USE_CURRENT_LOCATION = 1;
    public static final int USE_CUSTOM_LOCATION = 2;

    private int locationType = USE_CURRENT_LOCATION;
    private String customLocation;

    public void refreshWeather(View view) {
        JSONDownloader jsonDownloader = new JSONDownloader(this);

        if (locationType == USE_CURRENT_LOCATION) {
            jsonDownloader.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=bcf98db996d2d93497a184c6af4c3c7a&units=imperial");
        }
        else if (locationType == USE_CUSTOM_LOCATION && customLocation != null) {
            jsonDownloader.execute("http://api.openweathermap.org/data/2.5/weather?q=" + customLocation + "&units=imperial&appid=bcf98db996d2d93497a184c6af4c3c7a");
        }
    }

    public void editLocation(View view) {
        startActivityForResult(new Intent(getActivity(), EditLocationPopup.class), EditLocationPopup.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EditLocationPopup.REQUEST_CODE && resultCode == RESULT_OK) {
             JSONDownloader jsonDownloader = new JSONDownloader(this);
            if (!data.getStringExtra("Location").equals("CURRENT_LOCATION")) {
                locationType = USE_CUSTOM_LOCATION;
                customLocation = data.getStringExtra("Location");
                jsonDownloader.execute("http://api.openweathermap.org/data/2.5/weather?q=" + customLocation + "&units=imperial&appid=bcf98db996d2d93497a184c6af4c3c7a");
            }
            else {
                locationType = USE_CURRENT_LOCATION;
                jsonDownloader.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=bcf98db996d2d93497a184c6af4c3c7a&units=imperial");
            }
        }
    }
}
