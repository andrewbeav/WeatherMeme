package layout;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import andrewbeav.github.io.weathermeme.JSONDownloader;
import andrewbeav.github.io.weathermeme.MainActivity;
import andrewbeav.github.io.weathermeme.EditLocationPopup;
import andrewbeav.github.io.weathermeme.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemePageFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tempText = (TextView) getActivity().findViewById(R.id.temp_text);
        humidityText = (TextView) getActivity().findViewById(R.id.humidity_text);
        windText = (TextView) getActivity().findViewById(R.id.wind_text);
        weatherDescriptionText = (TextView) getActivity().findViewById(R.id.weather_description_text);
        memeView = (ImageView) getActivity().findViewById(R.id.memeView);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meme_page, container, false);
    }

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    public static final int USE_CURRENT_LOCATION = 1;
    public static final int USE_CUSTOM_LOCATION = 2;

    private int locationType = USE_CURRENT_LOCATION;
    private String customLocation;

    private double latitude, longitude;

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void onConnected(Bundle connectionHint) {
        JSONDownloader jsonDownloader = new JSONDownloader(this);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            jsonDownloader.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=bcf98db996d2d93497a184c6af4c3c7a&units=imperial");
        }
        //jsonDownloader.execute("http://api.openweathermap.org/data/2.5/weather?q=Springfield,mo&units=imperial&&appid=bcf98db996d2d93497a184c6af4c3c7a&units=imperial");
    }

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
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
