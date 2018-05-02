package uk.ac.tees.com2060.oreo.ApiCallLib;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * ApiResponse.java (ApiCallLib)
 * A wrapper that formats an API response.
 *
 * The response is broken down, and the type set based on the
 * first key (success or error). Errors are broken down and put into
 * an ArrayList of type String so they are more easily accessible.
 */
public class ApiResponse
{
    private Context context;
    private ResponseType type;
    private JSONObject body;
    private JSONArray bodyArray;

    /**
     * Class constructor
     * @param response JSON formatted response
     * @param context current context (this)
     */
    public ApiResponse(JSONObject response, Context context)
    {
        this.context = context;

        if (response.has("success"))
        {
            this.type = ResponseType.SUCCESS;

            try
            {
                if (response.get("success") instanceof JSONArray)
                {
                    this.bodyArray = (JSONArray) response.get("success");
                }
                else
                {
                    // Gets response message after the first key
                    this.body = (JSONObject) response.get("success");
                }
            } catch (JSONException e) { e.printStackTrace(); }
        }
        else
        {
            this.type = ResponseType.ERROR;

            try
            {
                /**
                 * If there is more than a single error message, the errors
                 * are converted to a JSONObject for further formatting.
                 */
                if (!(response.get("error") instanceof String))
                    this.body = (JSONObject) response.get("error");
                else
                    this.body = response;

            } catch (JSONException e) { e.printStackTrace(); }
        }
    }

    /**
     * Checks if the request was successful
     * @return success
     */
    public boolean success()
    {
        if (this.type == ResponseType.SUCCESS)
        {
            return true;
        }

        return false;
    }

    /**
     * Gets response body
     * @return response body
     */
    public JSONObject getBody()
    {
        return this.body;
    }

    public JSONArray getBodyArray()
    {
        return this.bodyArray;
    }

    /**
     * Gets current context
     * @return current context
     */
    public Context getContext()
    {
        return this.context;
    }

    /**
     * Breaks down errors into an ArrayList of type String.
     *
     * If the response body contains the error key, there is only a
     * single error message to add to the ArrayList, otherwise, the
     * response body is iterated over and each error message added
     * to the ArrayList.
     *
     * @return ArrayList (String) of errors
     */
    public ArrayList<String> getErrors()
    {
        ArrayList<String> errors = new ArrayList<>();

        if (this.body.has("error"))
        {
            try
            {
                errors.add((String) this.body.get("error"));
            } catch (JSONException e) { e.printStackTrace(); }
        }
        else
        {
            Iterator<String> errorsIterator = this.body.keys();
            while (errorsIterator.hasNext())
            {
                JSONArray array = null;

                try
                {
                    array = new JSONArray(this.body.optString(errorsIterator.next()));
                    errors.add(array.getString(0));
                } catch (JSONException e) { e.printStackTrace(); }

            }
        }

        return errors;
    }
}