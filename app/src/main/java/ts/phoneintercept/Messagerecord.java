package ts.phoneintercept;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Messagerecord extends AppCompatActivity {
    public MyBaseAdapter mAdapter;
    private ListView list;
    DataBase database;
    public static List<MList> stringList = new LinkedList<MList>();
    public static List<MList> text=new LinkedList<MList>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_record);
        init();

        mAdapter= new MyBaseAdapter();
        list.setAdapter(mAdapter);
        dataShow();
    }
    public void init(){
        list=(ListView)findViewById(R.id.list);
        database = new DataBase(getApplicationContext());
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Messagerecord.this,Message.class);
                intent.putExtra("phone",stringList.get(position).phone);
                intent.putExtra("message",text.get(position).message);
                intent.putExtra("time",stringList.get(position).time);
                startActivity(intent);

            }
        });
    }
    public void dataShow(){
        SQLiteDatabase db;
        stringList.clear();
        db=database.getReadableDatabase();
        Cursor cursor=db.query(DataBase.TABLE_messageRecord,null,null,null,null,null,null);
       // Log.i("data","into datashow");

        while(cursor.moveToNext()){
            MList people1=new MList();
            MList people2=new MList();
            people1.id=cursor.getString(0);
            people1.time=cursor.getString(1);
            people1.phone=cursor.getString(2);
            people2.id=cursor.getString(0);
            people2.time=cursor.getString(1);
            people2.phone=cursor.getString(2);
            people2.message=cursor.getString(3);
           if(cursor.getString(3).length()>=10){
               people1.message=cursor.getString(3).substring(0,10);
           }else people1.message=cursor.getString(3);
            stringList.add(people1);
            text.add(people2);
        }

        cursor.close();
        db.close();
        mAdapter.notifyDataSetChanged();
    }
    public class MyBaseAdapter extends BaseAdapter {


//        public int getCount(){
//            return list1.size();
//        }
//        public Object getItem(int position){
//            return id[position];
//        }
//        public long getItemId(int position){
//            return position;
//        }
//        public View getView(int position,View converView,ViewGroup parent){
//            View view =View.inflate(BlackActivity.this,R.layout.list_item,null);
//            TextView item_id=(TextView)findViewById(R.id.item_id);
//            TextView item_pho=(TextView)findViewById(R.id.item_phonenumber);
//            item_id.setText(id[position]);
//            item_pho.setText(phonenumber[position]);
//            return view;
//        }



        public int getCount(){
            return stringList.size();
        }
        public Object getItem(int position){
            return null;
        }
        public long getItemId(int position){
            return 0;
        }
        public View getView(int position, View converView, ViewGroup parent){
            View view =View.inflate(Messagerecord.this,R.layout.list_item,null);
            TextView item_id=(TextView)view.findViewById(R.id.item_id);
            TextView item_pho=(TextView)view.findViewById(R.id.item_phonenumber);
            TextView item_time=(TextView)view.findViewById(R.id.item_time) ;
            TextView item_message=(TextView)view.findViewById(R.id.item_message);
            item_time.setText(stringList.get(position).time);
            item_pho.setText(" "+stringList.get(position).phone);
            item_message.setText(" "+stringList.get(position).message);
//            Log.i("aaaa1",list1.get(position).name);
//            Log.i("aaaa2",list1.get(position).phone);
            return view;
        }
        public  String getNowTime(){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            return simpleDateFormat.format(date);
        }
    }
}