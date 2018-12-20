package ts.phoneintercept;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String tag="db";
    SQLiteDatabase sqLdb;
    TelephonyManager telephonyManager;
    DataBase db;

    Button btn;
    Button gettime;
    TextView text;
    Button record;
    Button white;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.db=new DataBase(getApplicationContext());//实例化数据库
        // Log.i(tag,"register");
        setContentView(R.layout.activity_main);
        telephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);//为什么只能在activity里
        registerListner();
        SmsReceiver smsReceiver=new SmsReceiver(db);
        TelReceiver telReceiver=new TelReceiver(db);
        //  Log.i(tag,"created sms and tel and db");

        //   setDefaultApp();
     //test();
        setContentView(R.layout.activity_main);
        init();
    }


    public void init(){
        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
        text=(TextView)findViewById(R.id.text01);
        record=(Button)findViewById(R.id.record);
        record.setOnClickListener(this);
        white=(Button)findViewById(R.id.white);
        white.setOnClickListener(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn:
                Intent intent =new Intent(this,Black.class);
                startActivity(intent);
                break;
            case R.id.gettime:

              //  text.setText(getNowTime());
                break;
            case R.id.record:
                Intent intents =new Intent(this,BlackRecords.class);
                startActivity(intents);
                break;
            case R.id.white:
                Intent intentw=new Intent(this,White.class);
                startActivity(intentw);
        }
    }

    private void registerListner(){
        // Log.i(tag,"register");
        MyPhoneStateListener mphoneStateListener=new MyPhoneStateListener();
        telephonyManager.listen(mphoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
    }
    //来电号码识别和拦截
    public class MyPhoneStateListener extends PhoneStateListener {
        Context context;
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {//String incomingNumber------------ 没用
            switch (telephonyManager.getCallState()) {

                case TelephonyManager.CALL_STATE_RINGING:
                /* String inComingNumber = getIntent().getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);//来电
                    Log.i(tag,"new STATE_RINGING ,number is "+incomingNumber);//为什么不在activity就不能用incomingNumber（会显示null）
                */      if(TelReceiver.isInBlackList(incomingNumber))
                    {
                       // Log.i(tag,"number is "+incomingNumber+" end call");
                        endInCall();
                        Date date;
                        date = new Date();
                        db.addPhoneCallRecord(incomingNumber,date.toString());
                        //测试区，测试打入来电记录黑名单于数据库内是否可行,一列id，二列时间，三列电话
                       // sqLdb=db.getWritableDatabase();//-----------------------------每次打开数据库查询才用
                       // Cursor cursor=sqLdb.query(DataBase.TABLE_phoneCallRecord,null,null,null,null,null,null);
                      //  while(cursor.moveToNext()){
                      //      Log.i(tag,cursor.getString(1)+"  "+cursor.getString(2));
                      //  }
                      //  cursor.close();
                       // sqLdb.close();
                    }
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
    private void test(){
        String table_name=DataBase.TABLE_keyword;
        //数据库创建
        //  Log.i(tag,"\n into test");
        // db=new DataBase(getApplicationContext());
        db.addBlackList("13974857489");
        db.addBlackList("17673209732");
        db.addBlackList("18711311553");
        db.addKeyword("益丰大药房");
        db.addKeyword("全店满");
        db.addKeyword("津贴叠加");
        sqLdb=db.getWritableDatabase();//-----------------------------每次打开数据库查询才用
        Cursor cursor=sqLdb.query(table_name,null,null,null,null,null,null);
        while(cursor.moveToNext()){

            // Log.i(tag,cursor.getString(0));
            Log.i(tag,table_name+ cursor.getString(1));
        }
        cursor.close();
        sqLdb.close();

        /*
//------------------手动读取table blacklist
      // Log.i(tag,"\n created db");


//为了测试手动加点黑名单号码-----------------------------------成功

        db.deleteBlackList(Integer.toString(9));


       int i=1;
        for(;i<=8;i++){
            db.deleteBlackList(Integer.toString(i));
        }

       */
    }

}
