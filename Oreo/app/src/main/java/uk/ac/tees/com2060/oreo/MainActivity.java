package uk.ac.tees.com2060.oreo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;

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
                    DashboardFragment.DashboardListener
{

    DashboardFragment dashboardFragment = new DashboardFragment();

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

        ImageView imageView_nav_header_profile_photo =
                (ImageView) navigationView.getHeaderView(0)
                        .findViewById(R.id.imageView_nav_header_profile_photo);
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

            getFragmentManager().beginTransaction()
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
                break;
            case R.id.nav_list:
                break;
            case R.id.nav_past_listings:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_logout:
                doLogout();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
