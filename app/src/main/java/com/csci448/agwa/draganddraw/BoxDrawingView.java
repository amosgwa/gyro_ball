package com.csci448.agwa.draganddraw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by amosgwa on 4/7/17.
 */

public class BoxDrawingView extends View implements SensorEventListener{
    private static final String TAG = "BoxDrawingView";

    private Ball red_hole;
    private Ball mBall;

    private float ball_radius = 50.0f;

    private Point maxSize = new Point();

    public float xPosition, mSensorX,xVelocity = 0.0f;
    public float yPosition, mSensorY,yVelocity = 0.0f;
    public float frameTime = 0.666f;

    public long mLastT;
    public float mLastLux;

    // Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    // Used when inflating the view from XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Sensor mAccelerometer = ServiceManager.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor mLight = ServiceManager.mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        ServiceManager.mSensorManager.registerListener(this, mAccelerometer, ServiceManager.mSensorManager.SENSOR_DELAY_GAME);
        ServiceManager.mSensorManager.registerListener(this, mLight, ServiceManager.mSensorManager.SENSOR_DELAY_GAME);

        // Get the maximum size.
        ServiceManager.mDisplay.getSize(maxSize);

        mLastT = System.currentTimeMillis();
        mLastLux = 0;

        // Create holes
        Paint hole_paint = new Paint();
        hole_paint.setStyle(Paint.Style.STROKE);
        hole_paint.setStrokeWidth(10.0f);
        hole_paint.setARGB(255, 157, 191, 159);

        PointF point = new PointF(500.0f, 500.0f);
        red_hole = new Ball(point, hole_paint);

        // The moving ball style.
        Paint ball_paint = new Paint();
        ball_paint.setStyle(Paint.Style.FILL);
        ball_paint.setARGB(255, 17, 145, 170);

        point = new PointF(120.0f, 120.0f);
        mBall = new Ball(point, ball_paint);
    }

    /**
     * We are constantly rerendering the view with the new updated position and color of the ball.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // Fill the background
        Paint background_paint = new Paint();
        background_paint.setARGB(255, 255, 255, 235);
        canvas.drawPaint(background_paint);

        // Draw the hole.
        canvas.drawCircle(red_hole.getPos().x, red_hole.getPos().y, ball_radius*2, red_hole.getColor());
        // Draw the ball.
        canvas.drawCircle(mBall.getPos().x, mBall.getPos().y, ball_radius, mBall.getColor());

        invalidate();
    }

    /**
     * Responsible for detecting the accelerometer and light changes.
     * The ball should stay in the boundary of the view.
     * The Lux is set to 0 - 100. Depending on the range, it changes very distinctive colors.
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            switch (ServiceManager.mDisplay.getRotation()) {
                case Surface.ROTATION_0:
                    mSensorX = event.values[0];
                    mSensorY = event.values[1];
                    break;
                case Surface.ROTATION_90:
                    mSensorX = -event.values[1];
                    mSensorY = event.values[0];
                    break;
                case Surface.ROTATION_180:
                    mSensorX = -event.values[0];
                    mSensorY = -event.values[1];
                    break;
                case Surface.ROTATION_270:
                    mSensorX = event.values[1];
                    mSensorY = -event.values[0];
                    break;
            }
            updateBall();

        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT){
            float currentLux = event.values[0];

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);

            if(currentLux > 100) {// Max Lux is 100.
                currentLux = 100;
            }

            if( 0 <= currentLux && currentLux < 25 ) {
                paint.setARGB(255, 60, 63, 65);
            } else if ( 25 <= currentLux && currentLux < 50 ) {
                paint.setARGB(255, 157, 191, 159);
            } else if ( 50 <= currentLux && currentLux < 75 ) {
                paint.setARGB(255, 233, 109, 31);
            } else {
                paint.setARGB(255, 197, 63, 38);
            }

            mBall.setColor(paint);

            //Set sensor values as acceleration
            updateBall();
        }

    }

    /**
     * This is basically the physics of the ball.
     * It records the previous velocity and time. So then, it can calculate the new distance
     * based on the acceleration from the accelerometer.
     */
    private void updateBall() {

        final float sx = mSensorX;
        final float sy = mSensorY;

        final float ax = -sx/5;
        final float ay = sy/5;
        final long now_t = System.currentTimeMillis();
        final float dT = (float) (now_t - mLastT) / 15.f;

        xPosition += xVelocity * dT + ax * dT * dT / 2;
        yPosition += yVelocity * dT + ay * dT * dT / 2;

        xVelocity += ax * dT;
        yVelocity += ay * dT;

        mLastT = now_t;

        if (xPosition > maxSize.x) {
            xPosition = maxSize.x;
            xVelocity = 0;
        } else if (xPosition < 0) {
            xPosition = 0;
            xVelocity = 0;
        }

        if (yPosition > maxSize.y-250) {
            yPosition = maxSize.y-250;
            yVelocity = 0;
        } else if (yPosition < 0) {
            yPosition = 0;
            yVelocity = 0;
        }

        PointF new_pos = new PointF(xPosition, yPosition);
        mBall.setPos(new_pos);

        checkWin();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Make sure that the sensors are unregistered after the game.
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        ServiceManager.mSensorManager.unregisterListener(this);
    }

    /**
     * Make sure that the game checks if the ball is in the hole.
     * This should be ran when the sensor change is detected. If
     * this is checked in the onDraw it will look like it's crashed.
     * Actually, the view stops rendering and it loses its intent.
     */
    public void checkWin(){
        float hole_x = red_hole.getPos().x;
        float hole_y = red_hole.getPos().y;
        float ball_x = mBall.getPos().x;
        float ball_y = mBall.getPos().y;

        double distance = Math.pow(hole_x - ball_x, 2) + Math.pow(ball_y - hole_y, 2);
        distance = Math.pow(distance, 0.5);

        if(distance < ball_radius) {
            Intent intent = new Intent(this.getContext(), gameover.class);
            this.getContext().startActivity(intent);
            ServiceManager.mSensorManager.unregisterListener(this);
        }
    }
}
