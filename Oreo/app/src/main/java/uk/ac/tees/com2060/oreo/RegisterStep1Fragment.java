package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * RegisterStep1Fragment.java
 *
 * The Fragment class that handles step 1 of user registration.
 * User data is collected and sent back to the Activity when complete.
 */
public class RegisterStep1Fragment extends Fragment
{
    RegisterStep1Listener mCallback;
    Bitmap profile_photo;
    EditText editText_full_name;
    EditText editText_known_as;
    EditText editText_dob;
    Button button_next_step;
    ImageView profilePhoto;

    /**
     * Required empty constructor
     */
    public RegisterStep1Fragment() {}

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface RegisterStep1Listener
    {
        void step1Listener(Bitmap profile_photo, String full_name,
                                  String known_as, String dob);
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
            mCallback = (RegisterStep1Listener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement RegisterStep1Listener");
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
        getActivity().setTitle(R.string.activity_register_step1_title);

        View view = inflater.inflate(R.layout.fragment_register_step1, container, false);
        setHasOptionsMenu(true);

        profilePhoto = (ImageView) view.findViewById(R.id.imageView_profile_view_image);
        editText_dob = (EditText) view.findViewById(R.id.editText_dob);
        editText_full_name = (EditText) view.findViewById(R.id.editText_full_name);
        editText_known_as = (EditText) view.findViewById(R.id.editText_known_as);
        button_next_step = (Button) view.findViewById(R.id.button_next_step);

        editText_full_name.addTextChangedListener(watcher);
        editText_known_as.addTextChangedListener(watcher);
        editText_dob.addTextChangedListener(watcher);

        if (profile_photo != null)
        {
            profilePhoto.setImageBitmap(profile_photo);
        }

        /**
         * Listens for when the date picker dialog has been used, and sets the text
         * in the date of birth field to be the selected date
         */
        final Calendar c = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2)
            {
                c.set(Calendar.YEAR, i);
                c.set(Calendar.MONTH, i1);
                c.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
                editText_dob.setText(df.format(c.getTime()));
            }
        };

        /**
         * onClick listener for the date of birth field, which opens the date picker dialog
         */
        c.add(Calendar.YEAR, -18);
        editText_dob.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DatePickerDialog dialog = new DatePickerDialog(view.getContext(), date,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(c.getTime().getTime());
                dialog.show();
            }
        });


        /**
         * onClick listener for the next step button
         * If a profile photo hasn't been set, a warning message will be displayed to give the
         * user the opportunity to set one before continuing. If they choose to continue, the
         * fields will be validated and step 2 will be displayed
         */
        button_next_step.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (profile_photo == null)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.activity_register_step1_no_profile_photo)
                            .setPositiveButton(R.string.activity_register_step1_no_profile_photo_no,
                                    new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                                callbackToActivity();
                                        }
                                    })
                            .setNegativeButton(R.string.activity_register_step1_no_profile_photo_yes, null);
                    builder.create();
                    builder.show();
                }
                else
                {
                    callbackToActivity();
                }
            }
        });

        return view;
    }

    /**
     * Callback to the Activity with all user provided data
     */
    public void callbackToActivity()
    {
        mCallback.step1Listener(profile_photo, editText_full_name.getText().toString(),
                editText_known_as.getText().toString(), editText_dob.getText().toString());
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
            if (editText_full_name.getText().toString().length() != 0
                    && editText_known_as.getText().toString().length() != 0
                    && editText_dob.getText().toString().length() != 0)
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

    /**
     * Adds the profile photo selection button to the title bar
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu_register, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Called when the profile photo selection button is selected.
     * Fires off an intent to select an image.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, 1);

        return super.onOptionsItemSelected(item);
    }

    /**
     * Used for the profile picture image picker functionality.
     * Receives the selected image and sets it accordingly.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK)
        {
            try
            {
                final Uri imageUri = imageReturnedIntent.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                profile_photo = BitmapFactory.decodeStream(imageStream);

                profilePhoto.setImageBitmap(profile_photo);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }
}
