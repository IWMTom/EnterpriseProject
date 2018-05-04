package uk.ac.tees.com2060.oreo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

public class ListItemStep3Fragment extends Fragment implements BlockingStep {

    TextView collectionLocation, deliveryLocation, itemDescription, itemSize, importantDetails;
    boolean canClick = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_item_step3, container, false);

        collectionLocation = v.findViewById(R.id.textView_collectionLocation);
        deliveryLocation = v.findViewById(R.id.textView_deliveryLocation);
        itemDescription = v.findViewById(R.id.textView_itemDescription);
        itemSize = v.findViewById(R.id.textView_itemSize);
        importantDetails = v.findViewById(R.id.textView_importantDetails);

        return v;
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        collectionLocation.setText(sharedPref.getString("collectionLocation", null));
        deliveryLocation.setText(sharedPref.getString("deliveryLocation", null));
        itemDescription.setText(sharedPref.getString("itemDescription", null));
        itemSize.setText(sharedPref.getString("itemSize", null));
        importantDetails.setText(sharedPref.getString("importantDetails", null));

    }


    @Override
    public void onError(@NonNull VerificationError error) {
        Utils.displayMessage(getActivity(), error.getErrorMessage());
    }

    @Override
    @UiThread
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.goToNextStep();
            }
        }, 2000L);
    }

    private String getShortSize(String longSize) {
        if (longSize.startsWith("Small")) {
            return "small";
        } else if (longSize.startsWith("Medium")) {
            return "medium";
        } else if (longSize.startsWith("Large")) {
            return "large";
        } else if (longSize.startsWith("Extra Large")) {
            return "xlarge";
        } else {
            return "huge";
        }
    }

    @Override
    @UiThread
    public void onCompleteClicked(final StepperLayout.OnCompleteClickedCallback callback) {

        ApiCall api = new ApiCall("listing/new", getContext());
        api.addParam("item_description", itemDescription.getText().toString());
        api.addParam("item_size", getShortSize(itemSize.getText().toString()));
        api.addParam("important_details", importantDetails.getText().toString());
        api.addParam("collection_location", collectionLocation.getText().toString());
        api.addParam("delivery_location", deliveryLocation.getText().toString());

        api.addResponseListener(new ResponseListener() {
            @Override
            public void responseReceived(ApiResponse response) {
                canClick = true;
                if (response.success()) {
                    Utils.clearTemporaryStorage(getActivity());

                    try {
                        getActivity().getPreferences(Context.MODE_PRIVATE).edit()
                                .putInt("listing_id", response.getBody().getInt("id"))
                                .apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callback.complete();
                        }
                    }, 0);
                } else {
                    Utils.displayMessage(getActivity(), response.getErrors().get(0));
                }
            }
        });

        if(canClick){api.sendRequest();}
        canClick = false;
        Toast.makeText(getContext(), R.string.submitting, Toast.LENGTH_SHORT).show();
    }

    @Override
    @UiThread
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}