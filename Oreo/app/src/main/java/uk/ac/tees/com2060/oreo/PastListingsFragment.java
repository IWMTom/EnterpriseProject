package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * PastListingsFragment.java
 * <p>
 * The Fragment class that handles the Past Listings page
 */
public class PastListingsFragment extends Fragment {
    PastListingsListener mCallback;


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
            mCallback = (PastListingsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement listener");
        }
    }

    public PastListingsFragment() {
    }

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface PastListingsListener {
        void pastListingsListener(Listing selectedListing);
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("My Listings");

        final View view = inflater.inflate(R.layout.fragment_past_listings, container, false);
        setHasOptionsMenu(true);

        final ProgressBar progressBar = view.findViewById(R.id.progressBar_past_listings);
        final ListView listView = view.findViewById(R.id.listView_past_listings);
        final ArrayList[] listings = new ArrayList[1];


        ApiCall api = new ApiCall("listing/list/user", getContext());
        api.addResponseListener(new ResponseListener() {
            @Override
            public void responseReceived(ApiResponse response) {
                if (response.success()) {
                    listings[0] = Listing.getListings(response.getBodyArray());
                    listView.setAdapter(new ListingAdapter(getContext(), listings[0]));
                    progressBar.setVisibility(View.INVISIBLE);

                    TextView nothingToSee = getView().findViewById(R.id.my_listings_no_results);
                    if(listings[0].size() == 0){
                        listView.setVisibility(View.INVISIBLE);
                        nothingToSee.setVisibility(View.VISIBLE);
                    }else{
                        nothingToSee.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        api.sendRequest();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Listing selectedListing = ((ArrayList<Listing>) listings[0]).get(position);

                callbackToActivity(selectedListing);
            }

        });

        return view;
    }

    /**
     * Callback to the Activity
     */
    public void callbackToActivity(Listing selectedListing) {
        mCallback = (PastListingsListener) getActivity();
        mCallback.pastListingsListener(selectedListing);
    }
}
