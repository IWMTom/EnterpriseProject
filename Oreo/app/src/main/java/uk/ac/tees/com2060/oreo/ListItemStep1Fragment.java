package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class ListItemStep1Fragment extends Fragment implements Step
{

    private EditText editText_collection, editText_delivery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_item_step1, container, false);

        editText_collection = v.findViewById(R.id.editText_collection);
        editText_delivery = v.findViewById(R.id.editText_delivery);

        editText_collection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try
                {
                    startActivityForResult(builder.build(getActivity()), 1);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) { e.printStackTrace(); }
            }
        });

        editText_delivery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try
                {
                    startActivityForResult(builder.build(getActivity()), 2);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) { e.printStackTrace(); }
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Place place = PlacePicker.getPlace(data, getContext());
                editText_collection.setText(place.getAddress());
            }
        }
        else if (requestCode == 2)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Place place = PlacePicker.getPlace(data, getContext());
                editText_delivery.setText(place.getAddress());
            }
        }
    }

    @Override
    public VerificationError verifyStep()
    {
        if (editText_collection.getText().toString().isEmpty() || editText_collection.getText().toString().isEmpty())
        {
            return new VerificationError("You must provide both a collection and delivery location!");
        }
        else
        {
            SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
            editor.putString("collectionLocation", editText_collection.getText().toString());
            editor.putString("deliveryLocation", editText_delivery.getText().toString());
            editor.apply();

            return null;
        }
    }

    @Override
    public void onSelected() {}

    @Override
    public void onError(@NonNull VerificationError error)
    {
        Utils.displayMessage(getActivity(), error.getErrorMessage());
    }
}