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

import java.util.ArrayList;

import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

/**
 * MyShipmentsFragment.java
 * <p>
 * The Fragment class that handles the My Shipments page
 */
public class MyShipmentsFragment extends Fragment {
    MyShipmentsFragmentListener mCallback;


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
            mCallback = (MyShipmentsFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement listener");
        }
    }

    public MyShipmentsFragment() {
    }

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface MyShipmentsFragmentListener {
        void myShipmentsFragmentListener(Contract contract);
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("My Shipments");

        final View view = inflater.inflate(R.layout.fragment_my_shipments, container, false);
        setHasOptionsMenu(true);

        final ProgressBar progressBar = view.findViewById(R.id.progressBar_my_shipments);
        final ListView listView = view.findViewById(R.id.listView_my_shipments);
        final ArrayList[] contracts = new ArrayList[1];


        ApiCall api = new ApiCall("/contract/list/courier", getContext());
        api.addResponseListener(new ResponseListener() {
            @Override
            public void responseReceived(ApiResponse response) {
                if (response.success()) {
                    contracts[0] = Contract.getContracts(response.getBodyArray());
                    listView.setAdapter(new ContractsAdapter(getContext(), contracts[0]));
                    listView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
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
        mCallback = (MyShipmentsFragmentListener) getActivity();
        mCallback.myShipmentsFragmentListener(selectedContract);
    }
}
