package uk.ac.tees.com2060.oreo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Listing implements Serializable {
    private int id, user_id;
    private String item_description, item_size, important_details, collection_city, delivery_city;
    private float distance, max_bid, min_bid, average_bid;

    Listing(int id, int user_id, String item_description, String item_size, String important_details,
            String collection_city, String delivery_city, float distance, float max_bid, float min_bid,
            float average_bid) {
        this.id = id;
        this.user_id = user_id;
        this.item_description = item_description;
        this.item_size = item_size;
        this.important_details = important_details;
        this.collection_city = collection_city;
        this.delivery_city = delivery_city;
        this.distance = distance;
        this.max_bid = max_bid;
        this.min_bid = min_bid;
        this.average_bid = average_bid;
    }

    static Listing getListing(JSONArray data) {
        return getListings(data).get(0);
    }

    static ArrayList<Listing> getListings(JSONArray json) {
        final ArrayList<Listing> al = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            try {
                JSONObject object = json.getJSONObject(i);

                al.add(new Listing(
                        object.getInt("id"),
                        object.getInt("user_id"),
                        object.getString("item_description"),
                        object.getString("item_size"),
                        object.getString("important_details"),
                        object.getString("collection_city"),
                        object.getString("delivery_city"),
                        Float.parseFloat(object.getString("distance")),
                        Float.parseFloat(object.getString("max_bid")),
                        Float.parseFloat(object.getString("min_bid")),
                        Float.parseFloat(object.getString("average_bid"))
                ));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return al;
    }

    public int id() {
        return this.id;
    }

    int user_id() {
        return this.user_id;
    }

    String itemDescription() {
        return this.item_description;
    }

    String itemSize() {
        return this.item_size;
    }

    String importantDetails() {
        return this.important_details;
    }

    String collectionCity() {
        return this.collection_city;
    }

    String deliveryCity() {
        return this.delivery_city;
    }

    float distance() {
        return this.distance;
    }

    float maxBid() {
        return this.max_bid;
    }

    float minBid() {
        return this.min_bid;
    }

    float averageBid() {
        return this.average_bid;
    }


}