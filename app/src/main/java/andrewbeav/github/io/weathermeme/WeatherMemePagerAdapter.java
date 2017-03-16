package andrewbeav.github.io.weathermeme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by andrewbeav on 3/16/17.
 */

public class WeatherMemePagerAdapter extends FragmentPagerAdapter {

    public WeatherMemePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MemePageFragment();
            case 1:
                return new DetailedWeatherFragment();
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
