package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.FontRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.widget.Switch;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Utils.java
 * <p>
 * A collection of utility functions for use within the application
 */
public class Utils {
    /**
     * Part of the fix for Google issue #63250768 relating to custom fonts for switches.
     * Enables the use of app:labelTypeface within XML layouts on switch components.
     */
    @BindingAdapter("labelTypeface")
    public static void setLabelTypeface(Switch view, @FontRes int id) {
        view.setTypeface(ResourcesCompat.getFont(view.getContext(), id));
    }

    /**
     * Displays an alert dialog with "okay" button
     *
     * @param activity current activity
     * @param message  message (string)
     */
    static void displayMessage(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setPositiveButton(R.string.okay, null);
        builder.create();
        builder.show();
    }

    /**
     * Displays an alert dialog with custom button
     *
     * @param activity         current activity
     * @param message_resource string resource for message
     * @param button_resource  string resource for button
     */
    static void displayMessage(Activity activity, int message_resource, int button_resource) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message_resource)
                .setPositiveButton(button_resource, null);
        builder.create();
        builder.show();
    }

    /**
     * Sets user access token in SharedPreferences
     *
     * @param context current context
     * @param key     user access token
     */
    static void setUserAccessToken(Context context, String key) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.shared_preferences_user_data), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(context.getString(R.string.shared_preferences_user_data_api_key), key);
        editor.apply();
    }

    /**
     * Gets user access token from SharedPreferences
     *
     * @param context current context
     * @return user access token
     */
    public static String getUserAccessToken(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.shared_preferences_user_data), Context.MODE_PRIVATE);

        return sharedPrefs.getString(context.getString(R.string.shared_preferences_user_data_api_key), null);
    }

    /**
     * Removes user access token from SharedPreferences
     *
     * @param context current context
     */
    static void removeUserAccessToken(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.shared_preferences_user_data), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(context.getString(R.string.shared_preferences_user_data_api_key));
        editor.apply();
    }

    /**
     * Converts a Bitmap image to a Base64 encoded string
     *
     * @param img bitmap image
     * @return Base64 encoded string
     */
    static String getStringFromImage(Bitmap img) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    /**
     * Converts a Base64 encoded string to a Bitmap image
     *
     * @param str Base64 encoded string
     * @return Bitmap image
     */
    static Bitmap getImageFromString(String str) {
        byte[] decodedString = Base64.decode(str, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    static void clearTemporaryStorage(Activity a) {
        a.getPreferences(Context.MODE_PRIVATE).edit().clear().apply();
    }

    static int sizeStringToInt(String s) {
        switch (s) {
            case "small":
                return 0;
            case "medium":
                return 1;
            case "large":
                return 2;
            case "xlarge":
                return 3;
            case "huge":
                return 4;
            default:
                return -1;
        }
    }

    static ArrayList<Listing> FilterList(ArrayList<Listing> list, int size) {

        if (list == null) {
            return null;
        }

        ArrayList<Listing> returnList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if ((Utils.sizeStringToInt(list.get(i).itemSize())) <= size) {
                Listing listing = new Listing(
                        list.get(i).id(),
                        list.get(i).user_id(),
                        list.get(i).itemDescription(),
                        list.get(i).itemSize(),
                        list.get(i).importantDetails(),
                        list.get(i).collectionCity(),
                        list.get(i).deliveryCity(),
                        list.get(i).distance(),
                        list.get(i).maxBid(),
                        list.get(i).minBid(),
                        list.get(i).averageBid());
                returnList.add(listing);
            }
        }
        return returnList;
    }

}
