package uk.ac.tees.com2060.oreo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

/**
 * ListItemFragment.java
 * <p>
 * The Fragment class that handles the List Item page
 */
public class ListItemFragment extends Fragment implements StepperLayout.StepperListener {
    ListItemListener mCallback;
    private ListItemStepperAdapter stepperAdapter;
    private boolean cantDestroy;

    public ListItemFragment() {
    }

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface ListItemListener {
         void listItemListener(int id);
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.fragment_list_item_title);

        View view = inflater.inflate(R.layout.fragment_list_item, container, false);
        setHasOptionsMenu(true);

        stepperAdapter = new ListItemStepperAdapter(getFragmentManager(), getContext());

        StepperLayout mStepperLayout = view.findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(stepperAdapter);
        mStepperLayout.setListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cantDestroy) {
            return;
        }
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        for (Step step : stepperAdapter.getFragments()) {
            fragmentTransaction.remove((Fragment) step);
        }

        fragmentTransaction.commit();

    }

    /**
     * Callback to the Activity
     */
    public void callbackToActivity(int id) {
        mCallback.listItemListener(id);
    }

    @Override
    public void onCompleted(View completeButton) {
        callbackToActivity(
                getActivity().getPreferences(Context.MODE_PRIVATE).getInt("listing_id", -1));

        Utils.clearTemporaryStorage(getActivity());
    }

    @Override
    public void onError(VerificationError verificationError) {
    }

    @Override
    public void onStepSelected(int newStepPosition) {
    }

    @Override
    public void onReturn() {
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        cantDestroy = true;
        super.onSaveInstanceState(savedInstanceState);
    }

}
