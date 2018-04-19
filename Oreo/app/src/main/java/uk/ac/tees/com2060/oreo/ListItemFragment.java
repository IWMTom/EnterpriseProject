package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;

/**
 * ListItemFragment.java
 *
 * The Fragment class that handles the List Item page
 */
public class ListItemFragment extends Fragment
{
    ListItemListener mCallback;
    private StepperLayout mStepperLayout;
    private MyStepperAdapter stepperAdapter;

    public ListItemFragment() {}

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface ListItemListener
    {
        public void listItemListener();
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
            mCallback = (ListItemListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement ListItemListener");
        }
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle(R.string.fragment_list_item_title);

        View view = inflater.inflate(R.layout.fragment_list_item, container, false);
        setHasOptionsMenu(true);

        stepperAdapter = new MyStepperAdapter(getFragmentManager(), getContext());

        mStepperLayout = (StepperLayout) view.findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(stepperAdapter);

        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        for (Step step : stepperAdapter.getFragments())
        {
            fragmentTransaction.remove((Fragment) step);
        }

        fragmentTransaction.commit();

    }

    /**
     * Callback to the Activity
     */
    public void callbackToActivity()
    {
        mCallback.listItemListener();
    }

}
