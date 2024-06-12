package com.example.smssenderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.SmsManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private EditText messageText;

    private Spinner simSpinner;

    private ArrayList<String> ls = new ArrayList<>();
    private Button sendButton;


    private int simInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumber = findViewById(R.id.phoneNumber); // Reference to EditText for phone number
        messageText = findViewById(R.id.messageText);    // Reference to EditText for message
        simSpinner = findViewById(R.id.simSpinner);
        sendButton = findViewById(R.id.sendBtn);

        SubscriptionManager subscriptionManager = SubscriptionManager.from(this.getApplicationContext());

        // Get the list of active subscription information (i.e., SIM cards)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();

        for (int i = 0; i < subscriptionInfoList.size(); i++) {
            ls.add(subscriptionInfoList.get(i).getCarrierName().toString());
        }
        simSpinner.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,ls));

        simSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                simInt = _param3;
            }

            @Override
            public void onNothingSelected(AdapterView<?> _param1) {

            }
        });


        sendButton.setOnClickListener(v -> {
            String phone = phoneNumber.getText().toString().trim();
            String message = messageText.getText().toString().trim();

            try {
                // Check if phone number and message are not empty
                if (!phone.isEmpty() && !message.isEmpty()) {

                    // Choose the SIM card to send the SMS
                    // For example, to use the first SIM card in the list:
                        int subscriptionId = subscriptionInfoList.get(simInt).getSubscriptionId();
                        SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(subscriptionId);
                        smsManager.sendTextMessage(phone, null, message, null, null); // Send SMS
                        Toast.makeText(MainActivity.this, "SMS Sent!", Toast.LENGTH_SHORT).show(); // Show success message
                } else {
                    Toast.makeText(MainActivity.this, "Please enter phone number and message", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                Toast.makeText(this, "SMS Failed to Send", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},10);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},11);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 10){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "ခွင့်ပြုချက် ရပြီးပါပြီ။!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "ခွင့်ပြုချက် မရသေးပါ။", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if(requestCode == 11){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "ခွင့်ပြုချက် ရပြီးပါပြီ။!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "ခွင့်ပြုချက် မရသေးပါ။", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


}