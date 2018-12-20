package ts.phoneintercept;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class White extends AppCompatActivity implements View.OnClickListener {
    public MyBaseAdapter mAdapter;
    DataBase dataBase;
    private Button add;
    private Button delete;
    private EditText write;
    private TextView textblack;
    private ListView list;
    public static List<People> stringList = new LinkedList<People>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.white);
        init();
        mAdapter= new MyBaseAdapter();
        dataShow();
        list.setAdapter(mAdapter);
    }
    private void init(){
        add=(Button)findViewById(R.id.add);
        delete=(Button)findViewById(R.id.delete);
        write=(EditText)findViewById(R.id.write);

        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        dataBase = new DataBase(getApplicationContext());
        list=(ListView)findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                write.setText(stringList.get(position).phone);

            }
        });

    }
    public void onClick(View v){
        String phonenumber;
        SQLiteDatabase db;
        ContentValues values;
        switch (v.getId()){
            case R.id.add:
                phonenumber = write.getText().toString();
                db=dataBase.getWritableDatabase();
                values=new ContentValues();
                values.put(DataBase.KEY_phonenumber,phonenumber);
                db.insert(DataBase.TABLE_whiteList,null,values);
                Toast.makeText(this,"信息已添加",Toast.LENGTH_SHORT).show();
                db.close();
                dataShow();
                break;

            case R.id.delete:
                db=dataBase.getWritableDatabase();
                if(write.getText().toString().trim().equals(null))
                    Toast.makeText(this,"号码为空",Toast.LENGTH_SHORT);
                else
                    db.delete(DataBase.TABLE_whiteList,"phonenumber=?",new String[]{write.getText().toString()});
                Toast.makeText(this,"信息已删除",Toast.LENGTH_SHORT);

                db.close();
                dataShow();
                break;
        }
    }
    public void dataShow(){
        SQLiteDatabase db;
        stringList.clear();
        db=dataBase.getWritableDatabase();
        Cursor cursor=db.query(DataBase.TABLE_whiteList,null,null,null,null,null,null);
        while(cursor.moveToNext()){

            People people1=new People();
          people1.name=cursor.getString(0);
            people1.phone=cursor.getString(1);
            stringList.add(people1);
        }
        cursor.close();
        db.close();
        mAdapter.notifyDataSetChanged();
    }

    public class MyBaseAdapter extends BaseAdapter{


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
            return stringList.get(position);
        }
        public long getItemId(int position){
            return position;
        }
        public View getView(int position, View converView, ViewGroup parent){
            View view =View.inflate(White.this,R.layout.list_item,null);
            TextView item_id=(TextView)view.findViewById(R.id.item_id);
            TextView item_pho=(TextView)view.findViewById(R.id.item_phonenumber);
            item_id.setText("id:"+stringList.get(position).name);
            item_pho.setText("    phonenumber:"+stringList.get(position).phone);
//            Log.i("aaaa1",list1.get(position).name);
//            Log.i("aaaa2",list1.get(position).phone);
            return view;
        }
    }
}