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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class ListItemStep2Fragment extends Fragment implements Step
{

    private EditText itemDescription;
    private RadioGroup itemSize;
    private EditText importantDetails;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_list_item_step2, container, false);

        itemDescription = v.findViewById(R.id.editText_itemDescription);
        itemSize = v.findViewById(R.id.radioGroup_itemSize);
        importantDetails = v.findViewById(R.id.editText_importantDetails);

        return v;
    }

    @Override
    public VerificationError verifyStep()
    {
        if (itemDescription.getText().toString().isEmpty())
        {
            return new VerificationError("You must tell us what your item is!");
        }
        else if (itemSize.getCheckedRadioButtonId() == -1)
        {
            return new VerificationError("You must tell us what size your item is!");
        }
        else
        {
            RadioButton rb = v.findViewById(itemSize.getCheckedRadioButtonId());

            SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
            editor.putString("itemDescription", itemDescription.getText().toString());
            editor.putString("itemSize", rb.getText().toString());
            editor.putString("importantDetails", importantDetails.getText().toString());
            editor.commit();

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