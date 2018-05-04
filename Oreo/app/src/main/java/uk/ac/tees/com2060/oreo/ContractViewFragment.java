package uk.ac.tees.com2060.oreo;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
 * The Fragment class that handles the ContractViewFragment page
 */
public class ContractViewFragment extends Fragment {
    ContractViewListener mCallback;
    Contract contract;

    Boolean reviewing = false;

    public ContractViewFragment() {
    }

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface ContractViewListener {
        void ContractViewListener();
    }


    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = this.getArguments();
        contract = (Contract) arguments.getSerializable("contract");

        mCallback = (ContractViewListener) getActivity();
        final View view = inflater.inflate(R.layout.fragment_contract_view, container, false);

        TextView status = view.findViewById(R.id.contract_view_status);
        Button goodButton = view.findViewById(R.id.button_contract_advance_contract);
        Button badButton = view.findViewById(R.id.button_contract_void_contract);
        EditText review = view.findViewById(R.id.editText_contract_review);

        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);

        if (contract.senderId == User.getUser().id()) {

            if (contract.confirmed) {
                status.setText("Delivery completed");
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));

                goodButton.setText("Review this interaction");
                goodButton.setVisibility(View.VISIBLE);
                goodButton.setAnimation(slideUp);
                badButton.setText("Report Theft");
                badButton.setVisibility(View.VISIBLE);
                badButton.setAnimation(slideUp);

            } else if (contract.delivered) {
                status.setText("Please confirm delivery made");
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRed));

                goodButton.setText("Confirm delivery");
                goodButton.setVisibility(View.VISIBLE);
                goodButton.setAnimation(slideUp);
                badButton.setText("Report Theft");
                badButton.setVisibility(View.VISIBLE);
                badButton.setAnimation(slideUp);

            } else if (contract.collected) {

                status.setText("Enroute to destination");
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

            } else if (!contract.collected) {

                status.setText("Waiting for collection");
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

                badButton.setText("Cancel collection");
                badButton.setVisibility(View.VISIBLE);
                badButton.setAnimation(slideUp);
            }

        } else {


            if (contract.confirmed) {
                status.setText("Job Complete!");
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));

                goodButton.setText("Review this interaction");
                goodButton.setVisibility(View.VISIBLE);
                goodButton.setAnimation(slideUp);

            } else if (contract.delivered) {

                status.setText("Waiting for shipper to validate");
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRed));

            } else if (contract.collected) {

                status.setText("You have this item in possesion");
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

                goodButton.setText("Confirm delivery");
                goodButton.setVisibility(View.VISIBLE);
                goodButton.setAnimation(slideUp);


            } else if (!contract.collected) {
                status.setText("Waiting for you to collect from shipper");
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRed));

                goodButton.setText("Confirm collection");
                goodButton.setVisibility(View.VISIBLE);
                goodButton.setAnimation(slideUp);
                badButton.setText("Cancel collection");
                badButton.setVisibility(View.VISIBLE);
                badButton.setAnimation(slideUp);

            }

        }

        goodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (contract.senderId == User.getUser().id()) {

                    if (contract.confirmed) {

                    } else if (contract.delivered) {
                        ApiCall call = new ApiCall("contract/" + contract.id + "/setConfirmed", getContext());
                        call.addResponseListener(new ResponseListener() {
                            @Override
                            public void responseReceived(ApiResponse response) {
                                callbackToActivity();
                            }
                        });
                        call.sendRequest();
                    }
                } else {
                    if (contract.confirmed) {

                    } else if (contract.collected) {
                        ApiCall call = new ApiCall("contract/" + contract.id + "/setDelivered", getContext());
                        call.addResponseListener(new ResponseListener() {
                            @Override
                            public void responseReceived(ApiResponse response) {
                                callbackToActivity();
                            }
                        });
                        call.sendRequest();
                    } else if (!contract.collected) {
                        ApiCall call = new ApiCall("contract/" + contract.id + "/setCollected", getContext());
                        call.addResponseListener(new ResponseListener() {
                            @Override
                            public void responseReceived(ApiResponse response) {
                                callbackToActivity();
                            }
                        });
                        call.sendRequest();
                    }
                }
            }
    });


        badButton.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){

        if (contract.senderId == User.getUser().id()) {

            if (contract.confirmed) {

            } else if (contract.collected) {

            } else if (contract.delivered) {
                ApiCall call = new ApiCall("contract/" + contract.id + "/setConfirmed", getContext());
                call.addResponseListener(new ResponseListener() {
                    @Override
                    public void responseReceived(ApiResponse response) {
                        callbackToActivity();
                    }
                });
                call.sendRequest();
            } else if (contract.confirmed) {

            }

        } else {

            if (!contract.collected) {
                ApiCall call = new ApiCall("contract/" + contract.id + "/setCollected", getContext());
                call.addResponseListener(new ResponseListener() {
                    @Override
                    public void responseReceived(ApiResponse response) {
                        callbackToActivity();
                    }
                });
                call.sendRequest();
            } else if (contract.collected) {
                ApiCall call = new ApiCall("contract/" + contract.id + "/setDelivered", getContext());
                call.addResponseListener(new ResponseListener() {
                    @Override
                    public void responseReceived(ApiResponse response) {
                        callbackToActivity();
                    }
                });
                call.sendRequest();
            } else if (contract.delivered) {

            } else if (contract.confirmed) {

            }
        }
    }
    });

    setHasOptionsMenu(true);
        return view;
}


    public void callbackToActivity() {
        mCallback = (ContractViewListener) getActivity();

        mCallback.ContractViewListener();
    }
}
