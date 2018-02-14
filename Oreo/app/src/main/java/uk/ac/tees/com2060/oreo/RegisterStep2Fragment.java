package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RegisterStep2Fragment extends Fragment
{
    RegisterStep2Listener mCallback;

    public RegisterStep2Fragment() {}

    public interface RegisterStep2Listener
    {
        public void step2Listener();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            mCallback = (RegisterStep2Listener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement RegisterStep2Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_register_step2, container, false);

        Button nextStep = (Button) view.findViewById(R.id.button10);
        nextStep.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mCallback.step2Listener();
            }
        });

        return view;
    }
}
