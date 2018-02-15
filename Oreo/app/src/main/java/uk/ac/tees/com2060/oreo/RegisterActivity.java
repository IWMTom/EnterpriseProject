package uk.ac.tees.com2060.oreo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class RegisterActivity extends AppCompatActivity implements RegisterStep1Fragment.RegisterStep1Listener, RegisterStep2Fragment.RegisterStep2Listener
{

    RegisterStep1Fragment step1Fragment = new RegisterStep1Fragment();
    RegisterStep2Fragment step2Fragment = new RegisterStep2Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (findViewById(R.id.register_fragment_container) != null)
        {
            if (savedInstanceState != null)
                return;

            getFragmentManager().beginTransaction()
                    .add(R.id.register_fragment_container, step1Fragment).commit();
        }
    }

    @Override
    public void step1Listener()
    {
        setTitle(R.string.activity_register_step2_title);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.register_fragment_container, step2Fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void step2Listener()
    {
        setTitle(R.string.activity_register_step1_title);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.register_fragment_container, step1Fragment);
        transaction.commit();
    }
}
