package ts.phoneintercept;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class BlackRecords extends AppCompatActivity {
    public MyBaseAdapter mAdapter;
    private ListView list;
    DataBase database;
    public static List<Peopleblack> stringList = new LinkedList<Peopleblack>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.black_records);
        init();

        mAdapter= new MyBaseAdapter();
        list.setAdapter(mAdapter);
        dataShow();
    }
    public void init(){
        list=(ListView)findViewById(R.id.list);
        database = new DataBase(getApplicationContext());
    }
    public void dataShow(){
        SQLiteDatabase db;
        stringList.clear();
        db=database.getReadableDatabase();
        Cursor cursor=db.query(DataBase.TABLE_phoneCallRecord,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            Peopleblack people1=new Peopleblack();
            people1.id=cursor.getString(0);
            people1.time=cursor.getString(1);
            people1.phone=cursor.getString(2);
            stringList.add(people1);
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
            View view =View.inflate(BlackRecords.this,R.layout.list_item,null);
            TextView item_id=(TextView)view.findViewById(R.id.item_id);
            TextView item_pho=(TextView)view.findViewById(R.id.item_phonenumber);
            TextView item_time=(TextView)view.findViewById(R.id.item_time) ;
            item_time.setText(stringList.get(position).time);
            item_id.setText(" id:"+stringList.get(position).id);
            item_pho.setText(" "+stringList.get(position).phone);
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
