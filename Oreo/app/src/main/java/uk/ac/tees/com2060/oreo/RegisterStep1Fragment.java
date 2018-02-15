package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

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
        setHasOptionsMenu(true);

//        Button nextStep = (Button) view.findViewById(R.id.button9);
//        nextStep.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View v)
//            {
//                mCallback.step1Listener();
//            }
//        });


        final Calendar c = Calendar.getInstance();
        final EditText dobField = (EditText) view.findViewById(R.id.editText_dob);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                c.set(Calendar.YEAR, i);
                c.set(Calendar.MONTH, i1);
                c.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
                dobField.setText(df.format(c.getTime()));
            }
        };

        dobField.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DatePickerDialog dialog = new DatePickerDialog(view.getContext(), date, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu_register, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.d("TAG", "OI OI!");

        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, 1);

        return super.onOptionsItemSelected(item);
    }

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
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                ImageView profilePhoto = (ImageView) getView().findViewById(R.id.imageView_profile_photo);
                profilePhoto.setImageBitmap(selectedImage);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }
}
