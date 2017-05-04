package com.csci448.agwa.draganddraw;

import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

public class DragAndDrawActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return DragAndDrawFragment.newInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceManager.mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        ServiceManager.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ServiceManager.mDisplay = ServiceManager.mWindowManager.getDefaultDisplay();
    }
}
