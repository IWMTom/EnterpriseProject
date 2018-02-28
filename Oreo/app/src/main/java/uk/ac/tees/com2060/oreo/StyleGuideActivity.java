package uk.ac.tees.com2060.oreo;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import uk.ac.tees.com2060.oreo.ApiCallLib.*;

public class StyleGuideActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_guide);

        // Part of the fix for Google issue #63250768 relating to custom fonts for switches
        DataBindingUtil.setContentView(this, R.layout.activity_style_guide);

        Spinner spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.demo_spinner_day, R.layout.spinner_rounded_layout);
        adapter.setDropDownViewResource(R.layout.spinner_rounded_layout_item);
        spinner.setAdapter(adapter);

        Spinner spinner2 = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.demo_spinner_month, R.layout.spinner_rounded_layout);
        adapter2.setDropDownViewResource(R.layout.spinner_rounded_layout_item);
        spinner2.setAdapter(adapter2);

        Spinner spinner3 = findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.demo_spinner_year, R.layout.spinner_rounded_layout);
        adapter3.setDropDownViewResource(R.layout.spinner_rounded_layout_item);
        spinner3.setAdapter(adapter3);
    }
}
