package uk.ac.tees.com2060.oreo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiCall;
import uk.ac.tees.com2060.oreo.ApiCallLib.ApiResponse;
import uk.ac.tees.com2060.oreo.ApiCallLib.ResponseListener;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * EditProfileFragment.java
 * <p>
 * The Fragment class that handles the Edit Profile page
 */
public class EditProfileFragment extends Fragment {
    EditProfileListener mCallback;

    ProgressBar spinner;
    CircleImageView image;
    EditText alias;
    EditText name;
    EditText postcode;
    Button button_user_details;
    EditText email;
    Button button_user_email;
    EditText mobile;
    Button button_user_number;
    EditText current_password;
    EditText new_password;
    EditText validate_password;
    Button button_user_password;
    int secretCount = 0;

    public EditProfileFragment() {}

    /**
     * Interface for the Activity to implement - enables activity/fragment communication
     */
    public interface EditProfileListener {
        void editProfileListener(Bundle b);
    }

    /**
     * Overridden default onCreateView() method
     * Sets the title in the title bar and displays the fragment layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        mCallback = (EditProfileListener) getActivity();

        getActivity().setTitle("Edit Your Profile");

        image = view.findViewById(R.id.imageView_profile_view_profile_photo);
        image.setImageBitmap(User.getUser().profilePhoto());
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                doUpdatePhoto();
            }
        });

        alias = view.findViewById(R.id.editText_profile_known_as);
        alias.setText(User.getUser().knownAs());

        name = view.findViewById(R.id.editText_profile_full_name);
        name.setText(User.getUser().fullName());

        postcode = view.findViewById(R.id.editText_profile_postcode);
        postcode.setText(User.getUser().postcode());

        button_user_details = view.findViewById(R.id.button_update_details);
        button_user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                doUpdateDetails();
            }
        });

        email = view.findViewById(R.id.editText_profile_email);
        email.setText(User.getUser().emailAddress());

        button_user_email = view.findViewById(R.id.button_update_email);
        button_user_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                doUpdateEmail();
            }
        });

        mobile = view.findViewById(R.id.editText_profile_mobile_number);
        mobile.setText(User.getUser().mobileNumber());

        button_user_number = view.findViewById(R.id.button_update_mobile_number);
        button_user_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                doUpdateMobileNumber();
            }
        });

        current_password = view.findViewById(R.id.editText_profile_password_current);
        new_password = view.findViewById(R.id.editText_profile_password_new);
        validate_password = view.findViewById(R.id.editText_profile_password_check);

        button_user_password = view.findViewById(R.id.button_update_password);
        button_user_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                doUpdatePassword();
            }
        });

        TextView version = view.findViewById(R.id.textView_profile_version);
        version.setText(String.format(Locale.ENGLISH, "%s-%d", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Secret();
            }
        });

        spinner = view.findViewById(R.id.progressBar_edit_profile_picture);

        setHasOptionsMenu(true);

        return view;
    }

    private void doUpdatePhoto() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = imageReturnedIntent.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                image.setClickable(false);
                spinner.setVisibility(View.VISIBLE);

                ApiCall doUpdatePhoto = new ApiCall("user/updateProfilePhoto", this.getContext());
                doUpdatePhoto.addParam("profile_photo", Utils.getStringFromImage(bmp));
                doUpdatePhoto.addResponseListener(new ResponseListener() {
                    @Override
                    public void responseReceived(ApiResponse response) {
                        if (response.success()) {
                            User.getUser().setProfilePhoto(bmp);
                            image.setImageBitmap(bmp);
                            Toast toast = Toast.makeText(getContext(), "Profile picture updated!", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            //TODO:Fail message
                        }

                        image.setClickable(true);
                        spinner.setVisibility(View.INVISIBLE);
                    }
                });

                doUpdatePhoto.sendRequest();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doUpdatePassword() {
        if (validatePasswords()) {
            ApiCall doUpdatePassword = new ApiCall("user/updatePassword", this.getContext());

            doUpdatePassword.addParam("current_password", current_password.getText().toString());
            doUpdatePassword.addParam("new_password", new_password.getText().toString());

            doUpdatePassword.addResponseListener(new ResponseListener() {
                @Override
                public void responseReceived(ApiResponse response) {
                    if (response.success()) {
                        Toast toast = Toast.makeText(getActivity().getBaseContext(), "Password Updated!", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        Utils.displayMessage(getActivity(), "Your password wasn't changed");
                    }
                }
            });

            doUpdatePassword.sendRequest();

            current_password.setText("");
            new_password.setText("");
            validate_password.setText("");
        }
    }

    private boolean validatePasswords() {
        if (!(new_password.getText().toString().equals(validate_password.getText().toString()))) {
            Utils.displayMessage(getActivity(), R.string.validation_invalid_password, R.string.okay);
            return false;
        } else {
            return true;
        }
    }

    private void doUpdateMobileNumber() {
        if (validateMobileNumber()) {
            ApiCall doUpdateNumber = new ApiCall("user/updatePhoneNumber", this.getContext());
            doUpdateNumber.addParam("phone_number", mobile.getText().toString());
            doUpdateNumber.addResponseListener(new ResponseListener() {
                @Override
                public void responseReceived(ApiResponse response) {
                    if (response.success()) {
                        User.getUser().setMobileNumber(mobile.getText().toString());
                        Toast toast = Toast.makeText(getContext(), "Verification Sent!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });

            doUpdateNumber.sendRequest();
        }
    }

    private boolean validateMobileNumber() {
        if (!mobile.getText().toString().matches(
                getString(R.string.regex_mobile_number))) {
            Utils.displayMessage(getActivity(), R.string.validation_invalid_phone_number, R.string.okay);
            return false;
        } else {
            return true;
        }
    }

    private void doUpdateEmail() {
        if (validateEmail()) {
            ApiCall doUpdate = new ApiCall("user/updateEmail", this.getContext());
            doUpdate.addParam("email_address", email.getText().toString());
            doUpdate.addResponseListener(new ResponseListener() {
                @Override
                public void responseReceived(ApiResponse response) {
                    if (response.success()) {
                        Toast toast = Toast.makeText(getContext(), "Verification Sent!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        //todo:fail message
                    }
                }
            });

            doUpdate.sendRequest();
        }
    }

    private boolean validateEmail() {
        if (email.length() < 5) {
            Utils.displayMessage(getActivity(), R.string.validation_complete_all_fields, R.string.okay);
            return false;
        } else {
            return true;
        }
    }

    private void doUpdateDetails() {
        if (validateDetails()) {
            ApiCall doUpdate = new ApiCall("user/updateUserInfo", this.getContext());
            doUpdate.addParam("known_as", alias.getText().toString());
            doUpdate.addParam("full_name", name.getText().toString());
            doUpdate.addParam("postcode", postcode.getText().toString());
            doUpdate.addResponseListener(new ResponseListener() {
                @Override
                public void responseReceived(ApiResponse response) {
                    if (response.success()) {
                        User.getUser().setAlias(alias.getText().toString());
                        User.getUser().setFullName(name.getText().toString());
                        User.getUser().setPostcode(postcode.getText().toString());
                        Toast toast = Toast.makeText(getContext(), "Updated!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        //todo:fail message
                    }
                }
            });

            doUpdate.sendRequest();
        }
    }

    private boolean validateDetails() {
        if (alias.length() < 1 || name.length() < 3) {
            Utils.displayMessage(getActivity(), R.string.validation_complete_all_fields, R.string.okay);
            return false;

        } else if (!postcode.getText().toString().matches(
                getString(R.string.regex_postcode))) {
            Utils.displayMessage(getActivity(), R.string.validation_invalid_postcode, R.string.okay);
            return false;

        } else {
            return true;
        }
    }

    /**
     * Callback to the Activity
     */
    public void callbackToActivity(Bundle b) {
        mCallback = (EditProfileListener) getActivity();
        mCallback.editProfileListener(b);
    }

    private void closeKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }

    private void Secret() {
        secretCount++;

        if (secretCount >= 10) {

            Random rand = new Random();
            Bundle b = new Bundle();
            b.putBoolean("secret", true);

            if (User.getUser().id() == 21) {
                b.putInt("userid", 23);
            } else if (User.getUser().id() == 23) {
                b.putInt("userid", 21);
            } else {
                if (rand.nextInt(100) >= 50) {
                    b.putInt("userid", 23);
                } else {
                    b.putInt("userid", 21);
                }
            }
            callbackToActivity(b);
        }
    }
}
