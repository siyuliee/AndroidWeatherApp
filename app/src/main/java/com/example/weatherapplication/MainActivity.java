package com.example.weatherapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Siyu driving now
public class MainActivity extends AppCompatActivity {

    private Button button;
    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        button= findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpThread http = new HttpThread(editText.getText().toString().replace(" ","+"));
                http.start();
            }
        });
    }

    public Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    HashMap<String, List<String>> hashmap = (HashMap<String, List<String>>) msg.obj;

//                    show the result of location and weather on the first page of the app
//                    String final_result = hashmap.get("location") + "\n" + hashmap.get("weather");
//                    textView.setText(final_result);

                    String final_lat = hashmap.get("location").get(0);
                    String final_log = hashmap.get("location").get(1);
                    String wea_temperature = hashmap.get("weather").get(0);
                    String wea_humidity = hashmap.get("weather").get(1);
                    String wea_windspeed = hashmap.get("weather").get(2);
                    String wea_precipintensity = hashmap.get("weather").get(3);
                    String wea_precipprob = hashmap.get("weather").get(4);
                    String wea_summary=hashmap.get("weather").get(5);

                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);

                    ArrayList<String> location_meg= new ArrayList<>();
                    location_meg.add(final_lat);
                    location_meg.add(final_log);
                    ArrayList<String> weather_meg = new ArrayList<>();
                    weather_meg.add(wea_temperature);
                    weather_meg.add(wea_humidity);
                    weather_meg.add(wea_windspeed);
                    weather_meg.add(wea_precipintensity);
                    weather_meg.add(wea_precipprob);
                    weather_meg.add(wea_summary);

                    intent.putStringArrayListExtra("location",location_meg);
                    intent.putStringArrayListExtra("weather",weather_meg);

                    String address = editText.getText().toString();
                    intent.putExtra("address",address);

                    startActivity(intent);
                    break;
            }
        }
    };   //end of Siyu driving, Chengjing driving now.

    //create a new http thread
    protected class HttpThread extends Thread{
        private String address;

        public HttpThread(String address) {
            this.address=address;
        }

        public void run(){
            List<String> loc_result = Function.getLocation(address);
            List<String> wea_result= Function.getWeather(loc_result.get(0)+","+loc_result.get(1));

            HashMap<String,List<String>> hashMap= new HashMap<>();
            hashMap.put("location",loc_result);
            hashMap.put("weather",wea_result);

            Message msg = new Message();
            msg.what = 1;
            msg.obj = hashMap;
            handle.sendMessage(msg);
        }
    } //end og Chengjing driving

}
