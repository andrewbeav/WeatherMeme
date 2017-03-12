package andrewbeav.github.io.weathermeme;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void setMeme(String imageName) {
        Resources res = getResources();
        int resourceId = res.getIdentifier(imageName, "drawable", getPackageName());

        memeView.setImageDrawable(res.getDrawable(resourceId));
    }

    public void updateCityName(String cityName) {
            this.cityNameText.setText(cityName);
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

    public void showToast(String message, int length) {
        Toast toast = Toast.makeText(getApplicationContext(), message, length);
        toast.show();
    }

    private TextView cityNameText;
    private TextView tempText;
    private TextView humidityText;
    private TextView windText;
    private TextView weatherDescriptionText;
    private ImageView memeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempText = (TextView) findViewById(R.id.temp_text);
        humidityText = (TextView) findViewById(R.id.humidity_text);
        cityNameText = (TextView) findViewById(R.id.cityNameView);
        windText = (TextView) findViewById(R.id.wind_text);
        weatherDescriptionText = (TextView) findViewById(R.id.weather_description_text);
        memeView = (ImageView) findViewById(R.id.memeView);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }



    private double latitude, longitude;

    public void onConnected(Bundle connectionHint) {
        JSONDownloader jsonDownloader = new JSONDownloader(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    public void editLocation(View view) {
        startActivityForResult(new Intent(MainActivity.this, EditLocationPopup.class), EditLocationPopup.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EditLocationPopup.REQUEST_CODE && resultCode == RESULT_OK) {
            // Process location stuff...
        }
    }

    public void refreshWeather(View view) {
        JSONDownloader jsonDownloader = new JSONDownloader(this);
        jsonDownloader.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=bcf98db996d2d93497a184c6af4c3c7a&units=imperial");

        //jsonDownloader.execute("http://api.openweathermap.org/data/2.5/weather?q=Springfield,mo&units=imperial&&appid=bcf98db996d2d93497a184c6af4c3c7a&units=imperial");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
