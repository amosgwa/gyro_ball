package com.csci448.agwa.draganddraw;

import android.content.Intent;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

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

    public void gameOver() {
        Intent intent = new Intent(this, gameover.class);
        startActivity(intent);
    }
}
