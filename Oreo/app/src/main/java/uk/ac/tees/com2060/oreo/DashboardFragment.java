package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * DashboardFragment.java
 * <p>
 * The Fragment class that handles the Dashboard page
 */
public class DashboardFragment extends Fragment {
    DashboardListener mCallback;

    public DashboardFragment() {
    }

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface DashboardListener {
        void dashboardListener(int i);
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
            mCallback = (DashboardListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DashboardListener");
        }
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        getActivity().setTitle(R.string.fragment_dashboard_title);

        TextView name = view.findViewById(R.id.textView_dash_name);
        name.setText(User.getUser().fullName());
        TextView rep = view.findViewById(R.id.textView_dash_rep);

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        rep.setText(formatter.format((User.getUser().getRep()))+ " ★");

        CircleImageView profile = view.findViewById(R.id.imageView_dash_profile_picture);
        profile.setImageBitmap(User.getUser().profilePhoto());

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback = (DashboardListener) getActivity();
                mCallback.dashboardListener(0);
            }
        });

        final Button listings = view.findViewById(R.id.button_dash_my_listings);
        final Button shipments = view.findViewById(R.id.button_dash_my_shipments);
        final Button jobs = view.findViewById(R.id.button_dash_my_jobs);

        final ApiCall getData = new ApiCall("user/dashboard",getContext());
        getData.addResponseListener(new ResponseListener() {
            @Override
            public void responseReceived(ApiResponse response) {
                if(response.success()){
                    try
                    {
                        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
                        if(response.getBody().getInt("active_listings") > 0){
                            listings.setText("View your " + response.getBody().getInt("active_listings") + " listings!");
                            listings.startAnimation(slideUp);
                            listings.setVisibility(View.VISIBLE);

                            listings.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mCallback = (DashboardListener) getActivity();
                                    mCallback.dashboardListener(1);
                                }
                            });

                        }

                        if(response.getBody().getInt("inactive_listings") > 0){
                            shipments.setText("View your " + response.getBody().getInt("inactive_listings") + " shipments!");
                            shipments.startAnimation(slideUp);
                            shipments.setVisibility(View.VISIBLE);

                            shipments.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mCallback = (DashboardListener) getActivity();
                                    mCallback.dashboardListener(2);
                                }
                            });

                        }

                        if(response.getBody().getInt("remaining_jobs") > 0){
                            jobs.setText("View your " + response.getBody().getInt("remaining_jobs") + " jobs!");
                            jobs.startAnimation(slideUp);
                            jobs.setVisibility(View.VISIBLE);

                            jobs.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mCallback = (DashboardListener) getActivity();
                                    mCallback.dashboardListener(3);
                                }
                            });

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        getData.sendRequest();

        setHasOptionsMenu(true);
        return view;
    }

    /**
     * Callback to the Activity
     */
    public void callbackToActivity(int i) {

        mCallback.dashboardListener(i);
    }
}
