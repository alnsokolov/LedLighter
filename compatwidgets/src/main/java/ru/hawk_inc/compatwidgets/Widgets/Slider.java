package ru.hawk_inc.compatwidgets.Widgets;

import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import ru.hawk_inc.compatwidgets.R;

/**
 * Created by Admin on 1/28/2018.
 */

public class Slider extends View {
    private int mColor, mBackground, mOrientation;

    private float mValue, mMaxValue, dx = 0, dy = 0,
                    width  = 0, height = 0,
                    HEIGHT = 40, BORDER_WIDTH = 12, RECT_WIDTH = 100, RECT_BORDER_WIDTH = 2,
                    rectXCenter = 0, rectYCenter = 0,
                    offsetX = 0, offsetY = 0;

    private boolean mEnabled, mDown = false;

    private Paint mLineOn, mLineOff, mBorderPaint, mBackgroundPaint, mLinesPaint;
    private RectF rect;
    ListenerArray listeners;

    public Slider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context
                .getTheme()
                .obtainStyledAttributes(attrs, R.styleable.Slider, 0, 0);
        try{
            mColor = a.getColor(R.styleable.Slider_slider_color, getResources().getColor(R.color.colorAccent));
            mBackground = a.getColor(R.styleable.Slider_slider_background, Color.WHITE);
            mOrientation = a.getInt(R.styleable.Slider_orientation, 0);

            mValue = a.getInt(R.styleable.Slider_slider_value, 0);
            mMaxValue = a.getInt(R.styleable.Slider_slider_max_value, 100);

            mEnabled = a.getBoolean(R.styleable.Slider_slider_enabled, true);
        } finally {
            a.recycle();
            init();
        }
    }

    private void init() {
        if(mOrientation == 0)
            rect = new RectF(-RECT_WIDTH / 2.f, -HEIGHT / 2.f, RECT_WIDTH / 2.f, HEIGHT / 2.f);
        else
            rect = new RectF(-HEIGHT / 2.f, -RECT_WIDTH / 2.f, HEIGHT / 2.f, RECT_WIDTH / 2.f);

        listeners = new ListenerArray();
        mLineOn = new Paint();
        mLineOn.setStyle(Paint.Style.STROKE);
        mLineOn.setStrokeCap(Paint.Cap.ROUND);
        mLineOn.setStrokeWidth(BORDER_WIDTH);
        mLineOn.setColor(mColor);

        mLineOff = new Paint();
        mLineOff.setStyle(Paint.Style.STROKE);
        mLineOff.setStrokeCap(Paint.Cap.ROUND);
        mLineOff.setStrokeWidth(BORDER_WIDTH / 2.5f);
        mLineOff.setColor(Color.GRAY);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mBackground);

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mColor);
        mBorderPaint.setStrokeWidth(RECT_BORDER_WIDTH);

        mLinesPaint = new Paint();
        mLinesPaint.setStyle(Paint.Style.STROKE);
        mLinesPaint.setStrokeWidth(BORDER_WIDTH / 1.5f);
        mLinesPaint.setStrokeCap(Paint.Cap.ROUND);
        mLinesPaint.setColor(mColor);
    }

    private void update() {
        if(mOrientation == 0) {
            rectXCenter = (RECT_WIDTH + RECT_BORDER_WIDTH * 2.f) / 2.f
                    + (width - RECT_WIDTH - RECT_BORDER_WIDTH * 2.f) / mMaxValue * mValue;
            rectYCenter = height / 2;
        }
        else {
            rectXCenter = width / 2;
            rectYCenter = (RECT_WIDTH + RECT_BORDER_WIDTH * 2.f) / 2.f
                    + (height - RECT_WIDTH - RECT_BORDER_WIDTH * 2.f) / mMaxValue * mValue;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mOrientation == 0){
            canvas.translate(dx, dy);
            canvas.drawLine(BORDER_WIDTH / 2.f, 0, rectXCenter, 0, mLineOn);
            canvas.drawLine(rectXCenter, 0, width - BORDER_WIDTH / 4.f, 0, mLineOff);

            canvas.translate(rectXCenter, 0);
            canvas.drawRoundRect(rect, HEIGHT / 2.f, HEIGHT / 2.f, mBackgroundPaint);
            canvas.drawRoundRect(rect, HEIGHT / 2.f, HEIGHT / 2.f, mBorderPaint);

            canvas.drawLine(0, -HEIGHT / 6.f, 0, HEIGHT / 6.f, mLinesPaint);
            canvas.drawLine( -RECT_WIDTH / 8.f, -HEIGHT / 6.f,
                    -RECT_WIDTH / 8.f, HEIGHT / 6.f, mLinesPaint);
            canvas.drawLine( RECT_WIDTH / 8.f, -HEIGHT / 6.f,
                    RECT_WIDTH / 8.f, HEIGHT / 6.f, mLinesPaint);
        } else {
            canvas.translate(dx, dy);
            canvas.drawLine(0, BORDER_WIDTH / 2.f, 0, rectYCenter, mLineOn);
            canvas.drawLine(0, rectYCenter, 0, height - BORDER_WIDTH / 4.f, mLineOff);

            canvas.translate(0, rectYCenter);
            canvas.drawRoundRect(rect, HEIGHT / 2.f, HEIGHT / 2.f, mBackgroundPaint);
            canvas.drawRoundRect(rect, HEIGHT / 2.f, HEIGHT / 2.f, mBorderPaint);

            canvas.drawLine(-HEIGHT / 6.f, 0, HEIGHT / 6.f, 0, mLinesPaint);
            canvas.drawLine( -HEIGHT / 6.f, -RECT_WIDTH / 8.f,
                    HEIGHT / 6.f, -RECT_WIDTH / 8.f, mLinesPaint);
            canvas.drawLine( -HEIGHT / 6.f, RECT_WIDTH / 8.f,
                    HEIGHT / 6.f, RECT_WIDTH / 8.f, mLinesPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();

        if(mEnabled) switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                if(rect.contains(x - rectXCenter, y - rectYCenter)) {
                    mDown = true;
                    offsetX = x - rectXCenter;
                    offsetY = y - rectYCenter;
                    listeners.OnChange(this, mValue, true);
                    return true;
                }
                return false;
            }
            case MotionEvent.ACTION_MOVE:{
                if(mOrientation == 0 && mDown){
                    mValue = Math.min((x - offsetX - RECT_WIDTH / 2.f)
                            / (width - RECT_WIDTH) * mMaxValue, mMaxValue);
                    mValue = Math.max(0, mValue);
                    listeners.OnChange(this, mValue, true);
                    update();
                    invalidate();

                    return true;
                } else if(mDown){
                    mValue = Math.min((y - offsetY - RECT_WIDTH / 2.f)
                            / (width - RECT_WIDTH) * mMaxValue, mMaxValue);
                    mValue = Math.max(0, mValue);
                    update();
                    invalidate();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int xpad = getPaddingLeft() + getPaddingRight(),
                ypad = getPaddingTop() + getPaddingBottom();

        if(mOrientation == 0) {
            width = w - xpad;
            height = h - ypad;
            dx = getPaddingLeft();
            dy = (height) / 2.f + getPaddingTop();
        }
        else {
            width = w - xpad;
            height = h - ypad;
            dy = getPaddingTop();
            dx = (width) / 2.f + getPaddingLeft();
        }

        update();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int xpad = getPaddingLeft() + getPaddingRight(),
                ypad = getPaddingTop() + getPaddingBottom();
        float width = getMeasuredWidth(), height = getMeasuredHeight();

        if(mOrientation == 0)
            setMeasuredDimension((int)Math.max(width - xpad, RECT_WIDTH * 2) + xpad,
                    (int)Math.max(HEIGHT + RECT_BORDER_WIDTH * 2, height - ypad) + ypad);
        else
            setMeasuredDimension((int)Math.max(HEIGHT + RECT_BORDER_WIDTH * 2, width - xpad) + xpad,
                    (int)Math.max(height - ypad, RECT_WIDTH * 2) + ypad);
    }


    @Override
    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }
    public void setColor(int color) {
        mColor = color;

        mLineOn.setColor(color);
        mBorderPaint.setColor(color);
        mLinesPaint.setColor(color);

        invalidate();
    }
    public void setBackground(int background) {
        mBackground = background;

        mBorderPaint.setColor(background);

        invalidate();
    }
    public void setOrientation(int orientation) {
        mOrientation = orientation;
        init();
        invalidate();
    }
    public void setValue(float value) {
        mValue = value;
        listeners.OnChange(this, mValue, false);
        update();
        invalidate();
    }
    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
        update();
        invalidate();
    }
    public void addListener(OnSliderChangeListener listener){
        listeners.add(listener);
    }

    @Override
    public boolean isEnabled() {
        return mEnabled;
    }
    public int getColor() {
        return mColor;
    }
    public int getBackgroundColor() {
        return mBackground;
    }
    public int getOrientation() {
        return mOrientation;
    }
    public float getValue() {
        return mValue;
    }
    public float getMaxValue() {
        return mMaxValue;
    }

    public interface OnSliderChangeListener{
        void OnChange(Slider slide, float newValue, boolean fromUser);
    }

    private class ListenerArray{
        ArrayList<OnSliderChangeListener> listeners;

        public ListenerArray(){
            listeners = new ArrayList<>();
        }

        public void OnChange(Slider slide, float newValue, boolean fromUser){
            for(OnSliderChangeListener slider : listeners)
                if(slider != null)
                    slider.OnChange(slide, newValue, fromUser);
        }

        public void add(OnSliderChangeListener listener){
            listeners.add(listener);
        }
    }
}
