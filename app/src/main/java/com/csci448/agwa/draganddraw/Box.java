package com.csci448.agwa.draganddraw;

import android.graphics.PointF;

/**
 * Created by amosgwa on 4/7/17.
 */
public class Box {
    private PointF mOrigin;
    private PointF mCurrent;
    private int mId;

    public Box(PointF origin, int id) {
        mOrigin = origin;
        mCurrent = origin;
        this.mId = id;
    }
    public PointF getCurrent() {
        return mCurrent;
    }
    public void setCurrent(PointF current) {
        mCurrent = current;
    }
    public PointF getOrigin() {
        return mOrigin;
    }
    public int getId() {
        return mId;
    }
}
