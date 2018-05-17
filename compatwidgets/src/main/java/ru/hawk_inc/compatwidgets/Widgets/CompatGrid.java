package ru.hawk_inc.compatwidgets.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import ru.hawk_inc.compatwidgets.R;

/**
 * Created by Admin on 1/20/2018.
 */

public class CompatGrid extends View {
    private int rows, cols, mColor, BORDER_WIDTH;
    private float  width, height, dx, dy, size;

    private boolean mToggle, mEnabled;
    private ListenerArray listeners;

    int curRow = 0, curCol = 0;
    private Tile[][] tiles;

    public CompatGrid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context
                .getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CompatGrid, 0, 0);
        try {
            rows = a.getInt(R.styleable.CompatGrid_rows, 3);
            cols = a.getInt(R.styleable.CompatGrid_cols, 3);
            mColor = a.getColor(R.styleable.CompatGrid_grid_color,
                    getResources().getColor(R.color.colorAccent));
            mToggle = a.getBoolean(R.styleable.CompatGrid_grid_toggle, false);
            BORDER_WIDTH = a.getInt(R.styleable.CompatGrid_grid_border_width, 10);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init(){
        tiles = new Tile[rows][cols];
        listeners = new ListenerArray();

        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                tiles[i][j] = new Tile(size, BORDER_WIDTH, mColor, mToggle);
    }

    private void update(){
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++) {
                tiles[i][j].setSize(size);
                tiles[i][j].setColor(mColor);
                tiles[i][j].setBorderWidth(BORDER_WIDTH);
            }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(dx, dy);

        Paint paint = new Paint();
        paint.setStrokeWidth(BORDER_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mColor);

        canvas.drawRect(0,0,size * rows, size * cols, paint);

        for(int i = 0; i < rows; i++){
            canvas.save();
            for(int j = 0; j < cols; j++){
                Tile tile = tiles[i][j];
                canvas.drawRect(tile.getBounds(), tile.getPaint());
                canvas.translate(size, 0);
            }
            canvas.restore();
            canvas.translate(0, size);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(),
              y = event.getY();

        float col = ((x - dx) / size),
              row = ((y - dy) / size);

        if(mEnabled)
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:{

                    if(col <= cols && row <= rows && col >= 0 && row >= 0) {
                        Tile tile = tiles[(int) row][(int) col];
                        tile.onTouch(true);
                        curRow = (int)row;
                        curCol = (int)col;

                        listeners.OnClick(curCol, curRow, tile, true);

                        invalidate();
                        return true;
                    }
                    else {
                        listeners.OnClick((int)col, (int)row, null, true);
                        return false;
                    }
                }
                case MotionEvent.ACTION_UP: {
                    Tile tile = tiles[curRow][curCol];
                    tile.onTouch(false);
                    listeners.OnClick(curCol, curRow, tile, false);

                    invalidate();
                    return true;
                }
                default: {
                    if(!mToggle) {
                        if (curCol != (int) col || curRow != (int) row) {
                            tiles[curRow][curCol].onTouch(false);
                            listeners.OnClick(curCol, curRow, tiles[curRow][curCol], false);
                            invalidate();
                        }
                        if (col <= cols && row <= rows && col >= 0 && row >= 0) {
                            Tile tile = tiles[(int) row][(int) col];
                            tile.onTouch(true);

                            curRow = (int) row;
                            curCol = (int) col;

                            listeners.OnClick(curCol, curRow, tile, true);

                            invalidate();
                            return true;
                        }
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

        width = w - xpad - BORDER_WIDTH;
        height = h - ypad - BORDER_WIDTH;
        size = Math.min(width / 1.f / cols, height / 1.f / rows);

        dx = getPaddingLeft() + (width - size * cols + BORDER_WIDTH) / 2.f;
        dy = getPaddingTop() + (height - size * rows + BORDER_WIDTH) / 2.f;

        update();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int xpad = getPaddingLeft() + getPaddingRight(),
            ypad = getPaddingTop() + getPaddingBottom();
        float width = getMeasuredWidth(), height = getMeasuredHeight();
        float xSize = (width - xpad) / 1.f / cols, ySize = (height - ypad) / 1.f / rows;

        if(xSize > ySize)
            setMeasuredDimension((int)(ySize * cols) + ypad,(int)height + xpad);
        else
            setMeasuredDimension((int)width + ypad, (int)(xSize * rows) + xpad);
    }

    @Override
    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }
    public void setColor(int color) {
        mColor = color;
        update();
        invalidate();
    }
    public void setToggle(boolean toggle) {
        mToggle = toggle;
    }
    public void setBorderWidth(int borderWidth) {
        this.BORDER_WIDTH = borderWidth; update();
    }
    public void setCols(int cols) {
        this.cols = cols;
        init();
        update();
    }
    public void setRows(int rows) {
        this.rows = rows;
        init();
        update();
    }

    @Override
    public boolean isEnabled() {
        return mEnabled;
    }
    public int getColor() {
        return mColor;
    }
    public boolean isToggle() {
        return mToggle;
    }
    public int getBorderWidth(){
        return BORDER_WIDTH;
    }
    public int getCols() {
        return cols;
    }
    public int getRows() {
        return rows;
    }

    public void addListener(OnGridChangeListener listener){
        listeners.add(listener);
    }

    public class Tile{
        private RectF bounds;
        private Paint paint, offPaint;
        boolean isOn = false, isToggle = false, mPressed = false;

        public Tile(float size, float borderWidth, int tileColor, int borderColor){
            bounds = new RectF(0,0,size, size);

            paint = new Paint();
            paint.setColor(tileColor);
            paint.setStyle(Paint.Style.FILL);

            offPaint = new Paint();
            offPaint.setColor(borderColor);
            offPaint.setStyle(Paint.Style.STROKE);
            offPaint.setStrokeWidth(borderWidth);
        }
        public Tile(float size, float borderWidth, int borderColor){
            this(size, borderWidth, borderColor, borderColor);
        }
        public Tile(float size, float borderWidth, int borderColor, int tileColor, boolean isToggle){
            this(size, borderWidth, tileColor, borderColor);

            this.isToggle = isToggle;
        }
        public Tile(float size, float borderWidth, int borderColor, boolean isToggle){
            this(size, borderWidth, borderColor, borderColor);

            this.isToggle = isToggle;
        }

        public void onTouch(boolean press){
            if(press) {
                mPressed = true;
                isOn = !isToggle || isOn;
            } else {
                mPressed = false;
                isOn = isToggle && !isOn;
            }
        }

        public void setColor(int color){
            paint.setColor(color);
        }
        public void setOffColor(int color){
            offPaint.setColor(color);
        }
        public void setSize(float size){
            bounds.set(0, 0, size, size);
        }
        public void setBorderWidth(int width){
            paint.setStrokeWidth(width);
            offPaint.setStrokeWidth(width);
        }

        public RectF getBounds(){
            return bounds;
        }
        public Paint getPaint(){
            return isOn || mPressed ? paint : offPaint;
        }
        public boolean isOn(){ return isOn || mPressed; }
    }

    public interface OnGridChangeListener{
        void OnClick(int row, int column, Tile tile, boolean pressed);
    }

    private class ListenerArray{
        private ArrayList<OnGridChangeListener> listeners;

        public ListenerArray(){
            listeners = new ArrayList<>();
        }

        public void add(OnGridChangeListener listener){
            listeners.add(listener);
        }

        public void OnClick(int row, int column, Tile tile, boolean pressed){
            for(OnGridChangeListener listener : listeners)
                listener.OnClick(row, column, tile, pressed);
        }
    }
}