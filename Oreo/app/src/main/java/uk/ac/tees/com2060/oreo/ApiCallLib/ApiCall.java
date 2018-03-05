package uk.ac.tees.com2060.oreo.ApiCallLib;

import android.content.Context;
import android.widget.Toast;

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

/**
 * ApiCall.java (ApiCallLib)
 * A wrapper for the Volley HTTP API, to make sending simple POST and GET
 * requests easier throughout the application. Parameters are stored in an
 * abstracted HashMap with only the put() method being exposed. A response listener
 * needs to be provided in the form of an anonymous function when using ApiCall,
 * which handles the success and error states of the API response.
 *
 * Authentication is required by default, and the default method is POST.
 */
public class ApiCall
{
    private static final String API_BASE_URL = "https://getshipr.com/api/";

    private Context context;
    private String endpoint;
    private RequestType type                    = RequestType.POST;
    private boolean auth                        = true;
    private HashMap<String, String> parameters  = new HashMap<>();
    private ResponseListener listener;

    /**
     * Class constructor
     * @param endpoint API endpoint
     * @param context current context (this)
     */
    public ApiCall(String endpoint, Context context)
    {
        this.context = context;
        this.endpoint = endpoint;
    }

    /**
     * Overloaded class constructor
     * @param endpoint API endpoint
     * @param type request type (RequestType.POST, RequestType.GET)
     * @param context current context (this)
     */
    public ApiCall(String endpoint, RequestType type, Context context)
    {
        this.context = context;
        this.endpoint = endpoint;
        this.type = type;
    }

    /**
     * Sets whether authentication is required for the call
     * @param auth authentication required?
     */
    public void setAuth(boolean auth)
    {
        this.auth = auth;
    }

    /**
     * Adds a parameter to the HashMap, in String key value format
     * @param key parameter key
     * @param value parameter value
     */
    public void addParam(String key, String value)
    {
        this.parameters.put(key, value);
    }

    /**
     * Adds a response listener
     * @param listener anonymous function for listener
     */
    public void addResponseListener(ResponseListener listener)
    {
        this.listener = listener;
    }

    /**
     * Gets the current request method in Volley format
     * @return request method
     */
    private int getMethod()
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

    /**
     * Sends a request using the specified parameters
     * Throws a NullPointerException if no response listener is specified
     * Throws a NullPointerException if auth is required but there is no access token available
     */
    public void sendRequest()
    {
        if (listener == null)
            throw new NullPointerException("No ResponseListener specified");
        if (auth == true && Utils.getUserAccessToken(context) == null)
            throw new NullPointerException("Authenticated API call requires access token");

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
                Toast.makeText(context, "Server error!", Toast.LENGTH_LONG);
            }
        })
        {
            @Override
            protected HashMap<String, String> getParams()
            {
                return parameters;
            }

            /**
             * If authentication is specified, attach the access token as a header
             */
            @Override
            public HashMap<String, String> getHeaders()
            {
                HashMap<String, String> headers = new HashMap<>();

                if (auth)
                    headers.put("Authorization", "Bearer " + Utils.getUserAccessToken(context));

                return headers;
            }
        };

        requestQueue.add(request);
    }

}
