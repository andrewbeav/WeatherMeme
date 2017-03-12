package andrewbeav.github.io.weathermeme;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

/**
 * Created by andrewbeav on 3/12/17.
 */

class EditLocationPopup extends Activity {
    public static final double WIDTH_PERCENT = 0.8;
    public static final double HEIGHT_PERCENT = 0.8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_location_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;

        this.getWindow().setLayout((int) (width*WIDTH_PERCENT), (int) (height*HEIGHT_PERCENT));
    }
}
