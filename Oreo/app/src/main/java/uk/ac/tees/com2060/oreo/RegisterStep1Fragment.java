package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RegisterStep1Fragment extends Fragment
{
    RegisterStep1Listener mCallback;

    public RegisterStep1Fragment() {}

    public interface RegisterStep1Listener
    {
        public void step1Listener();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            mCallback = (RegisterStep1Listener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement RegisterStep1Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_register_step1, container, false);

        Button nextStep = (Button) view.findViewById(R.id.button9);
        nextStep.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mCallback.step1Listener();
            }
        });

        return view;
    }
}
