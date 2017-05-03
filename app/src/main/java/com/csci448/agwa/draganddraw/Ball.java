package com.csci448.agwa.draganddraw;

import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by amosgwa on 5/3/17.
 */

 public class Ball {
    private PointF x;
    private PointF y;
    private Paint color;

    public Ball(PointF x, PointF y) {
        this.x = x;
        this.y = y;
        color = new Paint();
    }

    public PointF getX() {
        return x;
    }

    public void setX(PointF x) {
        this.x = x;
    }

    public PointF getY() {
        return y;
    }

    public void setY(PointF y) {
        this.y = y;
    }

    public Paint getColor() {
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
    }
}
