package uk.ac.tees.com2060.oreo;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh()
    {
        if (Utils.getUserAccessToken(getApplicationContext()) != null)
        {
            User.getUser().updatePushToken(this);
        }
    }

}