package uk.ac.tees.com2060.oreo;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * RegisterActivity.java
 *
 * The Activity class for handling user registration, and the fragment UI that is built for it.
 * Registration is displayed in a two step process, each step having its own fragment - step 1 is
 * displayed when the activity is first launched, and step 2 displayed when the user proceeds.
 *
 * When all validation is complete and the user proceeds to submitting the form, an API call is made
 * to process the user's data on the backend web server - if successful, an access token is returned
 * and this is stored in SharedPreferences. The user is then brought to the main application screen.
 */
public class RegisterActivity extends AppCompatActivity implements RegisterStep1Fragment.RegisterStep1Listener, RegisterStep2Fragment.RegisterStep2Listener
{
    RegisterStep1Fragment step1Fragment = new RegisterStep1Fragment();
    RegisterStep2Fragment step2Fragment = new RegisterStep2Fragment();

    Bitmap profile_photo;
    String full_name, known_as, dob, postcode, phone_number, email_address, password, confirm_password;

    /**
     * Overridden default onCreate() method.
     * The layout file is specified, and the fragment manager displays the step 1 fragment.
     */
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

    /**
     * Callback to activity for the step 1 fragment.
     * Collects all user provided data, and displays the step 2 fragment.
     *
     * @param profile_photo profile photo (bitmap)
     * @param full_name full name
     * @param known_as known as
     * @param dob date of birth (DD-MM-YYYY)
     */
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

        // Passes "known as" name to the step 2 fragment
        Bundle args = new Bundle();
        args.putString("known_as", this.known_as);

        step2Fragment.setArguments(args);
    }

    /**
     * Callback to activity for the step 2 fragment.
     * Collects all user provided data, and issues an API call to register the user
     * with the backend web server. If successful, the user's access token is stored
     * and they are navigated to the main application - otherwise, an error is displayed.
     *
     * @param postcode postcode
     * @param phone_number mobile phone number
     * @param email_address email address
     * @param password password
     * @param confirm_password password confirmation
     */
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

        // If the user has provided a profile photo, convert to a Base64 string
        if (this.profile_photo != null)
            doRegister.addParam("profile_photo", Utils.getStringFromImage(this.profile_photo));

        doRegister.addResponseListener(new ResponseListener()
        {
            @Override
            public void responseReceived(ApiResponse response)
            {
                if (response.success())
                {
                    try
                    {

                        Utils.setUserAccessToken(response.getContext(),
                                response.getBody().get("token").toString());

                    } catch (JSONException e) { e.printStackTrace(); }

                    // Navigate to main application activity, and clearing the navigation back stack.
                    Intent intent = new Intent(response.getContext(), TestActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Utils.displayMessage(RegisterActivity.this, response.getErrors().get(0));
                }
            }
        });
        doRegister.sendRequest();
        Toast.makeText(this, R.string.submitting, Toast.LENGTH_SHORT).show();
    }

    /**
     * Enables correct fragment up navigation by overriding
     * the default implementation of the toolbar back button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
