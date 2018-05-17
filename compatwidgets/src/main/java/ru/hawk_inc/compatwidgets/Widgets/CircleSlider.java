package ru.hawk_inc.compatwidgets.Widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import ru.hawk_inc.compatwidgets.R;

/**
 * Created by Admin on 8/26/2017.
 */
public class CircleSlider extends View {

//  All main changing values
    private float mValue = 0, mMaxValue = 100, mMinValue = 0,
                width = 0, height = 0, radius = 0,
                circleX = 0, circleY = 0, mTextHeight = 0,
                dx = 0, dy = 0;
    private float mCurAngle = 0;
    boolean mDown, mEnabled;
    
//  Values, connected with color
    private int mColor, mDarkColor;
    private Paint mPaint, mDarkPaint, mCirclePaint, mTextPaint;
    
//  Values, connected with width
    private final float STROKE_WIDTH = 30, CIRCLE_CLICKED = 1.2f,
                        MAX_TEXT_HEIGHT = 125, CLICK_DELAY_MS = 200,
                        START_ANGLE = 135, RANGE_ANGLE = 270;
    private float CURRENT_RADIUS = STROKE_WIDTH / 2f;
    private double OFFSET = (2 - Math.sqrt(2)) / 4.;

//  Animator, animating events of click/unclick
    ValueAnimator mInAnimator, mOutAnimator;
    ListenerArray listeners;

    RectF arc;
    Rect text;

    public CircleSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context
                .getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CircleSlider, 0, 0);
        
        try {
            mMaxValue = a.getInt(R.styleable.CircleSlider_max_value, 100);
            mMinValue = a.getInt(R.styleable.CircleSlider_min_value, 0);
            mValue = Math.min(a.getInt(R.styleable.CircleSlider_value, 0), mMaxValue);
            mValue = Math.max(mValue, mMinValue);

            mEnabled = a.getBoolean(R.styleable.CircleSlider_enabled, true);
            mColor = a.getColor(R.styleable.CircleSlider_color,
                                getResources().getColor(R.color.colorAccent));
        } finally {
            a.recycle();
            init();
        }
    }

//  Method, initialising all values not to spoil constructor
    private void init(){
        mDarkColor = getResources().getColor(R.color.colorAccentDark);
        listeners = new ListenerArray();
        
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(STROKE_WIDTH);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(mColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mDarkPaint = new Paint();
        mDarkPaint.setColor(mDarkColor);
        mDarkPaint.setStyle(Paint.Style.STROKE);
        mDarkPaint.setStrokeCap(Paint.Cap.ROUND);
        mDarkPaint.setStrokeWidth(STROKE_WIDTH);

        mTextPaint = new Paint();
        mTextPaint.setColor(mColor);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextSize(mTextHeight);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mInAnimator = ValueAnimator.ofFloat(STROKE_WIDTH / 2f, STROKE_WIDTH * CIRCLE_CLICKED / 2f);
        mInAnimator.setInterpolator(new FastOutSlowInInterpolator());
        mInAnimator.setDuration((int)CLICK_DELAY_MS);
        mInAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                CURRENT_RADIUS = (float)valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        mOutAnimator = ValueAnimator.ofFloat(STROKE_WIDTH * CIRCLE_CLICKED / 2f, STROKE_WIDTH / 2f);
        mOutAnimator.setInterpolator(new FastOutSlowInInterpolator());
        mOutAnimator.setDuration((int)CLICK_DELAY_MS);
        mOutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                CURRENT_RADIUS = (float)valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        arc = new RectF();
        text = new Rect();

        update();
    }

//  Method, updating all values according to other changed values
    private void update(){
        String t = Math.round(mValue) + "";
        mTextHeight = Math.min(radius / (float) Math.sqrt(2), MAX_TEXT_HEIGHT);
        mTextPaint.setTextSize(mTextHeight);
        mTextPaint.getTextBounds(t, 0, t.length(), text);

        mCurAngle = RANGE_ANGLE * (mValue/Math.abs(mMaxValue - mMinValue));

        circleX = width/2 + (float)(radius * Math.cos((360 - START_ANGLE - mCurAngle) / 360 * 2. * Math.PI));
        circleY = height/2 - (float)(radius * Math.sin((360 - START_ANGLE - mCurAngle) / 360 * 2. * Math.PI));
        circleY += (float)(radius * OFFSET);
    }

//  Method, drawing everything, based on values
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        float start = START_ANGLE + mCurAngle, stop = RANGE_ANGLE - mCurAngle;

        canvas.translate(dx, dy);

        canvas.drawArc(arc, start, stop,false, mDarkPaint);
        canvas.drawArc(arc, START_ANGLE, mCurAngle, false, mPaint);
        
        canvas.drawCircle(circleX, circleY, CURRENT_RADIUS, mCirclePaint);
        canvas.drawText(Math.round(mValue)+ "", width / 2,
                        height / 2 + mTextHeight*0.3f + (float)(radius * OFFSET), mTextPaint);
    }

//  Method, handling touches
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mEnabled)
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:{
                    double x2 = Math.pow(event.getX() - circleX - dx, 2),
                            y2 = Math.pow(event.getY() - circleY - dy, 2),
                            r2 = Math.pow(STROKE_WIDTH * CIRCLE_CLICKED, 2);

                    if(x2 + y2 <= 4 * r2) {
                        mDown = true;
                        listeners.OnStartTrackingTouch(this);
                        mInAnimator.start();
                    }
                    else
                        break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if(!mDown) break;

                    float distX = event.getX() - width / 2 - dx, distY = event.getY() - height / 2 - dy;
                    float angle = (float) (Math.atan2(-distY, distX) / (Math.PI * 2.0f) * 360);
                    while (angle < 360 - START_ANGLE - RANGE_ANGLE) angle += 360;
                    float newValue = (360 - START_ANGLE - angle) / RANGE_ANGLE * Math.abs(mMaxValue - mMinValue);

                    newValue = Math.max(newValue, 0);
                    newValue = Math.min(newValue, Math.abs(mMaxValue - mMinValue));

                    if(Math.abs(newValue - mValue) < 0.5f * Math.abs(mMaxValue - mMinValue)) {
                        mValue = newValue;
                        listeners.OnValueChanged(this, mValue, true);
                        update();
                        invalidate();
                    }

                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    listeners.OnStopTrackingTouch(this);
                    mOutAnimator.start();
                    invalidate();
                }
            }
        return false;
    }

//  Method, changing values, according to changes size
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int xpad = getPaddingLeft() + getPaddingRight(),
            ypad = getPaddingTop() + getPaddingBottom();
        dx = getPaddingLeft();
        dy = getPaddingTop();

        double value = (2 + Math.sqrt(2)) / 4.;

        width = w - xpad;
        height = h - ypad;
        int min = (int)Math.min(width,
                (height - STROKE_WIDTH * CIRCLE_CLICKED) / value + STROKE_WIDTH * CIRCLE_CLICKED);

        radius = (min - (int)STROKE_WIDTH * CIRCLE_CLICKED) / 2f;
        arc.set(width/2 - radius, height/2 - radius + (float)(radius * OFFSET),
                width/2 + radius, height/2 + radius + (float)(radius * OFFSET));

        update();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int ypad = getPaddingTop() + getPaddingBottom();
        float width = getMeasuredWidth(), height = getMeasuredHeight();
        float size = Math.min(width, height);

        double value = (2 + Math.sqrt(2)) / 4.;
        height = size - STROKE_WIDTH * CIRCLE_CLICKED - ypad;
        height *= value;
        height += STROKE_WIDTH * CIRCLE_CLICKED + ypad;

        setMeasuredDimension((int)size, (int)(height));
    }

    //  Getters and setters
    public void setValue(float value){
        mValue = value;
        listeners.OnValueChanged(this, mValue, false);
        update();
        invalidate();
    }
    public void setMaxValue(float maxValue){
        mMaxValue = maxValue;
        update();
        invalidate();
    }
    public void setMinValue(float minValue){
        mMinValue = minValue;
        update();
        invalidate();
    }
    public void setColor(int color){
        mColor = color;
        mPaint.setColor(mColor);
        invalidate();
    }
    public void setEnabled(boolean enabled){
        mEnabled = enabled;
    }
    public void addListener(OnSliderChangeListener listener){ listeners.add(listener); }

    public float getValue(){
        return mValue;
    }
    public float getMaxValue(){
        return mMaxValue;
    }
    public float getMinValue(){
        return mMinValue;
    }
    public int getColor(){
        return mColor;
    }
    public boolean isEnabled(){
        return mEnabled;
    }

//  Lister, that user might use for something
    public interface OnSliderChangeListener{
        void OnValueChanged(CircleSlider slider, float newValue, boolean fromUser);

        void OnStartTrackingTouch(CircleSlider circleSlider);

        void OnStopTrackingTouch(CircleSlider circleSlider);
    }
    
//  Array of listeners, executing everything
    private class ListenerArray{
        ArrayList<OnSliderChangeListener> listeners;
        
        public ListenerArray(){
            listeners = new ArrayList<>();
        }
        
        public void add(OnSliderChangeListener newListener){
            listeners.add(newListener);
        }
        
        public void OnValueChanged(CircleSlider slider, float newValue, boolean fromUser){
            for(OnSliderChangeListener listener : listeners)
                if(listener != null)
                    listener.OnValueChanged(slider, newValue, fromUser);
        }

        public void OnStartTrackingTouch(CircleSlider slider){
            for(OnSliderChangeListener listener : listeners)
                if(listener != null)
                    listener.OnStartTrackingTouch(slider);
        }

        public void OnStopTrackingTouch(CircleSlider slider){
            for(OnSliderChangeListener listener : listeners)
                if(listener != null)
                    listener.OnStopTrackingTouch(slider);
        }
    }
}