package andrewbeav.github.io.weathermeme;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailedWeatherFragment extends Fragment {

    public DetailedWeatherFragment() {
        // Required empty public constructor
    }

    private TextView tempView, humidityView, pressureView, windView, windDirectionView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detailed_weather, container, false);

        tempView = (TextView) view.findViewById(R.id.detailed_temp_value);
        humidityView = (TextView) view.findViewById(R.id.detailed_humidity_value);
        pressureView = (TextView) view.findViewById(R.id.detailed_pressure_value);
        windView = (TextView) view.findViewById(R.id.detailed_wind_value);
        windDirectionView = (TextView) view.findViewById(R.id.detailed_wind_direction_value);

        // Inflate the layout for this fragment
        return view;
    }

    public void setupWithWeatherInfo(WeatherInfo weatherInfo) {
        tempView.setText(weatherInfo.getTemperature());
        humidityView.setText(String.valueOf(weatherInfo.getHumidity()));
        pressureView.setText(String.valueOf(weatherInfo.getPressure()));
        windView.setText(String.valueOf(weatherInfo.getWindSpeed()));
        windDirectionView.setText(weatherInfo.getWindDirection());
    }

}
