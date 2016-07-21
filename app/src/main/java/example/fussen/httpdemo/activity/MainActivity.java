package example.fussen.httpdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import example.fussen.httpdemo.R;
import example.fussen.httpdemo.netutils.NetworkUtils;
import example.fussen.httpdemo.netutils.RequestCallback;

public class MainActivity extends AppCompatActivity {

    private Button send;
    private TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        send = (Button) findViewById(R.id.fly_to_internet);
        data = (TextView) findViewById(R.id.json_data);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromNet();
            }
        });
    }

    private void getDataFromNet() {

        String url = "http://dev.bodyplus.cc:8088/api/users?do=smsCode";

        Map<String, String> params = new HashMap<>();

        params.put("smsType", "1");
        params.put("smsPhone", "18219200511");
        params.put("type", "2");

        NetworkUtils.getInstance().sendPostRequest(url, params, new RequestCallback() {
            @Override
            public void onSuccess(Object object) {

                Log.d("1008611", object.toString());
                data.setText(object.toString());
            }

            @Override
            public void onFail(String string) {
                Log.d("1008611", string);
                data.setText(string);
            }
        });
    }
}
