package com.example.rohit.sendingsms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditTextSMSNumber,mEditTextCallNumber;
    private Button mButtonSave;
    private String mStrCallNumber,mStrSMSNumber,mStrValidationMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEditTextCallNumber = (EditText) findViewById(R.id.loginEditTextEmagencyCallNumber);
        mEditTextSMSNumber = (EditText) findViewById(R.id.loginEditTextEmagencySMSNumber);
        mButtonSave = (Button) findViewById(R.id.loginButtonSaveDetails);

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 mStrSMSNumber = mEditTextSMSNumber.getText().toString();
                 mStrCallNumber = mEditTextCallNumber.getText().toString();
                if (isValidDetails() ==  false){
                    Toast.makeText(LoginActivity.this,mStrValidationMessage,Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("sms_number",mStrSMSNumber);
                    editor.putString("call_number",mStrCallNumber);
                    editor.commit();

                    intent.putExtra("sms_number", mStrSMSNumber);
                    intent.putExtra("call_number",mStrCallNumber);
                    finish();
                    startActivity(intent);
                }



            }
        });
    }

    private boolean isValidDetails(){
        if (mStrSMSNumber.length() <= 0){
            mStrValidationMessage = getString(R.string.empty_sms_number);
            return false;
        } else if (isIs_mob_number(mStrSMSNumber) == false){
            mStrValidationMessage = getString(R.string.invalid_sms_mobile_number);
            return false;
        } else if (mStrCallNumber.length() <= 0) {
            mStrValidationMessage = getString(R.string.empty_call_number);
            return false;
        }else if (isIs_mob_number(mStrCallNumber) == false){
            mStrValidationMessage = getString(R.string.invalid_call_mobile_number);
            return false;
        }

        return true;
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    public boolean isIs_mob_number(String phoneNumber) {
// mobile number should be 10 digit
        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matchr = pattern.matcher(phoneNumber.trim());
        if (matchr.matches()) {
            return true;
        }
        return false;
    }
}
