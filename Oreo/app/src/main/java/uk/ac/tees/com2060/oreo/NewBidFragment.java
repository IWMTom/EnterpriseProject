package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * DashboardFragment.java
 *
 * The Fragment class that handles the Dashboard page
 */
public class NewBidFragment extends Fragment
{
    NewBidListener mCallback;
    CurrencyEditText bid_amount;
    EditText bid_message;
    Listing selectedListing;

    public NewBidFragment() {}

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface NewBidListener
    {
        public void newBidListener();
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
            mCallback = (NewBidListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement NewBidListener");
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
        selectedListing = (Listing) arguments.getSerializable("selectedListing");

        getActivity().setTitle("New Bid");

        final View view = inflater.inflate(R.layout.fragment_new_bid, container, false);
        setHasOptionsMenu(true);

        TextView minimumBid = view.findViewById(R.id.textView_minimum_bid);
        TextView maximumBid = view.findViewById(R.id.textView_maximum_bid);
        TextView averageBid = view.findViewById(R.id.textView_average_bid);

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        minimumBid.setText(formatter.format(selectedListing.minBid()));
        maximumBid.setText(formatter.format(selectedListing.maxBid()));
        averageBid.setText(formatter.format(selectedListing.averageBid()));


        bid_amount = view.findViewById(R.id.editText_bid_amount);
        bid_message = view.findViewById(R.id.editText_bid_message);

        Button submit = view.findViewById(R.id.button_submit_bid);
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                submitBid(view);
            }
        });

        return view;
    }

    /**
     * Callback to the Activity
     */
    public void callbackToActivity()
    {
        mCallback.newBidListener();
    }

    public void submitBid(View view)
    {
        ApiCall newBid = new ApiCall("listing/" + selectedListing.id() + "/bids/new", getContext());
        newBid.addParam("amount", Double.toString(this.bid_amount.getRawValue() / 100));
        newBid.addParam("message", this.bid_message.getText().toString());

        newBid.addResponseListener(new ResponseListener()
        {
            @Override
            public void responseReceived(ApiResponse response)
            {
                if (response.success())
                {
                    FragmentManager fm = getActivity()
                            .getSupportFragmentManager();
                    fm.popBackStack();
                }
                else
                {
                    Utils.displayMessage(getActivity(), response.getErrors().get(0));
                }
            }
        });
        newBid.sendRequest();
        Toast.makeText(getContext(), R.string.submitting, Toast.LENGTH_SHORT).show();
    }
}
