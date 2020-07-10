package com.scorillo.mtcdialservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.microntek.mtcser.BTServiceInf;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

/**
 * Created by COMPUTER on 2017-02-17.
 */

public abstract class BTServiceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Intent startBluetoothServiceIntent = new Intent();
        startBluetoothServiceIntent.setComponent(new ComponentName("android.microntek.mtcser", "android.microntek.mtcser.BlueToothService"));
        if(!bindService(startBluetoothServiceIntent, mServiceConnection, BIND_AUTO_CREATE)) {
            Toast.makeText(this, this.getText(R.string.BluetoothNotAvailable), Toast.LENGTH_LONG).show();
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    protected abstract void onServiceConnected();
    protected abstract void onServiceDisconnected();

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if(iBinder == null) {
                Toast.makeText(BTServiceActivity.this, BTServiceActivity.this.getText(R.string.BluetoothNotAvailable), Toast.LENGTH_LONG).show();
                BTServiceActivity.this.finish();
                return;
            }

            mBluetoothServiceInterface = BTServiceInf.Stub.asInterface(iBinder);

            if(mBluetoothServiceInterface == null) {
                Toast.makeText(BTServiceActivity.this, BTServiceActivity.this.getText(R.string.BluetoothNotAvailable), Toast.LENGTH_LONG).show();
                BTServiceActivity.this.finish();
                return;
            }

            try {
                mBluetoothServiceInterface.init();
                BTServiceActivity.this.onServiceConnected();
            } catch (RemoteException e) {
                e.printStackTrace();
                Toast.makeText(BTServiceActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                BTServiceActivity.this.finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothServiceInterface = null;
            BTServiceActivity.this.onServiceDisconnected();
        }
    };

    protected BTServiceInf mBluetoothServiceInterface;
}
