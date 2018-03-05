package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DashboardFragment extends Fragment
{
    DashboardListener mCallback;

    public DashboardFragment() {}

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface DashboardListener
    {
        public void dashboardListener();
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
            mCallback = (DashboardListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement DashboardListener");
        }
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     * A TextChangedListener is added to the EditText fields, to allow the continue button
     * to be disabled until all the fields have been completed fully.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle(R.string.fragment_dashboard_title);

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setHasOptionsMenu(true);

        ImageView imageView_test    = view.findViewById(R.id.dashboard_imageView);
        TextView textView_test      = view.findViewById(R.id.dashboard_textView);

        textView_test.setText(User.getUser().fullName());
        imageView_test.setImageBitmap(User.getUser().profilePhoto());

        return view;
    }

    /**
     * Callback to the Activity with all user provided data
     */
    public void callbackToActivity()
    {
        mCallback.dashboardListener();
    }
}
