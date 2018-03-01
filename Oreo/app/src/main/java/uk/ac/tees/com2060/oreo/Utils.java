package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

/**
 * Utils.java
 *
 * A collection of utility functions for use within the application
 */
public class Utils
{
    /**
     * Part of the fix for Google issue #63250768 relating to custom fonts for switches.
     * Enables the use of app:labelTypeface within XML layouts on switch components.
     */
    @BindingAdapter("labelTypeface")
    public static void setLabelTypeface(Switch view, @FontRes int id)
    {
        view.setTypeface(ResourcesCompat.getFont(view.getContext(), id));
    }

    /**
     * Displays an alert dialog with "okay" button
     * @param activity current activity
     * @param message message (string)
     */
    public static void displayMessage(Activity activity, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setPositiveButton(R.string.okay, null);
        builder.create();
        builder.show();
    }

    /**
     * Displays an alert dialog with custom button
     * @param activity current activity
     * @param message_resource string resource for message
     * @param button_resource string resource for button
     */
    public static void displayMessage(Activity activity, int message_resource, int button_resource)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message_resource)
                .setPositiveButton(button_resource, null);
        builder.create();
        builder.show();
    }

    /**
     * Sets user access token in SharedPreferences
     * @param context current context
     * @param key user access token
     */
    public static void setUserAccessToken(Context context, String key)
    {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.shared_preferences_user_data), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(context.getString(R.string.shared_preferences_user_data_api_key), key);
        editor.apply();
    }

    /**
     * Gets user access token from SharedPreferences
     * @param context current context
     * @return user access token
     */
    public static String getUserAccessToken(Context context)
    {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.shared_preferences_user_data), Context.MODE_PRIVATE);

        return sharedPrefs.getString(context.getString(R.string.shared_preferences_user_data_api_key), null);
    }

    /**
     * Converts a Bitmap image to a Base64 encoded string
     * @param img bitmap image
     * @return Base64 encoded string
     */
    public static String getStringFromImage(Bitmap img)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        return encodedImage;
    }

    /**
     * Converts a Base64 encoded string to a Bitmap image
     * @param str Base64 encoded string
     * @return Bitmap image
     */
    public static Bitmap getImageFromString(String str)
    {
        byte[] decodedString = Base64.decode(str, Base64.NO_WRAP);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
