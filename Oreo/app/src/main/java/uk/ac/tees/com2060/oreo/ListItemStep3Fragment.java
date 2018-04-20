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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class ListItemStep3Fragment extends Fragment implements Step
{

    TextView collectionLocation, deliveryLocation, itemDescription, itemSize, importantDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_item_step3, container, false);

        collectionLocation = v.findViewById(R.id.textView_collectionLocation);
        deliveryLocation = v.findViewById(R.id.textView_deliveryLocation);
        itemDescription = v.findViewById(R.id.textView_itemDescription);
        itemSize = v.findViewById(R.id.textView_itemSize);
        importantDetails = v.findViewById(R.id.textView_importantDetails);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        collectionLocation.setText(sharedPref.getString("collectionLocation", null));
        deliveryLocation.setText(sharedPref.getString("deliveryLocation", null));
        itemDescription.setText(sharedPref.getString("itemDescription", null));
        itemSize.setText(sharedPref.getString("itemSize", null));
        importantDetails.setText(sharedPref.getString("importantDetails", null));

        return v;
    }

    @Override
    public VerificationError verifyStep()
    {
        return null;
    }

    @Override
    public void onSelected() {}


    @Override
    public void onError(@NonNull VerificationError error)
    {
        Utils.displayMessage(getActivity(), error.getErrorMessage());
    }
}