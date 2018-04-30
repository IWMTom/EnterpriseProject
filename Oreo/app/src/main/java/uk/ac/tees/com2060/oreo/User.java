package uk.ac.tees.com2060.oreo;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * User.java
 * <p>
 * A singleton-based model class to store authenticated user data
 */
public class User implements Serializable {
    private static User userInstance = null;

    private int id, rep;
    private String fullName, knownAs, emailAddress, postcode, mobileNumber;
    private Date dob;
    private Bitmap profilePhoto;

    /**
     * Empty constructor
     */
    private User() {
    }

    /**
     * Initialises an empty User
     *
     * @param fullName     full name
     * @param knownAs      known as name
     * @param emailAddress email address
     * @param postcode     postcode
     * @param dob          date of birth
     * @param profilePhoto profile photo string
     * @param mobileNumber mobile number
     * @param context      context for rep api call
     */
    public void init(int id, String fullName, String knownAs, String emailAddress, String postcode,
                     String dob, String profilePhoto, String mobileNumber, Context context) throws ParseException {
        this.id = id;
        this.fullName = fullName;
        this.knownAs = knownAs;
        this.emailAddress = emailAddress;
        this.postcode = postcode;
        this.dob = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
        this.profilePhoto = Utils.getImageFromString(profilePhoto);
        this.mobileNumber = mobileNumber;
        rep = getRep(context);
    }

    public void updatePushToken(Context c) {
        ApiCall api = new ApiCall("user/updatePushToken", c);
        api.addParam("push_token", FirebaseInstanceId.getInstance().getToken());
        api.addResponseListener(new ResponseListener() {
            @Override
            public void responseReceived(ApiResponse response) {
                if (response.success()) {
                }
            }
        });
        api.sendRequest();
    }

    /**
     * Gets instance of user (singleton)
     *
     * @return
     */
    public static User getUser() {
        if (userInstance == null) {
            userInstance = new User();
        }

        return userInstance;
    }

    public int id() {
        return this.id;
    }

    /**
     * Gets full name
     *
     * @return full name
     */
    public String fullName() {
        return this.fullName;
    }

    /**
     * Gets known as name
     *
     * @return known as name
     */
    public String knownAs() {
        return this.knownAs;
    }

    /**
     * Gets email address
     *
     * @return email address
     */
    public String emailAddress() {
        return this.emailAddress;
    }

    /**
     * Gets postcode
     *
     * @return postcode
     */
    public String postcode() {
        return this.postcode;
    }

    /**
     * Gets date of birth
     *
     * @return date of birth
     */
    public Date dob() {
        return this.dob;
    }

    /**
     * Gets profile photo
     *
     * @return profile photo
     */
    public Bitmap profilePhoto() {
        return this.profilePhoto;
    }

    /**
     * Gets mobile number
     *
     * @return mobile number
     */
    public String mobileNumber() {
        return this.mobileNumber;
    }

    public int getRep(Context context) {
        ApiCall getUserData = new ApiCall("user/" + id, context);
        getUserData.addResponseListener(new ResponseListener() {

            @Override
            public void responseReceived(ApiResponse response) {
                if (response.success()) {
                    try {
                        rep = response.getBody().getInt("reputation");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
        return rep;
    }

    public void setMobileNumber(String mobile) {
        this.mobileNumber = mobile;
    }

    public void setProfilePhoto(Bitmap bmp) {
        profilePhoto = bmp;
    }

    public void setAlias(String s) {
        knownAs = s;
    }

    public void setFullName(String s) {
        fullName = s;
    }

    public void setPostcode(String s) {
        postcode = s;
    }

    public void setEmailAddress(String s) {
        emailAddress = s;
    }
}