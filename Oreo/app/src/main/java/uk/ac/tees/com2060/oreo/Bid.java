package uk.ac.tees.com2060.oreo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Bid implements Serializable
{
    private int id, user_id;
    private String user_name, message;
    private double amount;

    public Bid (int id, int user_id, String user_name, String message, double amount)
    {
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.message = message;
        this.amount = amount;
    }

    public int id() { return this.id; }

    public int user_id() { return this.user_id; }

    public String userName()
    {
        return this.user_name;
    }

    public String message()
    {
        return this.message;
    }

    public double amount()
    {
        return this.amount;
    }
}