package uk.ac.tees.com2060.oreo;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Part of the fix for Google issue #63250768 relating to custom fonts for switches
        DataBindingUtil.setContentView(this, R.layout.activity_welcome);
    }
}
