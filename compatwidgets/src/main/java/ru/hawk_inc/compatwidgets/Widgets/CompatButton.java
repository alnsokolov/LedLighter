package ru.hawk_inc.compatwidgets.Widgets;

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
 * Created by Admin on 1/12/2018.
 */

public class CompatButton extends View {

    private float V_OFFSET = 0, H_OFFSET = 0,
                  MAX_RADIUS = 100, MAX_TEXT_HEIGHT = 50, STROKE_WIDTH = 2,
                  dx = 0, dy = 0;

    private float width = 0, height = 0, radius;
    private RectF mRectangle;

    private boolean mDown = false, mToggle = false,
                    mOn = false, mEnabled;

    private int mColor, mTextColor;
    private Paint mPaint, mOffPaint, mTextOffPaint, mTextOnPaint;
    private float mTextHeight = 0;
    ListenerArray listeners;

    public CompatButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context
                .getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CompatButton, 0, 0);
        try {
            mEnabled = a.getBoolean(R.styleable.CompatButton_button_enabled, true);
            mToggle = a.getBoolean(R.styleable.CompatButton_button_toggle, false);
            mColor = a.getColor(R.styleable.CompatButton_button_color,
                                getResources().getColor(R.color.colorAccent));
            mTextColor = a.getColor(R.styleable.CompatButton_text_color, Color.WHITE);
        } finally {
            a.recycle();
            init();
        }
    }

    private void init() {
        mRectangle = new RectF();
        listeners = new ListenerArray();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextHeight);

        mOffPaint = new Paint();
        mOffPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mOffPaint.setColor(mColor);
        mOffPaint.setStrokeWidth(STROKE_WIDTH);

        mTextOffPaint = new Paint();
        mTextOffPaint.setColor(mTextColor);
        mTextOffPaint.setStyle(Paint.Style.STROKE);
        mTextOffPaint.setTextAlign(Paint.Align.CENTER);
        mTextOffPaint.setTextSize(mTextHeight);
        
        mTextOnPaint = new Paint();
        mTextOnPaint.setStyle(Paint.Style.STROKE);
        mTextOnPaint.setColor(mColor);
        mTextOnPaint.setTextAlign(Paint.Align.CENTER);
        mTextOnPaint.setTextSize(mTextHeight);

        update();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(dx, dy);

        if(!mToggle && !mDown || mToggle && !mOn) {
            canvas.drawRoundRect(mRectangle, radius, radius, mPaint);
            canvas.drawText("OFF",mRectangle.centerX(),
                    mRectangle.centerY() + mTextHeight * 0.3f, mTextOnPaint);
        }
        else {
            canvas.drawRoundRect(mRectangle, radius, radius, mOffPaint);
            canvas.drawText("ON",mRectangle.centerX(),
                    mRectangle.centerY() + mTextHeight * 0.3f, mTextOffPaint);
        }

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - dx, y = event.getY() - dy;
        
        boolean rect = (mRectangle.left) <= x && (mRectangle.top + radius) <= y &&
                (mRectangle.right) >= x && (mRectangle.bottom - radius) >= y;
        rect |= (mRectangle.left + radius) <= x && (mRectangle.top) <= y &&
                (mRectangle.right - radius) >= x && (mRectangle.bottom) >= y;

        boolean circle = isInCircle(mRectangle.left + radius, mRectangle.top + radius, radius, x, y);
        circle |= isInCircle(mRectangle.left + radius, mRectangle.bottom - radius, radius, x, y);
        circle |= isInCircle(mRectangle.right - radius, mRectangle.top + radius, radius, x, y);
        circle |= isInCircle(mRectangle.right - radius, mRectangle.bottom - radius, radius, x, y);

        if((rect || circle) && mEnabled) switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                listeners.OnClick(this, true);

                mDown = true;
                invalidate();
                return true;
            case  MotionEvent.ACTION_UP:
                listeners.OnClick(this, mToggle && !mOn);

                mDown = false;
                if(mToggle)
                    mOn = !mOn;
                invalidate();
                return true;
        }
        return false;
    }

    public boolean isInCircle(float centerX, float centerY, float radius, float x, float y){
        float distX = (float)Math.pow(centerX - x, 2), distY = (float)Math.pow(centerY - y, 2),
                r2 = radius * radius;

        return distX + distY <= r2;
    }

    public void update(){
        float   top = V_OFFSET + STROKE_WIDTH / 2.f,                left = H_OFFSET + STROKE_WIDTH / 2.f,
                bottom = height - V_OFFSET - STROKE_WIDTH / 2.f,    right = width - H_OFFSET - STROKE_WIDTH / 2.f;

        mTextHeight = Math.min((height - V_OFFSET*2) * 0.6f, MAX_TEXT_HEIGHT);
        mTextOnPaint.setTextSize(mTextHeight);
        mTextOffPaint.setTextSize(mTextHeight);

        mRectangle.set(left, top, right, bottom);
        radius = Math.min(Math.min(bottom - top, right - left) / 2f, MAX_RADIUS);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int xpad = getPaddingLeft() + getPaddingRight(),
            ypad = getPaddingTop() + getPaddingBottom();
        dx = getPaddingLeft();
        dy = getPaddingTop();

        width = w - xpad;
        height = h - ypad;

        setMeasuredDimension((int)width, (int)height);

        update();
    }

    @Override
    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }
    public void setColor(int color) {
        mColor = color;
        mPaint.setColor(color);
        mTextOnPaint.setColor(color);
        mOffPaint.setColor(color);

        invalidate();
    }
    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mTextOffPaint.setColor(textColor);
        invalidate();
    }
    public void setToggled(boolean toggle) {
        mToggle = toggle;
    }
    public void setV_OFFSET(float v_OFFSET) {
        V_OFFSET = v_OFFSET;
        invalidate();
    }
    public void setH_OFFSET(float h_OFFSET) {
        H_OFFSET = h_OFFSET;
        invalidate();
    }
    public void addListener(OnCompatButtonClickListener listener){
        listeners.add(listener);
    }

    @Override
    public boolean isEnabled() {
        return mEnabled;
    }
    public int getColor() {
        return mColor;
    }
    public int getTextColor() {
        return mTextColor;
    }
    public boolean isToggled() {
        return mToggle;
    }
    public float getV_OFFSET() {
        return V_OFFSET;
    }
    public float getH_OFFSET() {
        return H_OFFSET;
    }

    public interface OnCompatButtonClickListener{
        void OnClick(CompatButton button, boolean isOn);
    }

    private class ListenerArray{
        ArrayList<OnCompatButtonClickListener> listeners;

        public ListenerArray(){
            listeners = new ArrayList<>();
        }

        public void add(OnCompatButtonClickListener listener){
            listeners.add(listener);
        }

        public void OnClick(CompatButton button, boolean isOn){
            for(OnCompatButtonClickListener listener : listeners)
                if(listener != null)
                    listener.OnClick(button, isOn);
        }
    }
}
