package uk.ac.tees.com2060.oreo.ApiCallLib;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import uk.ac.tees.com2060.oreo.Utils;

public class ApiCall
{
    private static final String API_BASE_URL = "https://getshipr.com/api/";

    private Context context;
    private String endpoint;
    private RequestType type                    = RequestType.POST;
    private boolean auth                        = true;
    private HashMap<String, String> parameters  = new HashMap<>();
    private ResponseListener listener;

    public ApiCall(String endpoint, Context context)
    {
        this.context = context;
        this.endpoint = endpoint;
    }

    public ApiCall(String endpoint, RequestType type, Context context)
    {
        this.context = context;
        this.endpoint = endpoint;
        this.type = type;
    }

    public void setAuth(boolean auth)
    {
        this.auth = auth;
    }

    public void addParam(String key, String value)
    {
        this.parameters.put(key, value);
    }

    public void addResponseListener(ResponseListener listener)
    {
        this.listener = listener;
    }

    public int getMethod()
    {
        int returnValue;

        switch (this.type)
        {
            case GET:   returnValue = Request.Method.GET;
                        break;
            case POST:  returnValue = Request.Method.POST;
                        break;
            default:    returnValue = Request.Method.POST;
                        break;
        }

        return returnValue;
    }

    public void sendRequest()
    {
        if (listener == null)
            throw new NullPointerException("No ResponseListener specified");

        if (auth == true && Utils.getUserApiKey(context) == null)
            throw new NullPointerException("Authenticated API call requires API key");

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(getMethod(), (API_BASE_URL + this.endpoint), new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    listener.responseReceived(new ApiResponse(new JSONObject(response), context));
                } catch (JSONException e) { e.printStackTrace(); }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // TODO: handle errors
            }
        })
        {
            @Override
            protected HashMap<String, String> getParams()
            {
                return parameters;
            }

            @Override
            public HashMap<String, String> getHeaders()
            {
                if (auth == false)
                    return null;

                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + Utils.getUserApiKey(context));

                return headers;
            }
        };

        requestQueue.add(request);
    }

}
