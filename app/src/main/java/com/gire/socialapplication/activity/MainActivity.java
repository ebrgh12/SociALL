package com.gire.socialapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gire.socialapplication.R;

public class MainActivity extends AppCompatActivity {

    EditText mobileNumber;
    Button triggerSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mobileNumber = (EditText) findViewById(R.id.number);
        triggerSms = (Button) findViewById(R.id.submit_button);

        triggerSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobileNumber.getText().toString().length() == 10){
                    String messageToSend = "0078495 enter the following otp to confirm your mobile number.";
                    SmsManager.getDefault().sendTextMessage(mobileNumber.getText().toString(), null, messageToSend, null,null);
                }
            }
        });
    }

}
