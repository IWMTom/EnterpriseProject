package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * EditProfileFragment.java
 *
 * The Fragment class that handles the Edit Profile page
 */
public class EditProfileFragment extends Fragment
{
    EditProfileListener mCallback;

    public EditProfileFragment() {}

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface EditProfileListener
    {
        public void editProfileListener();
    }

    /**
     * Handles the attachment of the Fragment to the Activity.
     * Throws an exception if the Activity doesn't implement the listener interface.
     * @param activity calling activity
     */
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            mCallback = (EditProfileListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement EditProfileListener");
        }
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setHasOptionsMenu(true);

        getActivity().setTitle("Edit Your Profile");

        CircleImageView image = view.findViewById(R.id.imageView_profile_view_image2);
        image.setImageBitmap(User.getUser().profilePhoto());

        EditText alias = view.findViewById(R.id.editText_profile_known_as);
        alias.setText(User.getUser().knownAs());

        EditText name = view.findViewById(R.id.editText_profile_full_name);
        name.setText(User.getUser().fullName());

        EditText postcode = view.findViewById(R.id.editText_profile_postcode);
        postcode.setText(User.getUser().postcode());

        EditText email = view.findViewById(R.id.editText_profile_email);
        email.setText(User.getUser().emailAddress());

        EditText mobile = view.findViewById(R.id.editText_profile_mobile_number);
        //mobile.setText(); TODO:implement in user class

        TextView version = view.findViewById(R.id.textView_profile_version);
        version.setText(BuildConfig.VERSION_NAME);

        return view;
    }

    /**
     * Callback to the Activity
     */
    public void callbackToActivity()
    {
        mCallback.editProfileListener();
    }

}
