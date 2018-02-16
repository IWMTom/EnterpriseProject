package uk.ac.tees.com2060.oreo;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ApiUtil {

    private Context context;
    private RequestQueue requestQueue;
    private String apiRoot = "https://getshipr.com/api/";

    ApiUtil(Context c) {
        context = c;

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }

    public void Post(String endpoint, final HashMap<String, String> params, final VolleyCallback callback){
        Post(true, endpoint, params, callback);
    }

    public void Post(final boolean auth, String endpoint,final HashMap<String, String> params, final VolleyCallback callback) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiRoot.concat(endpoint),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        callback.onSuccessResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getParams() {

                return params;
            }

            @Override
            public Map<String, String> getHeaders(){
                Map<String,String> header = new HashMap<String, String>();
                header.put("Content-Type","form-data");
                return header;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void Get() {

    }
}
