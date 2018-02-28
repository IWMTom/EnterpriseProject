package uk.ac.tees.com2060.oreo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;

import java.util.HashMap;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

public class RegisterActivity extends AppCompatActivity implements RegisterStep1Fragment.RegisterStep1Listener, RegisterStep2Fragment.RegisterStep2Listener
{

    RegisterStep1Fragment step1Fragment = new RegisterStep1Fragment();
    RegisterStep2Fragment step2Fragment = new RegisterStep2Fragment();

    Bitmap profile_photo;
    String full_name, known_as, dob, postcode, phone_number, email_address, password, confirm_password;

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
    public void step1Listener(Bitmap profile_photo, String full_name,
                              String known_as, String dob)
    {
        this.profile_photo = profile_photo;
        this.full_name = full_name;
        this.known_as = known_as;
        this.dob = dob;

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.register_fragment_container, step2Fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        Bundle args = new Bundle();
        args.putString("known_as", this.known_as);

        step2Fragment.setArguments(args);
    }

    @Override
    public void step2Listener(String postcode, String phone_number,
                              String email_address, String password, String confirm_password)
    {
        this.postcode = postcode;
        this.phone_number = phone_number;
        this.email_address = email_address;
        this.password = password;
        this.confirm_password = confirm_password;

        ApiCall doRegister = new ApiCall("register", this);
        doRegister.setAuth(false);

        doRegister.addParam("full_name", this.full_name);
        doRegister.addParam("known_as", this.known_as);
        doRegister.addParam("dob", this.dob);
        doRegister.addParam("postcode", this.postcode);
        doRegister.addParam("phone_number", this.phone_number);
        doRegister.addParam("email", this.email_address);
        doRegister.addParam("password", this.password);
        doRegister.addParam("confirm_password", this.confirm_password);

        doRegister.addResponseListener(new ResponseListener()
        {
            @Override
            public void responseReceived(ApiResponse response)
            {
                if (response.success())
                {
                    try
                    {
                        System.out.println(response.getBody().get("token"));
                    } catch (JSONException e) { e.printStackTrace(); }
                }
                else
                {
                    System.out.println("ERROR! - " + response.getBody());
                }
            }
        });
        doRegister.sendRequest();

    }
}
