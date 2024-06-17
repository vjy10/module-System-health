package com.carsenze;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.hardware.carsenze.ICarsenze;

public class Carsenze extends Activity {
    private static final String TAG = "Carsenze";
    private static final String ICARSENZE_AIDL_INTERFACE = "android.hardware.carsenze.ICarsenze/default";
    private static ICarsenze carsenzeAJ; // AIDL Java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStats();
            }
        });

        // Attempt to bind to the AIDL service
        IBinder binder = ServiceManager.getService(ICARSENZE_AIDL_INTERFACE);
        if (binder == null) {
            Log.e(TAG, "Getting " + ICARSENZE_AIDL_INTERFACE + " service daemon binder failed!");
        } else {
            carsenzeAJ = ICarsenze.Stub.asInterface(binder);
            if (carsenzeAJ == null) {
                Log.e(TAG, "Getting ICarsenze AIDL daemon interface failed!");
            } else {
                Log.d(TAG, "ICarsenze AIDL daemon interface is binded!");
            }
        }
//
        //
        ////
        //
        //
        /
        /
        // Update stats initially
        updateStats();
    }

    private void updateStats() {
        try {
            // Retrieve stats from AIDL service
            String cpuStats = carsenzeAJ.getCpuStats();
            String memoryStats = carsenzeAJ.getMemoryStats();
            String networkStats = carsenzeAJ.getNetworkStats();

            // Update TextViews with retrieved stats
            TextView cpuTextView = findViewById(R.id.cpu);
            cpuTextView.setText("CPU: " + cpuStats);

            TextView memoryTextView = findViewById(R.id.memory);
            memoryTextView.setText("Memory: " + memoryStats);

            TextView networkTextView = findViewById(R.id.network);
            networkTextView.setText("Network: " + networkStats);

        } catch (RemoteException e) {
            Log.e(TAG, "Error retrieving stats from ICarsenze AIDL service", e);
        }
    }
}
