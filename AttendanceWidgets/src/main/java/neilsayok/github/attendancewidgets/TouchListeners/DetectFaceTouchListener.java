package neilsayok.github.attendancewidgets.TouchListeners;

import android.os.Handler;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class DetectFaceTouchListener implements View.OnTouchListener {

    private ViewGroup rootLayout;
    private long startTime,timeDiff;
    private Handler handler;
    private int _xDelta,_yDelta,X,Y,start_X,start_Y;
    private View view;


    public DetectFaceTouchListener(ViewGroup rootLayout) {
        this.rootLayout = rootLayout;
        handler = new Handler();
        startTime = 0;
        timeDiff = 0;
        _xDelta=0;
        _yDelta=0;
        X=0;
        Y=0;
        start_X=0;
        start_Y = 0;

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            timeDiff = System.currentTimeMillis() - startTime;
            if(timeDiff % 150 == 0){
                start_X = X;
                start_Y = Y;
            }

            if (X == start_X && Y == start_Y && timeDiff == 500){
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                view.performLongClick();
            }

            handler.postDelayed(this, 0);


        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        X = (int) event.getRawX();
        Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                view = v;
                start_X = X;
                start_Y = Y;
                handler.postDelayed(runnable, 0);
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;

                break;
            case MotionEvent.ACTION_UP:
                if (timeDiff <= 100){
                    v.performClick();
                }
                handler.removeCallbacks(runnable);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                handler.removeCallbacks(runnable);
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                v.setLayoutParams(layoutParams);
                break;


        }
        rootLayout.invalidate();
        return true;
    }





}
