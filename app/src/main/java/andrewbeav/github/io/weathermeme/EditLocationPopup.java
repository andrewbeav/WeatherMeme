package andrewbeav.github.io.weathermeme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

/**
 * Created by andrewbeav on 3/12/17.
 */

class EditLocationPopup extends Activity {
    public static final int REQUEST_CODE = 888;

    public static final double WIDTH_PERCENT = 0.8;
    public static final double HEIGHT_PERCENT = 0.4;

    private EditText locationText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_location_popup);

        // Getting the EditText from the layout
        locationText = (EditText) findViewById(R.id.location_text);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;

        this.getWindow().setLayout((int) (width*WIDTH_PERCENT), (int) (height*HEIGHT_PERCENT));
    }

    public void submitLocation(View view) {
        Intent intent = new Intent();
        intent.putExtra("Location", locationText.getText());
        setResult(RESULT_OK);
        finish();
    }

    public void useCurrentLocation(View view) {

    }
}
