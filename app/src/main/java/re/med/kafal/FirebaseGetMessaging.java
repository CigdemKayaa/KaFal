package re.med.kafal;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Pc on 21.05.2018.
 */

public class FirebaseGetMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> payload = remoteMessage.getData();


        Log.e("x","Mesaj Geldi : "+remoteMessage.getFrom());

        Iterator<String> it = payload.keySet().iterator();

        while (it.hasNext())
        {
            String key = it.next();
            String msg = payload.get(key);

            Log.e("x",key+"    ----->     "+msg);
        }
    }
}
