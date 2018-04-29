package uk.ac.tees.com2060.oreo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * MainActivity.java
 *
 * The Activity class that handles the main body of the application,
 * including navigation drawer, and fragment-based UI.
 */
public class MainActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
                    DashboardFragment.DashboardListener,
                    ListItemFragment.ListItemListener,
                    ListingDetailFragment.ListingDetailListener,
                    NewBidFragment.NewBidListener,
                    EditProfileFragment.EditProfileListener,
                    ViewProfileFragment.ViewProfileListener,
                    PastListingsFragment.PastListingsListener,
                    BrowseListingsFragment.BrowseListingsListener
{

    DashboardFragment dashboardFragment             = new DashboardFragment();
    ListItemFragment listItemFragment               = new ListItemFragment();
    ListingDetailFragment listingDetailFragment     = new ListingDetailFragment();
    NewBidFragment newBidFragment                   = new NewBidFragment();
    ViewProfileFragment viewProfileFragment         = new ViewProfileFragment();
    EditProfileFragment editProfileFragment         = new EditProfileFragment();
    PastListingsFragment pastListingsFragment       = new PastListingsFragment();
    BrowseListingsFragment browseListingsFragment   = new BrowseListingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /** START BOILERPLATE */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /** END BOILERPLATE */

        User.getUser().updatePushToken(this);

        if (getIntent().getIntExtra("notification_bid", -1) != -1)
            showListingDetail(getIntent().getIntExtra("notification_bid", -1));

        ImageView imageView_nav_header_profile_photo =
                (ImageView) navigationView.getHeaderView(0)
                        .findViewById(R.id.imageView_nav_header_profile_photo);

        imageView_nav_header_profile_photo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showProfile();
            }
        });;

        TextView textView_nav_header_name =
                (TextView) navigationView.getHeaderView(0)
                        .findViewById(R.id.textView_nav_header_name);

        imageView_nav_header_profile_photo.setImageBitmap(User.getUser().profilePhoto());
        textView_nav_header_name.setText(User.getUser().fullName());

        // Displays dashboard fragment by default
        if (findViewById(R.id.main_fragment_container) != null)
        {
            if (savedInstanceState != null)
                return;

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_fragment_container, dashboardFragment).commit();
        }
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public boolean onNavigationItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_dashboard:
                showDashboard();
                break;
            case R.id.nav_list:
                showListItem();
                break;
            case R.id.nav_browse_listings:
                showBrowseListings();
                break;
            case R.id.nav_past_listings:
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

    private void showProfile()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.LEFT);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, viewProfileFragment).commit();

    }

    private void showDashboard()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, dashboardFragment).commit();
    }

    private void showListItem()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, listItemFragment).commit();
    }

    private void showBrowseListings()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, browseListingsFragment).commit();
    }

    private void showPastListings()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, pastListingsFragment).commit();
    }

    private void showEditProfile(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, editProfileFragment).commit();
    }
    


    /**
     * Logs out the user by removing the access token in shared preferences
     */
    private void doLogout()
    {
        Utils.removeUserAccessToken(this);

        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void dashboardListener()
    {

    }

    private void showListingDetail(final int id)
    {
        ApiCall api = new ApiCall("listing/" + id, this);
        api.addResponseListener(new ResponseListener()
        {
            @Override
            public void responseReceived(ApiResponse response)
            {
                if (response.success())
                {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable("selectedListing", Listing.getListing(response.getBodyArray()));
                    listingDetailFragment.setArguments(arguments);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment_container, listingDetailFragment)
                            .addToBackStack(null).commit();
                }
            }
        });
        api.sendRequest();
    }

    @Override
    public void listItemListener(int id)
    {
        showListingDetail(id);
    }

    @Override
    public void listingDetailListener(Listing selectedListing)
    {
        Bundle arguments = new Bundle();
        arguments.putSerializable("selectedListing", selectedListing);
        newBidFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, newBidFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void newBidListener()
    {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().remove(newBidFragment).commitAllowingStateLoss();
    }

    @Override
    public void editProfileListener()
    {

    }

    @Override
    public void viewProfileListener()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, editProfileFragment)
        .addToBackStack(null).commit(); }

    @Override
    public void pastListingsListener(Listing selectedListing)
    {
        Bundle arguments = new Bundle();
        arguments.putSerializable("selectedListing", selectedListing);
        listingDetailFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, listingDetailFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void browseListingsListener(Listing selectedListing)
    {
        Bundle arguments = new Bundle();
        arguments.putSerializable("selectedListing", selectedListing);
        listingDetailFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, listingDetailFragment)
                .addToBackStack(null).commit();
    }
}
