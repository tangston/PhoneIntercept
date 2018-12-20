package ts.phoneintercept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Telephony;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TelReceiver extends BroadcastReceiver {
    public static DataBase db;
    public  Context context;
    public  Intent intent;
    TelephonyManager telephonyManager;
    public static final String tag="teldb";
    public TelReceiver( ) {

    }
    public TelReceiver(DataBase db) {
        this.db=db;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i(tag,"received");
        initialize(context,intent);
        outGoingBlock();
    }
    public void initialize(Context context, Intent intent){
        //  Log.i(tag,"initialize");
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
                final Toast toast = Toast.makeText(context, "删除黑名单后再播", Toast.LENGTH_SHORT);
                toast.show();
                //  Log.i(tag,"end call Number is"+outGogingNumber);
            }
            // Log.i(tag,"end call");
            //  endOutCall();
        }
    }

    public static boolean isInBlackList(String inComingNumber){
        if(isInWhiteList(inComingNumber)){
            return false;
        }
        Log.i(tag,"Incomingnumber is "+inComingNumber);
        //boolean result=false;
        //------------------------查询提取结果
        SQLiteDatabase sqLdb;
        sqLdb=db.getWritableDatabase();//-----------------------------每次打开数据库查询才用
        Cursor cursor=sqLdb.query(DataBase.TABLE_blackList,null,null,null,null,null,null);
        while(cursor.moveToNext()){
           // Log.i(tag,"search for table_blacklist "+cursor.getString(1));
            if(cursor.getString(1).equals(inComingNumber)){
               // Log.i(tag,"Find table_blacklist "+cursor.getString(1));
             return true;
            }
        }
        cursor.close();
        sqLdb.close();
        return false;
    }
    public static boolean isInWhiteList(String inComingNumber){
        // String testNumber="18711311553";
      //  Log.i(tag,"Incomingnumber is "+inComingNumber);
        //boolean result=false;
        //------------------------查询提取结果
        SQLiteDatabase sqLdb;
        sqLdb=db.getWritableDatabase();//-----------------------------每次打开数据库查询才用
        Cursor cursor=sqLdb.query(DataBase.TABLE_whiteList,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            // Log.i(tag,"search for table_blacklist "+cursor.getString(1));
            if(cursor.getString(1).equals(inComingNumber)){
                // Log.i(tag,"Find table_blacklist "+cursor.getString(1));
                return true;
            }
        }
        cursor.close();
        sqLdb.close();
        return false;
    }
    public static String getNowTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
    private void endOutCall(){
        setResultData(null);
    }

}
