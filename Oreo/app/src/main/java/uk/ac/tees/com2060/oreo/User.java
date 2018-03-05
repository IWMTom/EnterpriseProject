package uk.ac.tees.com2060.oreo;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User.java
 *
 * A singleton-based model class to store authenticated user data
 */
public class User implements Serializable
{
    private static User userInstance = null;

    private String fullName, knownAs, emailAddress, postcode;
    private Date dob;
    private Bitmap profilePhoto;

    /**
     * Empty constructor
     */
    private User() {}

    /**
     * Initialises an empty User
     * @param fullName full name
     * @param knownAs known as name
     * @param emailAddress email address
     * @param postcode postcode
     * @param dob date of birth
     * @param profilePhoto profile photo string
     */
    public void init(String fullName, String knownAs, String emailAddress, String postcode, String dob, String profilePhoto) throws ParseException
    {
        this.fullName = fullName;
        this.knownAs = knownAs;
        this.emailAddress = emailAddress;
        this.postcode = postcode;
        this.dob = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
        this.profilePhoto = Utils.getImageFromString(profilePhoto);
    }

    /**
     * Gets instance of user (singleton)
     * @return
     */
    public static User getUser()
    {
        if (userInstance == null)
        {
            userInstance = new User();
        }

        return userInstance;
    }

    /**
     * Gets full name
     * @return full name
     */
    public String fullName()
    {
        return this.fullName;
    }

    /**
     * Gets known as name
     * @return known as name
     */
    public String knownAs()
    {
        return this.knownAs;
    }

    /**
     * Gets email address
     * @return email address
     */
    public String emailAddress()
    {
        return this.emailAddress;
    }

    /**
     * Gets postcode
     * @return postcode
     */
    public String postcode()
    {
        return this.postcode;
    }

    /**
     * Gets date of birth
     * @return date of birth
     */
    public Date dob()
    {
        return this.dob;
    }

    /**
     * Gets profile photo
     * @return profile photo
     */
    public Bitmap profilePhoto()
    {
        return this.profilePhoto;
    }
}