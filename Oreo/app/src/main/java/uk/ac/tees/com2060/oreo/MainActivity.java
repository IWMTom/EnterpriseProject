package uk.ac.tees.com2060.oreo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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

import java.text.DecimalFormat;
import java.util.Stack;

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
        BidConfirmFragment.DeleteBidListener,
        MyJobsFragment.MyJobsFragmentListener,
        MyShipmentsFragment.MyShipmentsFragmentListener,
        RatingsAdapter.RatingsAdapterListener,
        ContractViewFragment.ContractViewListener

{

    DashboardFragment dashboardFragment = new DashboardFragment();
    ListItemFragment listItemFragment = new ListItemFragment();
    ListingDetailFragment listingDetailFragment = new ListingDetailFragment();
    NewBidFragment newBidFragment = new NewBidFragment();
    Stack<ViewProfileFragment> viewProfileFragment = new Stack<>();
    int stackSize = 0;
    EditProfileFragment editProfileFragment = new EditProfileFragment();
    PastListingsFragment pastListingsFragment = new PastListingsFragment();
    BrowseListingsFragment browseListingsFragment = new BrowseListingsFragment();
    BidConfirmFragment bidConfirmFragment = new BidConfirmFragment();
    MyShipmentsFragment myShipmentsFragment = new MyShipmentsFragment();
    MyJobsFragment myJobsFragment = new MyJobsFragment();
    ContractViewFragment contractViewFragment = new ContractViewFragment();

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewProfileFragment.push(new ViewProfileFragment());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.listing_toolbar);
        setSupportActionBar(toolbar);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_IDLE) {
                    TextView header_rep =
                            navigationView.getHeaderView(0)
                                    .findViewById((R.id.textView_nav_header_rep));
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    header_rep.setText(formatter.format(User.getUser().getRep()) + " ★");
                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        textView_nav_header_rep.setText(formatter.format((User.getUser().getRep())) + " ★");

        TextView textView_nav_header_name = navigationView.getHeaderView(0)
                .findViewById(R.id.textView_nav_header_name);
        textView_nav_header_name.setText(User.getUser().fullName());

        imageView_nav_header_profile_photo.setImageBitmap(User.getUser().profilePhoto());


        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1) != dashboardFragment) {
                        dashboardFragment = new DashboardFragment();
                    }
                }

                if (stackSize > getSupportFragmentManager().getFragments().size()) {
                    if (viewProfileFragment.size() > 1) {
                        viewProfileFragment.pop();
                    }
                }

                if (getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1) == dashboardFragment) {
                    navigationView.setCheckedItem(R.id.nav_dashboard);
                } else if (getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1) == listItemFragment) {
                    navigationView.setCheckedItem(R.id.nav_list);
                } else if (getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1) == pastListingsFragment) {
                    navigationView.setCheckedItem(R.id.nav_my_listings);
                } else if (getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1) == myShipmentsFragment) {
                    navigationView.setCheckedItem(R.id.nav_my_shipments);
                } else if (getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1) == browseListingsFragment) {
                    navigationView.setCheckedItem(R.id.nav_browse_listings);
                } else if (getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1) == myJobsFragment) {
                    navigationView.setCheckedItem(R.id.nav_my_jobs);
                } else if (getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1) == editProfileFragment) {
                    navigationView.setCheckedItem(R.id.nav_settings);
                }

                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    navigationView.setCheckedItem(R.id.nav_dashboard);
                }

                stackSize = getSupportFragmentManager().getBackStackEntryCount();


            }
        });

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
        navigationView.getHeaderView(0)
                .findViewById((R.id.textView_nav_header_rep));

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
            case R.id.nav_my_shipments:
                showMyShipments();
                break;
            case R.id.nav_my_jobs:
                showMyJobs();
                break;
            case R.id.nav_my_listings:
                showMyListings();
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

    private void showMyShipments() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, myShipmentsFragment).addToBackStack(null)
                .commit();
    }

    private void showMyJobs() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, myJobsFragment).addToBackStack(null)
                .commit();
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

    private void showMyListings() {
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
    public void dashboardListener(int i) {
        switch (i) {
            case 1:
                showMyListings();
                break;
            case 2:
                showMyShipments();
                break;
            case 3:
                showMyJobs();
                break;
            case 0:
                showProfile();
                break;
        }
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

                viewProfileFragment.peek().setArguments(b);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment_container, viewProfileFragment.peek())
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
        viewProfileFragment.peek().setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, viewProfileFragment.peek())
                .addToBackStack(null).commit();
    }

    @Override
    public void bidsAdapterListener(Bundle b) {

        if (b.get("bid") != null) {
            showConfirmBid(b);
        } else if (b.get("userid") != null) {
            openUserProfile(b.getInt("userid"));
        }
    }

    @Override
    public void bidConfirmListener() {
        showMyShipments();
    }

    @Override
    public void deleteListingListener() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void deleteBidListener() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void myJobsFragmentListener(Contract contract) {
        Bundle arguments = new Bundle();
        arguments.putSerializable("contract", contract);
        contractViewFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, contractViewFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void myShipmentsFragmentListener(Contract contract) {
        Bundle arguments = new Bundle();
        arguments.putSerializable("contract", contract);
        contractViewFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, contractViewFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void ratingsAdapterListener(Bundle b) {
        viewProfileFragment.push(new ViewProfileFragment());
        Log.d("profile stack size", String.valueOf(viewProfileFragment.size()));
        openUserProfile(b.getInt("userid"));
    }

    @Override
    public void ContractViewListener() {
        getSupportFragmentManager().popBackStack();
    }
}
