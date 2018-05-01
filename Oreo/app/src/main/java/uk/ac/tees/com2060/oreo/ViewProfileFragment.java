package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * ViewProfileFragment.java
 * <p>
 * The Fragment class that handles the View Profile page
 */
public class ViewProfileFragment extends Fragment {
    ViewProfileListener mCallback;

    int profileID;
    boolean ownProfile;

    public ViewProfileFragment() {
    }

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface ViewProfileListener {
        void viewProfileListener();
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        mCallback = (ViewProfileListener) getActivity();

        final CircleImageView image = view.findViewById(R.id.imageView_profile_view_image);
        final TextView rep = view.findViewById(R.id.textView_rep);
        final TextView location = view.findViewById(R.id.textView_profile_location);
        final ProgressBar spinner = view.findViewById(R.id.progressBar_view_profile);
        final ProgressBar spinner_profile = view.findViewById(R.id.progressBar_view_profile_picture);

        Bundle args = this.getArguments();
        if (args != null) {

            profileID = args.getInt("userid");

            ownProfile = profileID == User.getUser().id();

        } else {
            profileID = User.getUser().id();
            ownProfile = true;
        }

        if (ownProfile) {
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
            spinner_profile.setVisibility(View.VISIBLE);
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

            Picasso.get().load("https://getshipr.com/api/user/" + profileID + "/photo").placeholder(R.drawable.default_profile_photo).into(image, new Callback() {
                @Override
                public void onSuccess() {
                    spinner_profile.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    spinner_profile.setVisibility(View.INVISIBLE);
                }
            });

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
