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
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * BrowseListingsFragment.java
 *
 * The Fragment class that handles the Browse Listings page
 */
public class BrowseListingsFragment extends Fragment
{
    BrowseListingsListener mCallback;

    public BrowseListingsFragment() {}

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface BrowseListingsListener
    {
        public void browseListingsListener(Listing selectedListing);
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
            mCallback = (BrowseListingsListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement BrowseListingsListener");
        }
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("Browse Listings");

        final View view = inflater.inflate(R.layout.fragment_browse_listings, container, false);
        setHasOptionsMenu(true);

        final ListView listView           = view.findViewById(R.id.listView_browse_listings);
        final ArrayList[] listings        = new ArrayList[1];
        final ProgressBar progress        = view.findViewById(R.id.progressBar_browse_listings);

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
                    progress.setVisibility(View.INVISIBLE);
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
        mCallback.browseListingsListener(selectedListing);
    }
}