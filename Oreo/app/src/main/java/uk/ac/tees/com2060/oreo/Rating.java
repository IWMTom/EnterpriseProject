package uk.ac.tees.com2060.oreo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Rating implements Serializable {
    int id;
    int raterId;
    int ratedId;
    String type;
    String message;

    static ArrayList<Rating> getRatings(JSONArray json) {
        final ArrayList<Rating> al = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            try {
                JSONObject object = json.getJSONObject(i);

                al.add(new Rating(
                        object.getInt("id"),
                        object.getInt("user_id"),
                        object.getInt("reviewed_id"),
                        object.getString("type"),
                        object.getString("message")
                ));

        } catch(JSONException e){
            e.printStackTrace();
        }
    }

        return al;
}


    Rating(int _id, int _raterId, int _ratedId, String _type, String _message) {
        id = _id;
        raterId = _raterId;
        ratedId = _ratedId;
        type = _type;
        message = _message;
    }

    public int id() {
        return this.id;
    }

    int raterId() {
        return this.raterId;
    }

    int ratedId() {

        return this.ratedId;
    }

    String message() {

        return this.message;
    }

    String type() {

        return this.type;
    }
}
