package andrewbeav.github.io.weathermeme;

import android.app.Fragment;

/**
 * Created by andrewbeav on 3/18/17.
 */

public interface MainActivityFragment {
    public abstract void populateWithWeatherInfo(WeatherInfo weatherInfo);
}
