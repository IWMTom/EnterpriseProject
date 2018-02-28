package uk.ac.tees.com2060.oreo.ApiCallLib;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponse
{
    private ResponseType type;
    private JSONObject body;

    public ApiResponse(JSONObject response)
    {
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
}