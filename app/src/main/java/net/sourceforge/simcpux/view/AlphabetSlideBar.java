package net.sourceforge.simcpux.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import net.sourceforge.simcpux.log.L;

public class AlphabetSlideBar extends View {
    private static final String TAG = "AlphabetSlideBar";
    private Paint paint;
    private char[] alphabets = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '#'};
    private int drawWidth;
    private int drawHeight;
    private OnSlideTouchLisenter onSlideTouchLisenter;

    public AlphabetSlideBar(Context context) {
        super(context);
        init(context);
    }

    public AlphabetSlideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics()));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        drawWidth = getMeasuredWidth() / 2;
        drawHeight = getMeasuredHeight() / alphabets.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < alphabets.length; i++) {
            canvas.drawText(String.valueOf(alphabets[i]), drawWidth, drawHeight + drawHeight * i, paint);
        }
    }

    public interface OnSlideTouchLisenter {
        void onSlideTouchDown();

        void onSlideTouchMove(int section);

        void onSlideTouchUp();
    }

    public void setOnSlideTouchLisenter(OnSlideTouchLisenter onSlideTouchLisenter) {
        this.onSlideTouchLisenter = onSlideTouchLisenter;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(Color.GRAY);
                if (onSlideTouchLisenter != null) {
                    onSlideTouchLisenter.onSlideTouchDown();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int eventY = (int) event.getY();
                int index = eventY / drawHeight;
                //index>=0 && index<=alphabets.length-1
                index = Math.min(Math.max(0, index), alphabets.length - 1);
                L.i(TAG, "alphabet index: " + alphabets[index]);
                if (onSlideTouchLisenter != null) {
                    onSlideTouchLisenter.onSlideTouchMove(index);
                }
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);
                if (onSlideTouchLisenter != null) {
                    onSlideTouchLisenter.onSlideTouchUp();
                }
                break;
        }
        return true;
    }
}
