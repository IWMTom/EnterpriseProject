package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * ViewProfileFragment.java
 * <p>
 * The Fragment class that handles the View Profile page
 */
public class BidConfirmFragment extends Fragment {
    BidConfirmListener mCallback;
    DeleteBidListener deleteCallback;

    CircleImageView profilePhoto;
    TextView reputationText;
    TextView usernameText;
    ProgressBar profileProgressBar;
    ConstraintLayout hiddenControls;
    TextView titleText;
    TextView locationText;

    Button confirmButton;
    Button declineButton;

    Bid bid;
    Listing listing;

    public BidConfirmFragment() {
    }

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface BidConfirmListener {
        void bidConfirmListener(Bundle b);
    }

    public interface DeleteBidListener {
        void deleteBidListener();
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bid_confirm, container, false);

        getActivity().setTitle("Bid on your Listing");

        hiddenControls = view.findViewById(R.id.confirm_constraint_hidden);

        Bundle args = this.getArguments();

        if (args.get("bid") != null) {
            args.getInt("bid");
        }

        profilePhoto = view.findViewById(R.id.imageView_confirm_profile_picture);
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGoToProfile(bid.user_id());
            }
        });

        reputationText = view.findViewById(R.id.textView_confirm_rep);

        usernameText = view.findViewById(R.id.textView_confirm_name);

        titleText = view.findViewById(R.id.textView_confirm_title);

        locationText = view.findViewById(R.id.textView_confirm_location);

        profileProgressBar = view.findViewById(R.id.progressBar_confirm);
        profileProgressBar.setVisibility(View.VISIBLE);

        titleText.setText(getFlavourText());

        Picasso.get().load("https://getshipr.com/api/user/" + args.getInt("userid") + "/photo").placeholder(R.drawable.default_profile_photo).into(profilePhoto, new Callback() {
            @Override
            public void onSuccess() {
                profileProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {
                profileProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        ApiCall getUserData = new ApiCall("user/" + args.getInt("userid"), this.getContext());
        getUserData.addResponseListener(new ResponseListener() {
            @Override
            public void responseReceived(ApiResponse response) {
                if (response.success()) {
                    try {
                        reputationText.setVisibility(View.VISIBLE);
                        usernameText.setVisibility(View.VISIBLE);
                        locationText.setVisibility(View.VISIBLE);

                        reputationText.setText(String.valueOf(response.getBody().get("reputation")) + " ★");
                        usernameText.setText((String) response.getBody().get("known_as"));
                        locationText.setText((String) response.getBody().get("city"));

                        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
                        hiddenControls.startAnimation(slideUp);
                        hiddenControls.setVisibility(View.VISIBLE);


                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(view.getContext(), "This should not happen", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        getUserData.sendRequest();

        declineButton = view.findViewById(R.id.button_bid_decline);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiCall apiCall = new ApiCall("bid/" + String.valueOf(bid.id() + "/delete"), getContext());
                apiCall.addResponseListener(new ResponseListener() {
                    @Override
                    public void responseReceived(ApiResponse response) {
                        if (response.success()) {
                            Toast toast = Toast.makeText(getContext(), "Bid has been declined!", Toast.LENGTH_SHORT);
                            toast.show();

                            deleteCallback = (DeleteBidListener) getActivity();
                            deleteCallback.deleteBidListener();
                        } else {
                            Toast toast = Toast.makeText(getContext(), "Server Error!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
                apiCall.sendRequest();
            }
        });

        confirmButton = view.findViewById(R.id.button_bid_agree);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiCall apiCall = new ApiCall("bid/" + String.valueOf(bid.id() + "/accept"), getContext());
                apiCall.addResponseListener(new ResponseListener() {
                    @Override
                    public void responseReceived(ApiResponse response) {
                        if (response.success()) {
                            //todo bid accept screen
                        } else {
                            Toast toast = Toast.makeText(getContext(), "Server Error!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
                apiCall.sendRequest();
            }
        });
        setHasOptionsMenu(false);
        return view;
    }

    private void doGoToProfile(int i) {
        Bundle b = new Bundle();
        b.putInt("userid", i);
        callbackToActivity(b);
    }

    /**
     * Callback to the Activity
     */
    public void callbackToActivity(Bundle b) {
        mCallback = (BidConfirmListener) getContext();
        mCallback.bidConfirmListener(b);
    }

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
            mCallback = (BidConfirmListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BidConfirmListener");
        }
    }

    String getFlavourText() {
        Random rand = new Random();
        switch (rand.nextInt(10)) {
            case 0:
            case 1:
                return getString(R.string.bid_confirm_1);
            case 2:
                return getString(R.string.bid_confirm_2);
            case 3:
                return getString(R.string.bid_confirm_3);
            case 4:
                return getString(R.string.bid_confirm_4);
            case 5:
                return getString(R.string.bid_confirm_5);
            case 6:
                return getString(R.string.bid_confirm_6);
            case 7:
                return getString(R.string.bid_confirm_7);
            case 8:
                return getString(R.string.bid_confirm_8);
            case 9:
                return getString(R.string.bid_confirm_9);
            case 10:
                return getString(R.string.bid_confirm_10);
            default:
                return getString(R.string.bid_confirm_1);
        }
    }
}
