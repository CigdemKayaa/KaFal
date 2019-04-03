package re.med.kafal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Pc on 14.05.2018.
 */

public class Sms extends BroadcastReceiver

{
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle pudsBundle = intent.getExtras();
        Object[] pdus = (Object[]) pudsBundle.get("pdus");
        SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
        Toast.makeText(context, "Yeni SMS: " + messages.getMessageBody(),
                Toast.LENGTH_LONG).show();
        Log.e(getClass().getName().toString(), "yeni sms geldi");
    }
}
