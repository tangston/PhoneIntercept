package ts.phoneintercept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;


public class TelReceiver extends BroadcastReceiver {
    public  Context context;
    public  Intent intent;
    TelephonyManager telephonyManager;
    public static final String tag="ts";

    @Override
    public void onReceive(Context context, Intent intent) {
       // Log.i(tag,"received");
        initialize(context,intent);

        outGoingBlock();
    }
    public void initialize(Context context, Intent intent){
       // Log.i(tag,"initialize");
        this.context=context;
        this.intent=intent;
       // telephonyManager =context.getSystemService(TelephonyManager.class);
    }
    public void outGoingBlock(){
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
           // Log.i(tag,"new out going call");
            String outGogingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            if(isInBlackList(outGogingNumber)){
                endOutCall();
            }
            endOutCall();
        }
    }

    public static boolean isInBlackList(String inComingNumber){
        String testNumber="18711311553";
        if(inComingNumber.equals(testNumber)){
            return true;
        }
        return false;
    }

    private void endOutCall(){
        setResultData(null);
    }

}
