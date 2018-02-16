package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterStep2Fragment extends Fragment
{
    RegisterStep2Listener mCallback;
    TextView textView_hey;
    EditText editText_postcode;
    EditText editText_phone_number;
    EditText editText_email_address;
    EditText editText_password;
    EditText editText_confirm_password;
    Button button_next_step;

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
        getActivity().setTitle(R.string.activity_register_step2_title);

        View view = inflater.inflate(R.layout.fragment_register_step2, container, false);

        editText_postcode = (EditText) view.findViewById(R.id.editText_postcode);
        editText_phone_number = (EditText) view.findViewById(R.id.editText_phone_number);
        editText_email_address = (EditText) view.findViewById(R.id.editText_email_address);
        editText_password = (EditText) view.findViewById(R.id.editText_password);
        editText_confirm_password = (EditText) view.findViewById(R.id.editText_confirm_password);
        button_next_step = (Button) view.findViewById(R.id.button_next_step);

        editText_postcode.addTextChangedListener(watcher);
        editText_phone_number.addTextChangedListener(watcher);
        editText_email_address.addTextChangedListener(watcher);
        editText_password.addTextChangedListener(watcher);
        editText_confirm_password.addTextChangedListener(watcher);

        Button nextStep = (Button) view.findViewById(R.id.button_next_step);
        nextStep.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (validateFields())
                {
                    mCallback.step2Listener();
                }
            }
        });

        textView_hey = (TextView) view.findViewById(R.id.textView_hey);
        textView_hey.setText(getResources().getString(R.string.activity_register_step2_nearly_there, getArguments().getString("known_as")));

        return view;
    }

    public boolean validateFields()
    {
        if (!editText_postcode.getText().toString().matches(
                "^[a-zA-Z]{1,2}([0-9]{1,2}|[0-9][a-zA-Z])\\s*[0-9][a-zA-Z]{2}$"))
        {
            Utils.displayMessage(getActivity(), R.string.validation_invalid_postcode, R.string.okay);
            return false;
        }
        else if(!editText_phone_number.getText().toString().matches(
                "^(\\s?7\\d{3}|\\(?07\\d{3}\\)?)\\s?\\d{3}\\s?\\d{3}$"))
        {
            Utils.displayMessage(getActivity(), R.string.validation_invalid_phone_number, R.string.okay);
            return false;
        }
        else if(!(editText_password.getText().toString().equals(editText_confirm_password.getText().toString())))
        {
            Utils.displayMessage(getActivity(), R.string.validation_invalid_password, R.string.okay);
            return false;
        }

        return true;
    }

    /**
     * Watches all text fields in the fragment and tests whether they're all filled in
     * When all the fields are completed, the next step button is activated.
     */
    private TextWatcher watcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable)
        {
            if (editText_postcode.getText().toString().length() != 0
                    && editText_phone_number.getText().toString().length() != 0
                    && editText_email_address.getText().toString().length() != 0
                    && editText_password.getText().toString().length() != 0
                    && editText_confirm_password.getText().toString().length() != 0)
            {
                // Enable button, set style to primary
                button_next_step.setEnabled(true);
                button_next_step.setBackgroundResource(R.drawable.button_rounded_primary);
                button_next_step.setTextColor(getResources().getColor(R.color.colorWhite));
            }
            else
            {
                // Disable button, set style to disabled
                button_next_step.setEnabled(false);
                button_next_step.setBackgroundResource(R.drawable.button_rounded_disabled);
                button_next_step.setTextColor(getResources().getColor(R.color.colorDarkGrey));
            }
        }
    };
}
