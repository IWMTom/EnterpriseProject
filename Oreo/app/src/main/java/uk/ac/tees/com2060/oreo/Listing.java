package uk.ac.tees.com2060.oreo;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

public class Listing implements Serializable
{
    private int id, user_id;
    private String item_description, item_size, important_details, collection_city, delivery_city;
    private float distance;

    public Listing (int id, int user_id, String item_description, String item_size, String important_details,
                   String collection_city, String delivery_city, float distance)
    {
        this.id = id;
        this.user_id = user_id;
        this.item_description = item_description;
        this.item_size = item_size;
        this.important_details = important_details;
        this.collection_city = collection_city;
        this.delivery_city = delivery_city;
        this.distance = distance;
    }

    public static ArrayList<Listing> getListings(JSONArray json)
    {
        final ArrayList<Listing> al = new ArrayList<>();

        for(int i = 0; i < json.length(); i++)
        {
            try
            {
                JSONObject object = json.getJSONObject(i);

                al.add(new Listing(
                        object.getInt("id"),
                        object.getInt("user_id"),
                        object.getString("item_description"),
                        object.getString("item_size"),
                        object.getString("important_details"),
                        object.getString("collection_city"),
                        object.getString("delivery_city"),
                        Float.parseFloat(object.getString("distance"))
                ));

            } catch (JSONException e) { e.printStackTrace(); }
        }

        return al;
    }

    public int id() { return this.id; }

    public int user_id() { return this.user_id; }

    public String itemDescription()
    {
        return this.item_description;
    }

    public String itemSize()
    {
        return this.item_size;
    }

    public String importantDetails()
    {
        return this.important_details;
    }

    public String collectionCity() { return this.collection_city; }

    public String deliveryCity()
    {
        return this.delivery_city;
    }

    public float distance() {return this.distance; }
}