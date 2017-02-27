package com.github.keyurgolani.gameoflife;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by keyurgolani on 2/23/17.
 */

public class GridCanvasView extends View {

    Context c;
    public int width;
    public int height;
    private Bitmap mBitmap;
    Context context;
    private Paint mPaint;
    private float horizontalOverflow;
    private float verticalOverflow;
    int[] sizes = {100, 80, 50, 30};
    private int cellSize = sizes[0];
    LifeUtils currentLife = null;
    int colCount;
    int rowCount;


    public GridCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        c = context;

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);

    }

    protected void nextSize() {
        for(int i = 0; i < sizes.length; i++) {
            if(cellSize == sizes[i]) {
                cellSize = sizes[(i + 1) % sizes.length];
                break;
            }
        }
        Log.w("New Size", cellSize + "");
        currentLife = null;
        invalidate();
    }

    protected void drawGrid(int cellSize, int height, int width, Canvas canvas) {
        colCount = width / cellSize;
        rowCount = height / cellSize;

        verticalOverflow = height - (rowCount * cellSize);
        horizontalOverflow = width - (colCount * cellSize);

        // Drawing vertical lines
        for(int i = 0; i <= colCount; i++) {
            canvas.drawLine(i * cellSize + horizontalOverflow / 2, verticalOverflow / 2, i * cellSize + horizontalOverflow / 2, rowCount * cellSize + verticalOverflow / 2, mPaint);
        }

        // Drawing horizontal lines
        for(int i = 0; i <= rowCount; i++) {
            canvas.drawLine(horizontalOverflow / 2, i * cellSize + verticalOverflow / 2, colCount * cellSize + horizontalOverflow / 2, i * cellSize + verticalOverflow / 2, mPaint);
        }

        if(currentLife == null) {
            currentLife = new LifeUtils(colCount, rowCount);
        }

    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                int row =(int) ((y - verticalOverflow / 2) / cellSize);
                int col = (int) ((x - horizontalOverflow / 2) / cellSize);
                try {
                    currentLife.toggleLife(row, col);
                } catch(ArrayIndexOutOfBoundsException e) {
                    Log.w("Touch On Overflow", "[" + row + ", " + col + "]");
                }
                invalidate();
                break;
        }
        return true;
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGrid(cellSize, findViewById(R.id.grid_canvas_view).getHeight(), findViewById(R.id.grid_canvas_view).getWidth(), canvas);

        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.RED);

        for(int i = 0; i < currentLife.getHeight(); i++) {
            for(int j = 0; j < currentLife.getWidth(); j++) {
                if(currentLife.isAlive(j, i)) {
                    canvas.drawCircle((i * cellSize) + horizontalOverflow / 2 + cellSize / 2, (j * cellSize) + verticalOverflow / 2 + cellSize / 2, cellSize / 2 - 1, circlePaint);
                }
            }
        }
    }

    public void clearCanvas() {
        currentLife = new LifeUtils(colCount, rowCount);
        invalidate();
    }
}
