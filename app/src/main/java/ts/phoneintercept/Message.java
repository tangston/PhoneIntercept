package ts.phoneintercept;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class Message extends AppCompatActivity {
    TextView phone;
    TextView message;
    TextView time;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        phone=(TextView)findViewById(R.id.phone);
        message=(TextView)findViewById(R.id.message);
        time=(TextView)findViewById(R.id.time) ;
        Intent intent=getIntent();
        String Time=intent.getStringExtra("time");
        String Phone=intent.getStringExtra("phone");
        String Message=intent.getStringExtra("message");
        time.setText(Time);
        phone.setText(Phone);
        message.setText(Message);

    }
}
