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
     * @param fullName     full title
     * @param knownAs      known as title
     * @param emailAddress email address
     * @param postcode     postcode
     * @param dob          date of birth
     * @param profilePhoto profile photo string
     * @param mobileNumber mobile number
     * @param context      context for rep api call
     */
    void init(int id, String fullName, String knownAs, String emailAddress, String postcode,
              String dob, String profilePhoto, String mobileNumber, int reputation, Context context) throws ParseException {
        this.id = id;
        this.fullName = fullName;
        this.knownAs = knownAs;
        this.emailAddress = emailAddress;
        this.postcode = postcode;
        this.dob = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
        this.profilePhoto = Utils.getImageFromString(profilePhoto);
        this.mobileNumber = mobileNumber;
        this.rep = reputation;
    }

    void updatePushToken(Context c) {
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
     * @return returns User singleton
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
     * Gets full title
     *
     * @return full title
     */
    String fullName() {
        return this.fullName;
    }

    /**
     * Gets known as title
     *
     * @return known as title
     */
    String knownAs() {
        return this.knownAs;
    }

    /**
     * Gets email address
     *
     * @return email address
     */
    String emailAddress() {
        return this.emailAddress;
    }

    /**
     * Gets postcode
     *
     * @return postcode
     */
    String postcode() {
        return this.postcode;
    }

    /**
     * Gets date of birth
     *
     * @return date of birth
     */
    Date dob() {
        return this.dob;
    }

    /**
     * Gets profile photo
     *
     * @return profile photo
     */
    Bitmap profilePhoto() {
        return this.profilePhoto;
    }

    /**
     * Gets mobile number
     *
     * @return mobile number
     */
    String mobileNumber() {
        return this.mobileNumber;
    }

    void fetchRep(Context context) {
        ApiCall getUserData = new ApiCall("user/" + id, context);

        getUserData.addResponseListener(new ResponseListener() {
            @Override
            public void responseReceived(ApiResponse response) {
                if (response.success()) {
                    try {
                       setRep(response.getBody().getInt("reputation"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        getUserData.sendRequest();
    }

    int getRep() {return rep;}

    void setRep(int i) {rep = i;}

    void setMobileNumber(String mobile) {
        this.mobileNumber = mobile;
    }

    void setProfilePhoto(Bitmap bmp) {
        profilePhoto = bmp;
    }

    void setAlias(String s) {
        knownAs = s;
    }

    void setFullName(String s) {
        fullName = s;
    }

    void setPostcode(String s) {
        postcode = s;
    }
}