package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * DashboardFragment.java
 *
 * The Fragment class that handles the Dashboard page
 */
public class ListingDetailFragment extends Fragment
{
    ListingDetailListener mCallback;

    public ListingDetailFragment() {}

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface ListingDetailListener
    {
        public void listingDetailListener(Listing selectedListing);
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
            mCallback = (ListingDetailListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement ListingDetailListener");
        }
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle arguments = getArguments();
        final Listing selectedListing = (Listing) arguments.getSerializable("selectedListing");

        getActivity().setTitle(selectedListing.itemDescription());

        final View view = inflater.inflate(R.layout.fragment_listing_detail, container, false);
        setHasOptionsMenu(true);

        FloatingActionButton fab = view.findViewById(R.id.fab_listing_detail);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                callbackToActivity(selectedListing);
            }
        });

        TextView listing_collection = view.findViewById(R.id.textView_listing_collection);
        listing_collection.setText(selectedListing.collectionCity());

        TextView listing_delivery = view.findViewById(R.id.textView_listing_delivery);
        listing_delivery.setText(selectedListing.deliveryCity());

        TextView listing_distance = view.findViewById(R.id.textView_listing_distance);
        listing_distance.setText(selectedListing.distance() + " miles");

        TextView listing_size = view.findViewById(R.id.textView_listing_size);
        listing_size.setText(selectedListing.itemSize().substring(0, 1).toUpperCase() + selectedListing.itemSize().substring(1));

        TextView listing_important_details = view.findViewById(R.id.textView_listing_important_details);
        listing_important_details.setText((selectedListing.importantDetails().equals("null") ? "" : selectedListing.importantDetails()));

        final ProgressBar progress = view.findViewById(R.id.progressBar_listing_detail);
        final ListView listView = view.findViewById(R.id.listing_ratings);
        final ArrayList<Bid> al = new ArrayList<>();

        ApiCall api = new ApiCall("listing/" + selectedListing.id() + "/bids", getContext());
        api.addResponseListener(new ResponseListener()
        {
            @Override
            public void responseReceived(ApiResponse response)
            {
                if (response.success())
                {
                    for(int i = 0; i < response.getBodyArray().length(); i++)
                    {
                        try
                        {
                            JSONObject object = response.getBodyArray().getJSONObject(i);

                            al.add(new Bid(
                                    object.getInt("id"),
                                    object.getInt("user_id"),
                                    object.getString("username"),
                                    object.getString("message"),
                                    Double.parseDouble(object.getString("amount"))
                            ));

                        } catch (JSONException e) { e.printStackTrace(); }
                    }

                    listView.setAdapter(new BidsAdapter(getContext(), al));

                    progress.setVisibility(View.INVISIBLE);
                }
            }
        });
        api.sendRequest();

        return view;
    }

    /**
     * Callback to the Activity
     */
    public void callbackToActivity(Listing selectedListing)
    {
        mCallback.listingDetailListener(selectedListing);
    }
}
