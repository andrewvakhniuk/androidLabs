package com.example.andrew.lab5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;

/**
 * Created by andrew on 03.03.15.
 */
public class MultitouchView extends View {

    private static final int SIZE = 60;

    private SparseArray<PointF> mActivePointers;
    private Paint mPaint;
    private int[] colors = {Color.BLUE, Color.GREEN, Color.YELLOW,
            Color.BLACK, Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
            Color.LTGRAY, Color.YELLOW};

    private Paint textPaint;


    public MultitouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mActivePointers = new SparseArray<PointF>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // set painter color to a color you like
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // We have a new pointer. Lets add it to the list of pointers

                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = mActivePointers.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {

                //mActivePointers.remove(pointerId);
                break;
            }
        }
        invalidate();

        return true;
    }




    public double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw all pointers
        for (int size = mActivePointers.size(), i = 0; i < size; i++) {
            PointF point = mActivePointers.valueAt(i);
            if (point != null) {
                mPaint.setColor(colors[i % 9]);
                canvas.drawCircle(point.x, point.y, SIZE, mPaint);
            }
          if (mActivePointers.size() == 3) {
                double a = distance(mActivePointers.valueAt(0).x, mActivePointers.valueAt(0).y, mActivePointers.valueAt(1).x, mActivePointers.valueAt(1).y);
                double b = distance(mActivePointers.valueAt(1).x, mActivePointers.valueAt(1).y, mActivePointers.valueAt(2).x, mActivePointers.valueAt(2).y);
                double c = distance(mActivePointers.valueAt(0).x, mActivePointers.valueAt(0).y, mActivePointers.valueAt(2).x, mActivePointers.valueAt(2).y);
                double pi = 3.14159265359;

                double angleA = Math.acos((b * b + c * c - a * a) / (2.0 * b * c)) * (180.0 / pi);
                double angleB = Math.acos((a * a + c * c - b * b) / (2.0 * a * c)) * (180.0 / pi);
                double angleC = (180.0 - angleA - angleB);



              canvas.drawLine(mActivePointers.valueAt(0).x, mActivePointers.valueAt(0).y, mActivePointers.valueAt(1).x, mActivePointers.valueAt(1).y,mPaint);
              canvas.drawLine(mActivePointers.valueAt(1).x, mActivePointers.valueAt(1).y, mActivePointers.valueAt(2).x, mActivePointers.valueAt(2).y,mPaint);
              canvas.drawLine(mActivePointers.valueAt(2).x, mActivePointers.valueAt(2).y, mActivePointers.valueAt(0).x, mActivePointers.valueAt(0).y,mPaint);
              Toast.makeText(getContext(),"Angles " + (int) angleA + " " + (int) angleB + " " + (int) angleC,
                      Toast.LENGTH_SHORT).show();
              canvas.drawText("Angles " + (int) angleA + " " + (int) angleB + " " + (int) angleC, 20, 60, textPaint);
            }
        }
    }
}

