package uk.ac.tees.com2060.oreo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ApiCall getInfo = new ApiCall("user/details", this);

        getInfo.addResponseListener(new ResponseListener()
        {
            @Override
            public void responseReceived(ApiResponse response)
            {
                if (response.success())
                {
                    ImageView imageView_test    = findViewById(R.id.imageView_test);
                    TextView textView_test      = findViewById(R.id.textView_test);

                    try
                    {
                        textView_test.setText((String) response.getBody().get("full_name"));
                        imageView_test.setImageBitmap(Utils.getImageFromString((String) response.getBody().get("profile_photo")));
                    } catch (JSONException e) { e.printStackTrace(); }
                }
                else
                {
                    System.out.println("ERROR! - " + response.getBody());
                }
            }
        });
        getInfo.sendRequest();
    }
}
