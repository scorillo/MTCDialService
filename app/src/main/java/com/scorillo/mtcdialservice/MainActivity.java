package com.scorillo.mtcdialservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.RemoteException;
import android.telephony.PhoneNumberUtils;
import android.widget.Toast;

public class MainActivity extends BTServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onServiceConnected() {
        final String number = extractPhoneNumber(getIntent());

        if (number != null && !number.isEmpty()) {
            try {
                mBluetoothServiceInterface.dialOut(number);
            } catch (RemoteException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }

       MainActivity .this.finish();
    }

    String extractPhoneNumber(Intent intent) {
        Uri uri = intent.getData();

        if(uri != null && uri.getScheme().matches("tel|sms|smsto|mms|mmsto")) {
            return PhoneNumberUtils.normalizeNumber(uri.getSchemeSpecificPart());
        } else {
            return null;
        }
    }

    @Override
    public void onServiceDisconnected() {

    }
}
