package ts.phoneintercept;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;

public class DataBase extends SQLiteOpenHelper {
    public static final String TAG = "db";
    public static int   deleteState;//返回-1失败
    public static long   insertState;//返回-1失败
    public static String KEY_phonenumber="phonenumber";
    public static String KEY_id="_id";
    public static String KEY_time="time";
    public static String TABLE_blackList="blackList";
    public static String TABLE_phoneCallRecord="phoneCallRecord";
    public static String TABLE_messageRecord="messageRecord";

    /**
     *
     * @param context
     */
    public DataBase(Context context) {
        super(context, "black.db", null, 1);
    }

    /**
     *
     * @param db
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE blackList(_id INTEGER PRIMARY KEY AUTOINCREMENT,phonenumber VARCHAR(11))");
        db.execSQL("CREATE TABLE phoneCallRecord(_id INTEGER PRIMARY KEY AUTOINCREMENT,time VARCHAR(20),phonenumber VARCHAR(11))");
        db.execSQL("CREATE TABLE messageRecord(_id INTEGER PRIMARY KEY AUTOINCREMENT,time VARCHAR(20),phonenumber VARCHAR(11),text VARCHAR(255))");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //---------------------------------------------------------------添加部分

    /**
     *
     * @param pNumber
     */
    public void addBlackList(String pNumber) {
        ContentValues values = new ContentValues();
        values.put("phonenumber", pNumber);
        this.getWritableDatabase().insert("blackList", null, values);
        this.close();
      //  Log.i(TAG, "/n added blackList ");
    }

    /**
     *
     * @param pNumber
     * @param time
     */
 public void addPhoneCallRecord(String pNumber, String time) {
        ContentValues values = new ContentValues();
        values.put("time", time);
        values.put("phonenumber", pNumber);
        this.getWritableDatabase().insert("phoneCallRecord", null, values);
        this.close();
       // Log.i(TAG, "/n PhoneCallRecord ");
    }

    /**
     *
     * @param sms
     */
    public void addMessageRecord(MySMS sms) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        ContentValues values = new ContentValues();
        values.put("time", dateFormat.format(sms.getDate()));
        values.put("phonenumber", sms.getAddress());
        values.put("text", sms.getBody());
        this.getWritableDatabase().insert("messageRecord", null, values);
        this.close();
       // Log.i(TAG, "/n added MessageRecord ");
    }
    //-----------------------------------------------------------删除部分
    /**
     *
     * @param  sequence 序号
     */
    public void deleteBlackList(String  sequence) {
        String whereClause="_id=?";
        String args[]=new String[] {sequence};
        this.getWritableDatabase().delete("blackList", whereClause,args);
        this.close();
        //Log.i(TAG, "/n delete blackList ");
    }

    /**
     *
     * @param sequence 序号
     */
    public void deletePhoneCallRecord(String  sequence) {

        String whereClause="_id=?";
        String args[]=new String[] {sequence};
        this.getWritableDatabase().delete("phoneCallRecord", whereClause,args);
        this.close();
        //Log.i(TAG, "/n delete blackList ");
    }

    /**
     *
     * @param sequence 序号
     */
    public void deleteMessageRecord(String  sequence) {

        String whereClause="_id=?";
        String args[]=new String[] {sequence};
        this.getWritableDatabase().delete("messageRecord", whereClause,args);
        this.close();
       // Log.i(TAG, "/n delete blackList ");
    }

}



