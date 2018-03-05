package uk.ac.tees.com2060.oreo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * WelcomeActivity.java
 *
 * The Activity class used to handle the initial application entry point.
 * If a user is authenticated, they are redirected to the main application - otherwise,
 * the welcome screen (login and registration) is displayed.
 */
public class WelcomeActivity extends AppCompatActivity
{
    String email, password;

    /**
     * Overriddes default onCreate() method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    /**
     * Validates login fields
     * @return true if fields are valid
     */
    private boolean validateFields()
    {
        if (email.length() == 0 || password.length() == 0)
        {
            return false;
        }
        return true;
    }

    /**
     * Called when the login button is clicked.
     *
     * Login fields are validated, and if valid, an API call is made to the backend web server
     * to check the provided credentials. If valid, the access token is stored and the user
     * navigated to the application - otherwise, an error message is displayed.
     */
    public void doLogin(View view)
    {
        email       = ((EditText) findViewById(R.id.editText_email)).getText().toString();
        password    = ((EditText) findViewById(R.id.editText_password)).getText().toString();

        if (validateFields())
        {
            ApiCall doLogin = new ApiCall("login", this);
            doLogin.setAuth(false);

            doLogin.addParam("email", email);
            doLogin.addParam("password", password);

            doLogin.addResponseListener(new ResponseListener()
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

                        // Navigates to main activity, clearing the navigation back stack
                        Intent intent = new Intent(response.getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Utils.displayMessage(WelcomeActivity.this, response.getErrors().get(0));
                    }
                }
            });
            doLogin.sendRequest();
            Toast.makeText(this, R.string.submitting, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Utils.displayMessage(this, R.string.validation_complete_all_fields, R.string.okay);
        }
    }

    /**
     * Called when the register button is clicked.
     * Navigates the user to the registration activity.
     */
    public void openRegister(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * TESTING - TO REMOVE
     */
    public void openStyleGuide(View view)
    {
        Intent intent = new Intent(this, StyleGuideActivity.class);
        startActivity(intent);
    }

}
