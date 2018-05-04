package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * MyJobsFragment.java
 * <p>
 * The Fragment class that handles the MyJobsFragment page
 */
public class MyJobsFragment extends Fragment {
    MyJobsFragmentListener mCallback;


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
            mCallback = (MyJobsFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement listener");
        }
    }

    public MyJobsFragment() {
    }

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface MyJobsFragmentListener {
        void myJobsFragmentListener(Contract contract);
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("My Jobs");

        final View view = inflater.inflate(R.layout.fragment_my_jobs, container, false);
        setHasOptionsMenu(true);

        final ProgressBar progressBar = view.findViewById(R.id.progressBar_my_jobs);
        final ListView listView = view.findViewById(R.id.listView_my_jobs);
        final ArrayList[] contracts = new ArrayList[1];


        ApiCall api = new ApiCall("contract/list/courier", getContext());
        api.addResponseListener(new ResponseListener() {
            @Override
            public void responseReceived(ApiResponse response) {
                if (response.success()) {
                    contracts[0] = Contract.getContracts(response.getBodyArray());
                    listView.setAdapter(new ContractsAdapter(getContext(), contracts[0]));
                    progressBar.setVisibility(View.INVISIBLE);

                    TextView nothingToSee = view.findViewById(R.id.my_jobs_no_results);
                    if(contracts[0].size() == 0){
                        listView.setVisibility(View.INVISIBLE);
                        nothingToSee.setVisibility(View.VISIBLE);
                    }else{
                        nothingToSee.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        api.sendRequest();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contract selectedContract = ((ArrayList<Contract>) contracts[0]).get(position);

                callbackToActivity(selectedContract);
            }

        });

        return view;
    }

    /**
     * Callback to the Activity
     */
    public void callbackToActivity(Contract selectedContract) {
        mCallback = (MyJobsFragmentListener) getActivity();
        mCallback.myJobsFragmentListener(selectedContract);
    }
}
