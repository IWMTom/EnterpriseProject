package uk.ac.tees.com2060.oreo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * MainActivity.java
 * <p>
 * The Activity class that handles the main body of the application,
 * including navigation drawer, and fragment-based UI.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DashboardFragment.DashboardListener,
        ListItemFragment.ListItemListener,
        ListingDetailFragment.ListingDetailListener,
        ListingDetailFragment.DeleteListingListener,
        NewBidFragment.NewBidListener,
        EditProfileFragment.EditProfileListener,
        ViewProfileFragment.ViewProfileListener,
        PastListingsFragment.PastListingsListener,
        BrowseListingsFragment.BrowseListingsListener,
        BidsAdapter.BidsAdapterListener,
        BidConfirmFragment.BidConfirmListener,
        BidConfirmFragment.DeleteBidListener

{

    DashboardFragment dashboardFragment = new DashboardFragment();
    ListItemFragment listItemFragment = new ListItemFragment();
    ListingDetailFragment listingDetailFragment = new ListingDetailFragment();
    NewBidFragment newBidFragment = new NewBidFragment();
    ViewProfileFragment viewProfileFragment = new ViewProfileFragment();
    EditProfileFragment editProfileFragment = new EditProfileFragment();
    PastListingsFragment pastListingsFragment = new PastListingsFragment();
    BrowseListingsFragment browseListingsFragment = new BrowseListingsFragment();
    BidConfirmFragment bidConfirmFragment = new BidConfirmFragment();

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** START BOILERPLATE */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.listing_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /** END BOILERPLATE */

        User.getUser().updatePushToken(this);

        if (getIntent().getIntExtra("notification_bid", -1) != -1)
            showListingDetail(getIntent().getIntExtra("notification_bid", -1));

        ImageView imageView_nav_header_profile_photo =
                navigationView.getHeaderView(0)
                        .findViewById(R.id.imageView_nav_header_profile_photo);

        imageView_nav_header_profile_photo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showProfile();
            }
        });

        TextView textView_nav_header_rep =
                navigationView.getHeaderView(0)
                        .findViewById((R.id.textView_nav_header_rep));
        textView_nav_header_rep.setText(String.valueOf(User.getUser().getRep())+" ★");

        TextView textView_nav_header_name = navigationView.getHeaderView(0)
                .findViewById(R.id.textView_nav_header_name);
        textView_nav_header_name.setText(User.getUser().fullName());

        imageView_nav_header_profile_photo.setImageBitmap(User.getUser().profilePhoto());

        // Displays dashboard fragment by default
        if (findViewById(R.id.main_fragment_container) != null) {
            if (savedInstanceState != null)
                return;

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_fragment_container, dashboardFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Coordinates which function will be called when a navigation drawer item is selected
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView textView_nav_header_rep =
                navigationView.getHeaderView(0)
                        .findViewById((R.id.textView_nav_header_rep));
        textView_nav_header_rep.setText(String.valueOf(User.getUser().getRep())+" ★");

        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                showDashboard();
                break;
            case R.id.nav_list:
                showListItem();
                break;
            case R.id.nav_browse_listings:
                showBrowseListings();
                break;
            case R.id.nav_my_listings:
                showPastListings();
                break;
            case R.id.nav_settings:
                showEditProfile();
                break;
            case R.id.nav_logout:
                doLogout();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showProfile() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.START);

        openUserProfile(User.getUser().id());
    }

    private void showDashboard() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, dashboardFragment).addToBackStack(null)
                .commit();
    }

    private void showListItem() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, listItemFragment).addToBackStack(null)
                .commit();

    }

    private void showBrowseListings() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, browseListingsFragment).addToBackStack(null)
                .commit();
    }

    private void showPastListings() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, pastListingsFragment).addToBackStack(null)
                .commit();
    }

    private void showEditProfile() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, editProfileFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showConfirmBid(Bundle b) {
        bidConfirmFragment.setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, bidConfirmFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Logs out the user by removing the access token in shared preferences
     */
    private void doLogout() {
        Utils.removeUserAccessToken(this);

        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void dashboardListener() {

    }

    private void showListingDetail(final int id) {
        ApiCall api = new ApiCall("listing/" + id, this);
        api.addResponseListener(new ResponseListener() {
            @Override
            public void responseReceived(ApiResponse response) {
                if (response.success()) {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable("selectedListing", Listing.getListing(response.getBodyArray()));
                    listingDetailFragment.setArguments(arguments);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment_container, listingDetailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        api.sendRequest();
    }

    @Override
    public void listItemListener(int id) {
        showListingDetail(id);
    }

    @Override
    public void listingDetailListener(Listing selectedListing) {
        Bundle arguments = new Bundle();
        arguments.putSerializable("selectedListing", selectedListing);
        newBidFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, newBidFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void newBidListener() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().remove(newBidFragment).commitAllowingStateLoss();
    }

    @Override
    public void editProfileListener(Bundle b) {

        if (b != null) {
            if (b.getBoolean("secret")) {

                Toast toast = Toast.makeText(this, "Oooh, what's this?", Toast.LENGTH_SHORT);
                toast.show();

                viewProfileFragment.setArguments(b);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment_container, viewProfileFragment)
                        .addToBackStack(null).commit();

            } else {

            }

        }
    }

    @Override
    public void viewProfileListener() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, editProfileFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void pastListingsListener(Listing selectedListing) {
        Bundle arguments = new Bundle();
        arguments.putSerializable("selectedListing", selectedListing);
        listingDetailFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, listingDetailFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void browseListingsListener(Listing selectedListing) {
        Bundle arguments = new Bundle();
        arguments.putSerializable("selectedListing", selectedListing);
        listingDetailFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, listingDetailFragment)
                .addToBackStack(null).commit();
    }

    public void openUserProfile(int userid) {
        Bundle b = new Bundle();
        b.putInt("userid", userid);
        viewProfileFragment.setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, viewProfileFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void bidsAdapterListener(Bundle b) {

        if(b.get("bid")!=null){
            showConfirmBid(b);
        }else if(b.get("userid")!=null){
            openUserProfile(b.getInt("userid"));
        }
    }

    @Override
    public void bidConfirmListener(Bundle b) {

    }

    @Override
    public void deleteListingListener() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void deleteBidListener() {
        getSupportFragmentManager().popBackStack();
    }
}
