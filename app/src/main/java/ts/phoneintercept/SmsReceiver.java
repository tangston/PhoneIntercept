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
    public static DataBase db;
    public Context context;
    public Intent intent;
    public SmsReceiver( ) {

    }
    public SmsReceiver(DataBase db) {
        this.db=db;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        this.intent=intent;
        myPackageName = intent.getPackage();
      //  defPackageName=Telephony.Sms.getDefaultSmsPackage(context);
        //---------收到短信
        if (SMS_RECEIVED.equals(intent.getAction())) {
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
            //让用户设置你的应用成为默认短信应用,先实现老版本的功能
            //setDefaultApp(myPackageName);

            Log.i(TAG,"发现目标位于黑名单了");
            MySMS mySMS=new MySMS(body.toString(),number,date);
            //Log.i(TAG,mySMS.toString());
            //存储没录入的信息进垃圾箱
            db.addMessageRecord(mySMS);
            abortBroadcast();
            //测试区 1列到3列 时间 电话 文本
           // SQLiteDatabase sqLdb=db.getWritableDatabase();//-----------------------------每次打开数据库查询才用
          //  Cursor cursor=sqLdb.query(DataBase.TABLE_messageRecord,null,null,null,null,null,null);
           // while(cursor.moveToNext()){
          //      Log.i(TAG,cursor.getString(1)+"  "+cursor.getString(2)+"  "+cursor.getString(3));
         //   }
         //   cursor.close();
          //  sqLdb.close();
            //------------------------------------------------------------
        }else if(keywordDetect(body.toString()) ){
            Log.i(TAG,"发现关键字黑名单");
            MySMS mySMS=new MySMS(body.toString(),number,date);
            db.addMessageRecord(mySMS);
            abortBroadcast();
        }else{
            /////---------------正常情况或者是白名单的情况下，需要将广播接受的存到短信数据库里
            //此版本为老版本4.3 android
            Log.i(TAG,"非拦截的短信："+body.toString());
        }

        // Log.i(TAG, "\n"+number+"\n"+body.toString()+"\n"+"time "+dateFormat.format(date));

        //让用户设置回系统的短信应用
       // setDefaultApp(defPackageName);//如果不设置的话应该要有什么问题
    }
    public void setDefaultApp(String appPackageName){
        //在BroadcastReceiver的onReceive()方法中启动Activity应写为:
        Intent intent=new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, appPackageName);
        context.startActivity(intent);
    }
    public static boolean keywordDetect(String text){
        //------------------------这里有个bug，如果在短信里面的关键词前加 keyword，关键词识别就会失效
        SQLiteDatabase sqLdb=db.getWritableDatabase();//-----------------------------每次打开数据库查询才用
        sqLdb=db.getWritableDatabase();//-----------------------------每次打开数据库查询才用
        Cursor cursor=sqLdb.query(DataBase.TABLE_keyword,null,null,null,null,null,null);
        int occurrence;
        while(cursor.moveToNext()){
           // Log.i(TAG,"detecting key word "+DataBase.TABLE_keyword+ cursor.getString(1));
            occurrence=text.indexOf(DataBase.TABLE_keyword+ cursor.getString(1));
            if(occurrence==-1){
                Log.i(TAG,cursor.getString(1));
                return true;
            }
        }
        cursor.close();
        sqLdb.close();
        return false;
    }

}