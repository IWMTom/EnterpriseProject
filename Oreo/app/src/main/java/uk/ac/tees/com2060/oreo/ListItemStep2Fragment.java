package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andressantibanez.android.patio.Patio;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class ListItemStep2Fragment extends Fragment implements Step, Patio.PatioCallbacks
{

    public static final int REQUEST_CODE_TAKE_PICTURE = 1000;
    public static final int REQUEST_CODE_ATTACH_PICTURE = 2000;

    Patio mPatio;

    public static ListItemStep2Fragment newInstance()
    {
        return new ListItemStep2Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_item_step2, container, false);

        return v;
    }

    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        mPatio = getActivity().findViewById(R.id.patio);
        mPatio.setCallbacksListener(this);
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }

    @Override
    public void onTakePictureClick() {
        Intent intent = mPatio.getTakePictureIntent();
        startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
    }

    @Override
    public void onAddPictureClick() {
        Intent intent = mPatio.getAttachPictureIntent();
        startActivityForResult(intent, REQUEST_CODE_ATTACH_PICTURE);
    }
}