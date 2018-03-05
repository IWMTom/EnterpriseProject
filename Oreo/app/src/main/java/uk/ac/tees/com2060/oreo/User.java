package uk.ac.tees.com2060.oreo;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User implements Serializable
{
    private static User userInstance = null;

    private String fullName, knownAs, emailAddress, postcode;
    private Date dob;
    private Bitmap profilePhoto;

    private User() {}

    public void init(String fullName, String knownAs, String emailAddress, String postcode, String dob, String profilePhoto) throws ParseException
    {
        this.fullName = fullName;
        this.knownAs = knownAs;
        this.emailAddress = emailAddress;
        this.postcode = postcode;
        this.dob = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
        this.profilePhoto = Utils.getImageFromString(profilePhoto);
    }

    public static User getUser()
    {
        if (userInstance == null)
        {
            userInstance = new User();
        }

        return userInstance;
    }

    public String fullName()
    {
        return this.fullName;
    }

    public String knownAs()
    {
        return this.knownAs;
    }

    public String emailAddress()
    {
        return this.emailAddress;
    }

    public String postcode()
    {
        return this.postcode;
    }

    public Date dob()
    {
        return this.dob;
    }

    public Bitmap profilePhoto()
    {
        return this.profilePhoto;
    }
}