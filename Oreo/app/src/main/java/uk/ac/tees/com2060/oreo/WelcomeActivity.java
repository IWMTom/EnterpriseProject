package uk.ac.tees.com2060.oreo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

public class WelcomeActivity extends AppCompatActivity
{
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (Utils.getUserApiKey(this) != null)
        {
            Intent intent = new Intent(this, TestActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    private boolean validateFields()
    {
        if (email.length() == 0 || password.length() == 0)
        {
            return false;
        }

        return true;
    }

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
                            Utils.setUserApiKey(response.getContext(), response.getBody().get("token").toString());
                        } catch (JSONException e) { e.printStackTrace(); }

                        Intent intent = new Intent(response.getContext(), TestActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Utils.displayMessage(WelcomeActivity.this, response.getErrors().get(0), R.string.okay);
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

    public void openRegister(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void openStyleGuide(View view)
    {
        Intent intent = new Intent(this, StyleGuideActivity.class);
        startActivity(intent);
    }

}
