package com.example.weatherapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//Siyu is driving now
public class LoadActivity extends AppCompatActivity {
    private final int time = 5000;
    private boolean lag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_layout);
        Toast.makeText(LoadActivity.this , "wait 5s or skip!" , Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(lag){
                    Intent intent = new Intent(LoadActivity.this , MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        } , time);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(LoadActivity.this , MainActivity.class);
                startActivity(intent);
                lag = false;
            }
        });
    }
}  //end of Siyu driving
