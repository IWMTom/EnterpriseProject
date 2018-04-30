package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

import static android.content.ContentValues.TAG;

/**
 * ViewProfileFragment.java
 * <p>
 * The Fragment class that handles the View Profile page
 */
public class ViewProfileFragment extends Fragment {
    ViewProfileListener mCallback;

    int profileID;
    boolean ownprofile;

    public ViewProfileFragment() {

    }


    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface ViewProfileListener {
        public void viewProfileListener();
    }

    /**
     * Handles the attachment of the Fragment to the Activity.
     * Throws an exception if the Activity doesn't implement the listener interface.
     *
     * @param activity calling activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (ViewProfileListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ViewProfileListener");
        }
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        final CircleImageView image = view.findViewById(R.id.imageView_profile_view_image);
        final TextView rep = view.findViewById(R.id.textView_rep);
        final TextView location = view.findViewById(R.id.textView_profile_location);
        final ProgressBar spinner = view.findViewById(R.id.progressBar_view_profile);
        Bundle args = this.getArguments();
        if (args != null) {

            profileID = args.getInt("userid");

            if (profileID == User.getUser().id()) {
                ownprofile = true;
            } else {
                ownprofile = false;
            }

        } else {
            profileID = User.getUser().id();
            ownprofile = true;
        }

        if (ownprofile) {
            getActivity().setTitle("Your Profile");
            image.setImageBitmap(User.getUser().profilePhoto());

            ApiCall getUserData = new ApiCall("user/" + profileID, this.getContext());
            getUserData.addResponseListener(new ResponseListener() {

                @Override
                public void responseReceived(ApiResponse response) {
                    if (response.success()) {
                        try {
                            location.setText(response.getBody().getString("city"));
                            rep.setText(String.valueOf(response.getBody().getInt("reputation")));
                            rep.setVisibility(View.VISIBLE);
                            location.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast toast = Toast.makeText(view.getContext(), "This should not happen", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });

            getUserData.sendRequest();

            setHasOptionsMenu(true);

        } else {

            ApiCall getUserData = new ApiCall("user/" + profileID, this.getContext());
            getUserData.addResponseListener(new ResponseListener() {

                @Override
                public void responseReceived(ApiResponse response) {
                    if (response.success()) {
                        try {
                            getActivity().setTitle(response.getBody().getString("known_as"));
                            rep.setText(String.valueOf(response.getBody().getInt("reputation")));
                            location.setText(response.getBody().getString("city"));
                            rep.setVisibility(View.VISIBLE);
                            location.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.GONE);
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    } else {
                        Toast toast = Toast.makeText(view.getContext(), "This should not happen", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });

            getUserData.sendRequest();

            Picasso.get().load("https://getshipr.com/api/user/" + profileID + "/photo").placeholder(R.drawable.default_profile_photo).into(image);

            setHasOptionsMenu(false);
        }
        return view;
    }

    /**
     * Callback to the Activity
     */
    public void callbackToActivity() {
        mCallback.viewProfileListener();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_edit, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        callbackToActivity();
        return super.onOptionsItemSelected(item);
    }
}
