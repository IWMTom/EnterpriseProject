package uk.ac.tees.com2060.oreo.ApiCallLib;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ApiResponse
{
    private Context context;
    private ResponseType type;
    private JSONObject body;

    public ApiResponse(JSONObject response, Context context)
    {
        this.context = context;

        if (response.has("success"))
        {
            this.type = ResponseType.SUCCESS;

            try
            {
                this.body = (JSONObject) response.get("success");
            } catch (JSONException e) { e.printStackTrace(); }
        }
        else
        {
            this.type = ResponseType.ERROR;

            try
            {
                if (response.get("error") instanceof String)
                    this.body = response;
                else
                    this.body = (JSONObject) response.get("error");
            } catch (JSONException e) { e.printStackTrace(); }
        }
    }

    public boolean success()
    {
        if (this.type == ResponseType.SUCCESS)
        {
            return true;
        }

        return false;
    }

    public JSONObject getBody()
    {
        return this.body;
    }

    public Context getContext()
    {
        return this.context;
    }

    public ArrayList<String> getErrors()
    {
        System.out.println(this.body);

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