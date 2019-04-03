package re.med.kafal;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Pc on 21.05.2018.
 */

public class FirebaseRegister extends FirebaseInstanceIdService

{

    @Override
    public void onTokenRefresh() {

        String refreshToken = FirebaseInstanceId.getInstance().getToken();

        Log.e("ID ", refreshToken);
    }
}
