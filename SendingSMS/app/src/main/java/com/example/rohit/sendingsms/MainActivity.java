package com.example.rohit.sendingsms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private TextView mTextViewLocation,mTextViewSMSNumber,mTextViewCallNumber;
    private Button mButtonSendDetails,mButtonResetDetails;
    BroadcastReceiver smsSentReceiver,smsReceivedReceiver;
    Double mLatitude,mLongitude;
    String mStrSMSNumber,mStrCallNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        mStrSMSNumber = intent.getStringExtra("sms_number");
        mStrCallNumber = intent.getStringExtra("call_number");
        mTextViewLocation = (TextView) findViewById(R.id.mainLocation);
        mTextViewCallNumber = (TextView) findViewById(R.id.mainTextViewCallNumber);
        mTextViewSMSNumber = (TextView) findViewById(R.id.mainTextViewSMSNumber);


        mTextViewSMSNumber.setText("SMS will be sent to: " + mStrSMSNumber);
        mTextViewCallNumber.setText("Call : " + mStrCallNumber);

        mButtonSendDetails = (Button) findViewById(R.id.mainButtonSendDetails);

        mButtonSendDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
                if (gpsTracker.canGetLocation() == false) {
                    gpsTracker.showLocationSettingDialoge();
                } else {
                    double lat = gpsTracker.getLatitude();
                    mLatitude = lat;
                    double longitude = gpsTracker.getLongitude();
                    mLongitude = longitude;
                    mTextViewLocation.setText("Latitude: " + String.valueOf(lat) + "Longitude: " + String.valueOf(longitude));
                    mTextViewLocation.setVisibility(View.INVISIBLE);
                    sendSMS();

                    Intent intent = new Intent(Intent.ACTION_CALL);
                  // intent.setData(Uri.parse("tel:" + mStrCallNumber));
                    intent.setData(Uri.parse("tel:" + Uri.encode(mStrCallNumber.trim())));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        });

        mButtonResetDetails = (Button) findViewById(R.id.mainButtonGoBack);
        mButtonResetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();
        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()){
                    case RESULT_OK:
                        Toast.makeText(MainActivity.this,"Message Sent",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(MainActivity.this,"RESULT_ERROR_GENERIC_FAILURE",Toast.LENGTH_SHORT).show();

                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainActivity.this,"RESULT_ERROR_NO_SERVICE",Toast.LENGTH_SHORT).show();

                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(MainActivity.this,"RESULT_ERROR_NULL_PDU",Toast.LENGTH_SHORT).show();

                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(MainActivity.this,"RESULT_ERROR_RADIO_OFF",Toast.LENGTH_SHORT).show();

                        break;
                }

            }
        };

        smsReceivedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()){
                    case RESULT_OK:
                        Toast.makeText(MainActivity.this,"SMS delivered",Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(MainActivity.this,"SMS not delivered",Toast.LENGTH_SHORT).show();
                        break;


                }

            }
        };

        registerReceiver(smsSentReceiver,new IntentFilter("SENT"));
        registerReceiver(smsReceivedReceiver, new IntentFilter("RECEIVE"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsReceivedReceiver);
        unregisterReceiver(smsSentReceiver);
    }

    /**
     * send Sms message
     */
    private void sendSMS(){
        PendingIntent piSMSSENT = PendingIntent.getBroadcast(MainActivity.this,0,new Intent("SENT"),0);
        PendingIntent piSMSRECEIVED = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent("RECEIVE"), 0);
        SmsManager smsManager = SmsManager.getDefault();
        StringBuffer smsBody = new StringBuffer();
        smsBody.append("Message body \n");
        smsBody.append("http://maps.google.com?q=");
        smsBody.append(mLatitude);
        smsBody.append(",");
        smsBody.append(mLongitude);
        smsManager.sendTextMessage("0"+mStrSMSNumber,null,smsBody.toString(),piSMSSENT,piSMSRECEIVED);
    }
}
