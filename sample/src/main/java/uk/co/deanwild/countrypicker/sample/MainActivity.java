package uk.co.deanwild.countrypicker.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.countrypicker.CountryPicker;
import com.countrypicker.CountryPickerListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final CountryPicker picker = CountryPicker.newInstance("select country", "remove country");

        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code) {
                picker.dismissAllowingStateLoss();
            }
        });

        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
    }
}
