package uk.ac.tees.com2060.oreo;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * DashboardFragment.java
 * <p>
 * The Fragment class that handles the Dashboard page
 */
public class ListingDetailFragment extends Fragment{
    ListingDetailListener mCallback;
    DeleteListingListener deleteCallback;

    public ListingDetailFragment() {
    }

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface ListingDetailListener {
        void listingDetailListener(Listing selectedListing);
    }

    public interface DeleteListingListener {
        void deleteListingListener();
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();

        final Listing selectedListing = (Listing) arguments.getSerializable("selectedListing");
        final View view = inflater.inflate(R.layout.fragment_listing_detail, container, false);

        if (selectedListing != null) {
            getActivity().setTitle(selectedListing.itemDescription());

            FloatingActionButton fab = view.findViewById(R.id.fab_listing_detail);

            if (selectedListing.user_id() != User.getUser().id()) {
                fab.setImageResource(R.drawable.ic_add_black_24dp);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callbackToActivity(selectedListing);
                    }
                });
            } else {
                fab.setImageResource(R.drawable.ic_delete);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ApiCall apiCall= new ApiCall("listing/"+ String.valueOf(selectedListing.id() + "/delete"), getContext());
                        apiCall.addResponseListener(new ResponseListener() {
                            @Override
                            public void responseReceived(ApiResponse response) {
                                    if(response.success()){
                                        Toast toast = Toast.makeText(getContext(), "Listing has been deleted!", Toast.LENGTH_SHORT);
                                        toast.show();

                                        deleteCallback = (DeleteListingListener) getActivity();
                                        deleteCallback.deleteListingListener();
                                    }else{
                                        Toast toast = Toast.makeText(getContext(), "Server Error!", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                            }
                        });

                        apiCall.sendRequest();


                    }
                });
            }

            TextView listing_collection = view.findViewById(R.id.textView_confirm_collection);
            listing_collection.setText(selectedListing.collectionCity());

            TextView listing_delivery = view.findViewById(R.id.textView_confirm_delivery);
            listing_delivery.setText(selectedListing.deliveryCity());

            TextView listing_distance = view.findViewById(R.id.textView_listing_distance);
            listing_distance.setText(String.format(Locale.ENGLISH, "%s miles", selectedListing.distance()));

            TextView listing_size = view.findViewById(R.id.textView_listing_size);
            listing_size.setText(String.format(Locale.ENGLISH, "%s%s", selectedListing.itemSize().substring(0, 1).toUpperCase(), selectedListing.itemSize().substring(1)));

            TextView listing_important_details = view.findViewById(R.id.textView_confirm_bidder_comment);
            listing_important_details.setText((selectedListing.importantDetails().equals("null") ? "" : selectedListing.importantDetails()));

            final ProgressBar progress = view.findViewById(R.id.progressBar_listing_detail);
            final ListView listView = view.findViewById(R.id.listing_ratings);
            final ArrayList<Bid> al = new ArrayList<>();

            ApiCall api = new ApiCall("listing/" + selectedListing.id() + "/bids", getContext());
            api.addResponseListener(new ResponseListener() {
                @Override
                public void responseReceived(ApiResponse response) {
                    if (response.success()) {
                        for (int i = 0; i < response.getBodyArray().length(); i++) {
                            try {
                                JSONObject object = response.getBodyArray().getJSONObject(i);

                                al.add(new Bid(
                                        object.getInt("id"),
                                        object.getInt("user_id"),
                                        object.getString("username"),
                                        object.getString("message"),
                                        Double.parseDouble(object.getString("amount"))
                                ));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        listView.setAdapter(new BidsAdapter(getContext(), al, selectedListing));

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        });
                        progress.setVisibility(View.INVISIBLE);
                    }
                }
            });
            api.sendRequest();

            setHasOptionsMenu(true);
            return view;
        }
        setHasOptionsMenu(true);
        return view;
    }


    public void callbackToActivity(Listing listing) {
        mCallback = (ListingDetailListener) getActivity();
        mCallback.listingDetailListener(listing);
    }
}
