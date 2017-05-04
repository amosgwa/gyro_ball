package com.csci448.agwa.draganddraw;

import android.content.Context;
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

    private Point maxSize;

    public float xPosition, mSensorX,xVelocity = 0.0f;
    public float yPosition, mSensorY,yVelocity = 0.0f;
    public float frameTime = 0.666f;

    // Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    // Used when inflating the view from XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Sensor mAccelerometer = ServiceManager.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        ServiceManager.mSensorManager.registerListener(this, mAccelerometer, ServiceManager.mSensorManager.SENSOR_DELAY_GAME);

        // Get the maximum size.
        ServiceManager.mDisplay.getSize(maxSize);

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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

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
       //Set sensor values as acceleration
        updateBall();
    }

    private void updateBall() {
        //Calculate new speed
        xVelocity += (mSensorX * frameTime);
        yVelocity += (mSensorY * frameTime);

        //Calc distance travelled in that time
        float xS = (xVelocity/2)*frameTime;
        float yS = (yVelocity/2)*frameTime;

        //Add to position negative due to sensor
        //readings being opposite to what we want!
        xPosition -= xS;
        yPosition -= yS;

        if (xPosition > maxSize.x) {
            xPosition = maxSize.x;
        } else if (xPosition < 0) {
            xPosition = 0;
        }
        if (yPosition > maxSize.y) {
            yPosition = maxSize.y;
        } else if (yPosition < 0) {
            yPosition = 0;
        }

        PointF new_pos = new PointF(xPosition, yPosition);
        mBall.setPos(new_pos);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        ServiceManager.mSensorManager.unregisterListener(this);
    }

    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        PointF current = new PointF(event.getX(), event.getY());
//        String action = "";
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                action = "ACTION_DOWN";
//                // Reset drawing state
//                count += 1;
//                mCurrentBox = new Box(current, count);
//                mBoxen.add(mCurrentBox);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                action = "ACTION_MOVE";
//                if (mCurrentBox != null) {
//                    mCurrentBox.setCurrent(current);
//                    invalidate();
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                action = "ACTION_UP";
//                mCurrentBox = null;
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                action = "ACTION_CANCEL";
//                mCurrentBox = null;
//                break;
//        }
//        Log.i(TAG, action + " at x=" + current.x +
//                ", y=" + current.y);
//        return true;
//    }
}
