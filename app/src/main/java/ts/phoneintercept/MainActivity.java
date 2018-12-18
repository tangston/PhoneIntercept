package ts.phoneintercept;

import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    public static final String tag="gt";
    TelephonyManager telephonyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(tag,"register");
        setContentView(R.layout.activity_main);
        telephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);//为什么只能在activity里
        registerListner();
        SmsReceiver smsReceiver=new SmsReceiver();
       TelReceiver telReceiver=new TelReceiver();
        Log.i(tag,"created sms and tel");
     //   setDefaultApp();

    }

    public void setDefaultApp(){
        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, "要设置的包名");
        startActivity(intent);
    }
    private void registerListner(){
        Log.i(tag,"register");
        MyPhoneStateListener mphoneStateListener=new MyPhoneStateListener();
        telephonyManager.listen(mphoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
    }
    //来电号码识别和拦截
    public class MyPhoneStateListener extends PhoneStateListener {
        Context context;
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (telephonyManager.getCallState()) {

                case TelephonyManager.CALL_STATE_RINGING:
                   /* Intent intent=getIntent();
                    String inComingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);//去电*/
                    //Log.i(tag,"new STATE_RINGING ,number is "+incomingNumber);//为什么不在activity就不能用incomingNumber（会显示null）
                    if(TelReceiver.isInBlackList(incomingNumber))
                    {
                        endInCall();
                        Log.i(tag,"end call");
                    }

                   // endInCall();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                  //  Log.i(tag,"CALL_STATE_OFFHOOK ");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                   // Log.i(tag,"CALL_STATE_IDLE ");
                    break;
            }
        }
    }
    //-------------------------------去电号码
    private void endInCall()
    {
        Class<TelephonyManager> c = TelephonyManager.class;
        try {
            Method getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);
            getITelephonyMethod.setAccessible(true);
            ITelephony iTelephony = null;
            iTelephony = (ITelephony) getITelephonyMethod.invoke(telephonyManager, (Object[]) null);
            iTelephony.endCall();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
