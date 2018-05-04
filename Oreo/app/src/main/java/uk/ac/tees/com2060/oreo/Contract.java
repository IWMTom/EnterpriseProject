package uk.ac.tees.com2060.oreo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Contract implements Serializable {
    String item_description, courier_alias, sender_alias, bid_message;
    int courierId, senderId, listing_id, id;
    double bid_amount;
    boolean collected, delivered, confirmed;

    public Contract(String item_description, String courier_alias, String sender_alias,
                    int sender_id, int courier_id, int id, int listing_id,
                    boolean collected, boolean delivered, boolean confirmed,
                    String bid_message, double bid_amount) {

        this.listing_id = listing_id;
        this.item_description = item_description;
        this.courier_alias = courier_alias;
        this.sender_alias = sender_alias;
        this.courierId = courier_id;
        this.senderId = sender_id;
        this.id = id;
        this.bid_message = bid_message;
        this.bid_amount = bid_amount;
        this.collected = collected;
        this.delivered = delivered;
        this.confirmed = confirmed;
    }

    static ArrayList<Contract> getContracts(JSONArray json) {
        final ArrayList<Contract> al = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            try {
                JSONObject object = json.getJSONObject(i);

                al.add(new Contract(

                        object.getString("item_description"),
                        object.getString("courier_alias"),
                        object.getString("shipper_alias"),
                        object.getInt("shipper_id"),
                        object.getInt("courier_id"),
                        object.getInt("id"),
                        object.getInt("listing_id"),
                        object.getInt("collected") == 1,
                        object.getInt("delivered") == 1,
                        object.getInt("confirmed") == 1,
                        object.getString("bid_message"),
                        object.getDouble("bid_amount")
                ));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return al;
    }

}