package uk.ac.tees.com2060.oreo;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * BrowseListingsFragment.java
 * <p>
 * The Fragment class that handles the Browse Listings page
 */
public class BrowseListingsFragment extends Fragment {
    BrowseListingsListener mCallback;

    CollapsingToolbarLayout toolbar;
    ListView listView;
    ArrayList<Listing> listings;
    ArrayList<Listing> filteredListings;
    ProgressBar progressBar;
    TextView radiusText;
    TextView sizeText;
    SeekBar seekerRadius;
    SeekBar seekerSize;

    int range = 20;
    int size = 2;


    public BrowseListingsFragment() {
    }

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface BrowseListingsListener {
        void browseListingsListener(Listing selectedListing);
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Browse Listings");

        final View view = inflater.inflate(R.layout.fragment_browse_listings, container, false);

        toolbar = view.findViewById(R.id.listing_toolbar_layout);
        toolbar.setVisibility(View.GONE);

        listView = view.findViewById(R.id.listView_browse_listings);

        listings = new ArrayList<>();
        filteredListings = new ArrayList<>();

        progressBar = view.findViewById(R.id.progressBar_browse_listings);

        radiusText = view.findViewById(R.id.radius_text);
        sizeText = view.findViewById(R.id.size_text);

        seekerRadius = view.findViewById(R.id.range_seeker);
        seekerRadius.setProgress(range);
        seekerRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                range = progress + 2;
                radiusText.setText(String.format(Locale.ENGLISH, "%d Miles", range));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                doFetchListings(range);
                radiusText.setText(String.format(Locale.ENGLISH, "%d Miles", range));
            }
        });

        seekerSize = view.findViewById(R.id.size_seeker);
        seekerSize.setProgress(size);
        seekerSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                size = progress;
                switch (progress) {
                    case 0:
                        sizeText.setText(R.string.package_size_0);
                        break;
                    case 1:
                        sizeText.setText(R.string.package_size_1);
                        break;
                    case 2:
                        sizeText.setText(R.string.package_size_2);
                        break;
                    case 3:
                        sizeText.setText(R.string.package_size_3);
                        break;
                    case 4:
                        sizeText.setText(R.string.package_size_4);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                filteredListings = Utils.FilterList(listings, size);

                listView.setAdapter(new ListingAdapter(getContext(), filteredListings));

                listView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


        doFetchListings(10);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Listing selectedListing = (listings).get(position);

                callbackToActivity(selectedListing);
            }

        });

        setHasOptionsMenu(true);

        return view;
    }

    /**
     * Callback to the Activity
     */
    public void callbackToActivity(Listing selectedListing) {
        mCallback = (BrowseListingsListener)getActivity();
        mCallback.browseListingsListener(selectedListing);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.menu_listing, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        item.setVisible(false);
        toolbar.setVisibility(View.VISIBLE);
        return super.onOptionsItemSelected(item);
    }

    private void doFetchListings(int radius) {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        ApiCall api = new ApiCall("listing/list/radius/" + radius, getContext());
        api.addResponseListener(new ResponseListener() {
            @Override
            public void responseReceived(ApiResponse response) {
                if (response.success()) {

                    listings = Listing.getListings(response.getBodyArray());

                    filteredListings = Utils.FilterList(listings, size);

                    listView.setAdapter(new ListingAdapter(getContext(), filteredListings));

                    listView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                }
            }
        });
        api.sendRequest();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Listing selectedListing = (listings).get(position);

                callbackToActivity(selectedListing);
            }

        });
    }
}
