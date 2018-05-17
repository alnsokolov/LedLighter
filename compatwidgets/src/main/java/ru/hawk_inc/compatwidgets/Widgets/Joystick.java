package ru.hawk_inc.compatwidgets.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import ru.hawk_inc.compatwidgets.R;

/**
 * Created by Admin on 2/4/2018.
 */

public class Joystick extends View {
    private float mOuterWidth = 10, mOuterRadius = 0, mInnerRadius = 30,
                  dx = 0, dy = 0, circleX = 0, circleY = 0,
                  offsetX = 0, offsetY = 0;
    private float width = 0, height = 0;
    private boolean mEnabled, mDown = false, mSticky;

    private float minValue, maxValue;

    private ListenerArray listeners;

    private int mInnerColor, mOuterColor;
    private Paint mOuterPaint, mInnerPaint;

    public Joystick(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context
                .getTheme()
                .obtainStyledAttributes(attrs, R.styleable.Joystick, 0, 0);
        try{
            mInnerColor = a.getColor(R.styleable.Joystick_joystick_color,
                    getResources().getColor(R.color.colorAccent));
            mOuterColor = a.getColor(R.styleable.Joystick_joystick_outer_color, mInnerColor);
            mEnabled = a.getBoolean(R.styleable.Joystick_joystick_enabled, true);
            mSticky = a.getBoolean(R.styleable.Joystick_joystick_sticky, false);

            minValue = a.getInt(R.styleable.Joystick_min_joystick_value, 0);
            maxValue = a.getInt(R.styleable.Joystick_max_joystick_value, 100);
        } finally {
            a.recycle();
            init();
        }
    }

    private void init(){
        listeners = new ListenerArray();

        mInnerPaint = new Paint();
        mInnerPaint.setStyle(Paint.Style.FILL);
        mInnerPaint.setColor(mInnerColor);

        mOuterPaint = new Paint();
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setStrokeWidth(mOuterWidth);
        mOuterPaint.setColor(mOuterColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(dx, dy);
        canvas.drawCircle(width / 2.f, height / 2.f, mOuterRadius, mOuterPaint);
        canvas.drawCircle(circleX, circleY, mInnerRadius, mInnerPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() + offsetX,
              y = event.getY() + offsetY;

        if(mEnabled)
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:{
                    if(isInCircle(x, y, circleX, circleY, mInnerRadius)) {
                        offsetX = circleX - x;
                        offsetY = circleY - y;

                        float x1 = map(width / 2 - (mOuterRadius - mInnerRadius),
                                width  / 2 + (mOuterRadius - mInnerRadius), minValue, maxValue, circleX),
                            y1 = map(height / 2 - (mOuterRadius - mInnerRadius),
                                height / 2 + (mOuterRadius - mInnerRadius), minValue, maxValue, circleY);

                        listeners.OnJoystickTouch(x1, y1, true);

                        mDown = true;
                        return true;
                    }
                }
                case MotionEvent.ACTION_MOVE: {
                    boolean in = isInCircle(x, y, width / 2, height / 2,
                            mOuterRadius - mInnerRadius - mOuterWidth / 2);
                    if (mDown) {
                        if (in) {
                            circleX = x;
                            circleY = y;

                            invalidate();
                        } else {
                            double x2 = Math.pow(x - width / 2, 2),
                                   y2 = Math.pow(y - height / 2, 2);
                            double dist = Math.sqrt(x2 + y2);
                            circleX = width  / 2 + (x - width  / 2) / (float)dist * (mOuterRadius - mInnerRadius);
                            circleY = height / 2 + (y - height / 2) / (float)dist * (mOuterRadius - mInnerRadius);

                            invalidate();
                        }
                    }

                    float x1 = map(width / 2 - (mOuterRadius - mInnerRadius),
                            width  / 2 + (mOuterRadius - mInnerRadius), minValue, maxValue, circleX),
                        y1 = map(height / 2 - (mOuterRadius - mInnerRadius),
                            height / 2 + (mOuterRadius - mInnerRadius), minValue, maxValue, circleY);

                    listeners.OnJoystickTouch(x1, y1, in);

                    return true;
                }
                case MotionEvent.ACTION_UP:{
                    mDown = false;
                    if(!mSticky){
                        circleX = width / 2;
                        circleY = height / 2;
                        listeners.OnJoystickTouch((minValue+maxValue)/2, (minValue+maxValue)/2, true);
                    }

                    offsetX = 0;
                    offsetY = 0;

                    invalidate();
                }
            }

        return false;
    }

    public boolean isInCircle(float x, float y, float cx, float cy, float radius){
        double x2 = Math.pow(x - cx, 2),
               y2 = Math.pow(y - cy, 2),
               r2  = radius * radius;

        return x2 + y2 < r2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int xpad = getPaddingLeft() + getPaddingRight(),
            ypad = getPaddingTop() + getPaddingBottom();

        dx = getPaddingLeft();
        dy = getPaddingTop();

        mOuterRadius = Math.min(w - xpad - mOuterWidth, h - ypad - mOuterWidth) / 2;

        if(!mDown){
            circleX = (w - xpad) / 2;
            circleY = (h - ypad) / 2;
        } else if(mDown || mSticky) {
            circleX = circleX / width * (w - xpad);
            circleY = circleY / height * (h - ypad);
        }

        width = w - xpad;
        height = h - ypad;

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int ypad = getPaddingTop() + getPaddingBottom(),
            xpad = getPaddingLeft() + getPaddingRight();
        float width  = getMeasuredWidth(),
              height = getMeasuredHeight();

        if(width  - xpad - mOuterWidth * 2 < mInnerRadius * 4)
            width  = mInnerRadius * 2 + xpad;
        if(height - ypad - mOuterWidth * 2 < mInnerRadius * 4)
            height = mInnerRadius * 2 + ypad;

        setMeasuredDimension((int)width, (int)height);
    }

    public void setEnabled(boolean enabled){
        mEnabled = enabled;
    }
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        invalidate();
    }
    public void setMinValue(float minValue) {
        this.minValue = minValue;
        invalidate();
    }
    public void setColor(int innerColor) {
        mInnerColor = innerColor;
        mOuterColor = innerColor;
        mInnerPaint.setColor(mInnerColor);
        mOuterPaint.setColor(mOuterColor);
        invalidate();
    }
    public void setSticky(boolean sticky) {
        mSticky = sticky;
    }

    @Override
    public boolean isEnabled() {
        return mEnabled;
    }
    public float getMaxValue() {
        return maxValue;
    }
    public float getMinValue() {
        return minValue;
    }
    public int getColor() {
        return mInnerColor;
    }
    public boolean isSticky() {
        return mSticky;
    }

    public void addListener(OnJoystickTouchListener listener){
        listeners.add(listener);
    }


    public interface OnJoystickTouchListener{
        void OnJoystickTouch(float x, float y, boolean in);
    }

    private class ListenerArray{
        ArrayList<OnJoystickTouchListener> listeners;

        public ListenerArray(){
            listeners = new ArrayList<>();
        }

        public void OnJoystickTouch(float x, float y, boolean in){
            for(OnJoystickTouchListener listener : listeners)
                if(listener != null)
                    listener.OnJoystickTouch(x, y, in);
        }

        public void add(OnJoystickTouchListener listener){
            listeners.add(listener);
        }
    }

    private float map(float start1, float end1, float start2, float end2, float value){
        float dist1 = end1 - start1,
              dist2 = end2 - start2;
        value = start2 + (value - start1) / dist1 * dist2;
        return value;
    }
}
