package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * DashboardFragment.java
 *
 * The Fragment class that handles the Dashboard page
 */
public class DashboardFragment extends Fragment
{
    DashboardListener mCallback;

    public DashboardFragment() {}

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface DashboardListener
    {
        public void dashboardListener(Listing selectedListing);
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
            mCallback = (DashboardListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement DashboardListener");
        }
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle(R.string.fragment_dashboard_title);

        final View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setHasOptionsMenu(true);

        ImageView imageView_test          = view.findViewById(R.id.dashboard_imageView);
        TextView textView_test            = view.findViewById(R.id.dashboard_textView);
        final ListView listView           = view.findViewById(R.id.dashboard_listView);
        final ArrayList[] listings        = new ArrayList[1];

        textView_test.setText(User.getUser().fullName());
        imageView_test.setImageBitmap(User.getUser().profilePhoto());

        ApiCall api = new ApiCall("listing/list", getContext());
        api.addResponseListener(new ResponseListener()
        {
            @Override
            public void responseReceived(ApiResponse response)
            {
                if (response.success())
                {
                    listings[0] = Listing.getListings(response.getBodyArray());
                    listView.setAdapter(new ListingAdapter(getContext(), listings[0]));
                }
            }
        });
        api.sendRequest();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Listing selectedListing = ((ArrayList<Listing>) listings[0]).get(position);

                callbackToActivity(selectedListing);
            }

        });

        return view;
    }

    /**
     * Callback to the Activity
     */
    public void callbackToActivity(Listing selectedListing)
    {
        mCallback.dashboardListener(selectedListing);
    }
}
