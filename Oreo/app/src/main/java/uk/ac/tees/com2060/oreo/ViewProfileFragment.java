package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

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
    int reputation = 0;
    boolean ownProfile;

    CircleImageView image;
    TextView rep;
    TextView title;
    TextView location;
    TextView alias;
    ListView ratingsList;

    ArrayList<Rating> ratings;

    TextView noRatings;

    ConstraintLayout ratingsLayout;

    ProgressBar progress;


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


        alias = view.findViewById(R.id.textView_profile_alias);
        title = view.findViewById(R.id.textView_profile_title);
        image = view.findViewById(R.id.imageView_profile_profile_picture);
        rep = view.findViewById(R.id.textView_profile_rep);
        location = view.findViewById(R.id.textView_profile_location);
        progress = view.findViewById(R.id.progressBar_profile);
        noRatings = view.findViewById(R.id.textView_no_rating);
        ratingsLayout = view.findViewById(R.id.profile_constraint_hidden);
        ratingsList = view.findViewById(R.id.ListView_profile_ratings);

        Bundle args = this.getArguments();
        if (args != null) {

            profileID = args.getInt("userid");

            ownProfile = profileID == User.getUser().id();

        } else {
            profileID = User.getUser().id();
            ownProfile = true;
        }

        ApiCall getUserData = new ApiCall("user/" + profileID, this.getContext());
        getUserData.addResponseListener(new ResponseListener() {

            @Override
            public void responseReceived(ApiResponse response) {
                if (response.success()) {
                    try {
                        alias.setText(response.getBody().getString("known_as"));
                        getActivity().setTitle(response.getBody().getString("known_as"));
                        location.setText(response.getBody().getString("city"));

                        rep.setText(String.format("%s ★", String.valueOf(response.getBody().getInt("reputation"))));
                        if(ownProfile){User.getUser().setRep(response.getBody().getInt("reputation"));}

                        reputation = response.getBody().getInt("reputation");

                        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);

                        if (response.getBody().getJSONArray("ratings") != null)
                        {
                            ratings = Rating.getRatings(response.getBody().getJSONArray("ratings"));
                            ratingsList.setAdapter(new RatingsAdapter(getContext(), ratings));

                            ratingsLayout.startAnimation(slideUp);
                            ratingsLayout.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            noRatings.startAnimation(slideUp);
                            noRatings.setVisibility(View.VISIBLE);
                        }

                        alias.setVisibility(View.VISIBLE);
                        title.setVisibility(View.VISIBLE);
                        rep.setVisibility(View.VISIBLE);
                        location.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(view.getContext(), "Server Error", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        getUserData.sendRequest();

        if (ownProfile) {
            getActivity().setTitle("Your Profile!");
            image.setImageBitmap(User.getUser().profilePhoto());

            title.setText("Welcome back!");

            setHasOptionsMenu(true);
        } else {
            Picasso.get().load("https://getshipr.com/api/user/" + profileID + "/photo").placeholder(R.drawable.default_profile_photo).into(image, new Callback() {
                @Override
                public void onSuccess() {
                    progress.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    progress.setVisibility(View.INVISIBLE);
                }
            });


            if (profileID == 21 || profileID == 23) {
                title.setText("Shipr Developer");
            } else if (reputation < 0) {
                title.setText("BANNED");
            } else if (reputation < 100) {
                title.setText("Fresh Shipr");
            } else if (reputation < 250) {
                title.setText("Novice Shipr");
            } else if (reputation < 500) {
                title.setText("Adept Shipr");
            } else if (reputation < 1000) {
                title.setText("Experienced Shipr");
            } else if (reputation < 2000) {
                title.setText("Trusted Shipr");
            } else if (reputation < 10000) {
                title.setText("Trusted Shipr");
            }
        }

        setHasOptionsMenu(false);
        return view;
    }

    /**
     * Callback to the Activity
     */

    public void callbackToActivity() {
        mCallback.viewProfileListener();
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
                    + " must implement listener");
        }
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
