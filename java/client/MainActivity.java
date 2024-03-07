package com.alexronnegi.client;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.client.R;

import java.io.IOException;
import java.io.InputStream;




public class MainActivity extends AppCompatActivity {
    TextView label;
    Button btn;
    Switch switchButton;
    Button btnUser1;
    Button btnUser2;
    Button btnUser3;
    Handler myHandler;
    CalculationThread CalThread;
    ThreadUsrs ThreadUsrs;
    int currentFileIndex = 1;
    String previousResult = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label = findViewById(R.id.label);
        btn = findViewById(R.id.btn);
        btnUser1 = findViewById(R.id.btnUser1);
        btnUser2 = findViewById(R.id.btnUser2);
        btnUser3 = findViewById(R.id.btnUser3);
        switchButton = findViewById(R.id.switchButton);


        btn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.customButtonColor));

        myHandler = new Handler(Looper.getMainLooper(),
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        String result = message.getData().getString("result");
                        previousResult = result; // Store the previous result
                        label.setText(result);
                        return true;
                    }
                });
        switchButton.setChecked(true); // Set the switch initially enabled
        enableUserButtons(); // Enable the user buttons

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableUserButtons(); // Enable the user buttons if the switch is checked (enabled)
                } else {
                    disableUserButtons(); // Disable the user buttons if the switch is unchecked (disabled)
                }
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFileIndex < 7) {
                    String arg = "route" + currentFileIndex + ".gpx";
                    InputStream stream = null;
                    try {
                        stream = getAssets().open(arg);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    currentFileIndex++;
                    CalThread = new CalculationThread(myHandler, stream);
                    CalThread.start();
                }
            }
        });

        btnUser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFileIndex < 7) {
                    String arg = "route" + currentFileIndex + ".gpx";
                    InputStream stream = null;
                    try {
                        stream = getAssets().open(arg);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ThreadUsrs = new ThreadUsrs(myHandler, stream);
                    if(ThreadUsrs.user.equals("user1")){
                        ThreadUsrs.start();
                    }



                }
            }
        });


    }

    private void enableUserButtons() {
        btnUser1.setEnabled(false);
        btnUser2.setEnabled(false);
        btnUser3.setEnabled(false);
    }

    private void disableUserButtons() {
        btnUser1.setEnabled(true);
        btnUser2.setEnabled(true);
        btnUser3.setEnabled(true);
    }
}
