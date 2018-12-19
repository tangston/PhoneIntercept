package ts.phoneintercept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;


public class SmsReceiver extends BroadcastReceiver {
   // public Message sms;
    public static final String TAG = "sms";
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public  static  String myPackageName;
    public static  String defPackageName;
    public DataBase db;
    public SmsReceiver( ) {

    }
    public SmsReceiver(DataBase db) {
        this.db=db;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //---------收到短信
        if (SMS_RECEIVED.equals(intent.getAction())) {

           myPackageName = intent.getPackage();
            defPackageName=Telephony.Sms.getDefaultSmsPackage(context);

            Log.i(TAG, "sms received!");
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                messageHandle(bundle,context);
            }
        }
    }

    public void messageHandle(Bundle bundle,Context context) {
        StringBuffer body=new StringBuffer();
        String number="";
        Long date=new Long(Long.valueOf("8012"));
        Object[] pdus = (Object[]) bundle.get("pdus");
        final SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < pdus.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            if (messages.length > 0) {
                body.append(messages[0].getMessageBody());
                number = messages[0].getOriginatingAddress();
                 date = messages[0].getTimestampMillis();
            }
        }
        if(TelReceiver.isInBlackList(number)){
            String tag="sms";
            //让用户设置你的应用成为默认短信应用
            /*
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, context.getPackageName());
            context.startActivity(intent);
            */
           // 设置完毕

            //存储没录入的信息进垃圾箱
            //sms=new Message(body.toString(),number,System.currentTimeMillis());
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //测试区，记录黑名单拦截一次
            MySMS mySMS=new MySMS(body.toString(),number,date);
            db.addMessageRecord(mySMS);
            Log.i(tag,"发现目标位于黑名单了");
            //测试区，测试打入来电记录黑名单于数据库内是否可行,一列id，二列时间，三列电话

            SQLiteDatabase sqLdb=db.getWritableDatabase();//-----------------------------每次打开数据库查询才用
            Cursor cursor=sqLdb.query(DataBase.TABLE_messageRecord,null,null,null,null,null,null);
            while(cursor.moveToNext()){
                Log.i(tag,cursor.getString(1)+"  "+cursor.getString(2));
            }
            cursor.close();
            sqLdb.close();
        }else if(false/*关键字部分被查出来*/){
            //让用户设置你的应用成为默认短信应用
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, context.getPackageName());
            context.startActivity(intent);
            // 设置完毕
            MySMS mySMS=new MySMS(body.toString(),number,date);
            db.addMessageRecord(mySMS);
            //sms=new Message(body.toString(),number,System.currentTimeMillis());
        }else{
            /////---------------正常情况或者是白名单的情况下，需要将广播接受的存到短信数据库里

        }

       // Log.i(TAG, "\n"+number+"\n"+body.toString()+"\n"+"time "+dateFormat.format(date));

        //让用户设置回系统的短信应用
        /*
        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, defPackageName);
        context.startActivity(intent);
        */
    }

}