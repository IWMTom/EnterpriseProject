package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * SplashActivity.java
 *
 * The Activity class for handling the splash screen.
 * The splash screen will be displayed while user data is being cached for this session.
 * An API call is made to get user details, and this is cached using the User singleton.
 */
public class SplashActivity extends AppCompatActivity
{
    Activity activity = this;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Utils.getUserAccessToken(activity) != null)
        {
            intent = new Intent(activity, MainActivity.class);

            ApiCall getInfo = new ApiCall("user/details", this);
            getInfo.addResponseListener(new ResponseListener()
            {
                @Override
                public void responseReceived(ApiResponse response)
                {
                    if (response.success())
                    {
                        try
                        {
                            JSONObject body = response.getBody();

                            String profilePhoto;

                            if (body.get("profile_photo").toString().equals("null"))
                            {
                                profilePhoto = Utils.getStringFromImage(
                                        BitmapFactory.decodeResource(activity.getResources(),
                                                R.drawable.default_profile_photo));
                            }
                            else
                            {
                                profilePhoto = (String) body.get("profile_photo");
                            }

                            User user = User.getUser();
                            user.init(
                                    (int) body.get("id"),
                                    (String) body.get("full_name"),
                                    (String) body.get("known_as"),
                                    (String) body.get("email"),
                                    (String) body.get("postcode"),
                                    (String) body.get("dob"),
                                    profilePhoto
                            );
                        } catch (JSONException | ParseException e) { e.printStackTrace(); }

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Log.d("ERROR", response.getBody().toString());
                    }
                }
            });
            getInfo.sendRequest();
        }
        else
        {
            intent = new Intent(activity, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

    }
}
