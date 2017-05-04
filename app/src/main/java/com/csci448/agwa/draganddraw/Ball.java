package com.csci448.agwa.draganddraw;

import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by amosgwa on 5/3/17.
 */

 public class Ball {
    private PointF pos;
    private Paint color;

    public Ball(PointF pos, Paint color) {
        this.pos = pos;
        this.color = color;
    }

    public PointF getPos() {
        return pos;
    }

    public void setPos(PointF pos) {
        this.pos = pos;
    }

    public Paint getColor() {
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
    }
}
