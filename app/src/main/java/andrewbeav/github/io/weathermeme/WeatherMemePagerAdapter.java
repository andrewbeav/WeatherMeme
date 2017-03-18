package andrewbeav.github.io.weathermeme;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by andrewbeav on 3/16/17.
 */

public class WeatherMemePagerAdapter extends FragmentPagerAdapter {

    Location location;

    public WeatherMemePagerAdapter(FragmentManager fm, Location location) {
        super(fm);

        this.location = location;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MemePageFragment fragment = new MemePageFragment();
                Bundle bundle = new Bundle();
                bundle.putDouble("Latitude", location.getLatitude());
                bundle.putDouble("Longitude", location.getLongitude());
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                DetailedWeatherFragment detailedWeatherFragment = new DetailedWeatherFragment();
                Bundle detailedWeatherBundle = new Bundle();
                detailedWeatherBundle.putDouble("Latitude", location.getLatitude());
                detailedWeatherBundle.putDouble("Longitude", location.getLongitude());
                detailedWeatherFragment.setArguments(detailedWeatherBundle);
                return detailedWeatherFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Meme";
            case 1:
                return "Weather";
            default:
                return null;
        }
    }
}
