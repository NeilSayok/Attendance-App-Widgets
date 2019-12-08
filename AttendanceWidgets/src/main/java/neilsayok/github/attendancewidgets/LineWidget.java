package neilsayok.github.attendancewidgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


public class LineWidget extends View {

    int[] pt0, pt1,pt2,pt3;
    Path path;
    Paint paint;

    public void setPt0(int[] pt0) {
        this.pt0 = pt0;
        calcPt2Pt3();
    }

    public void setPt1(int[] pt1) {
        this.pt1 = pt1;
        calcPt2Pt3();
    }


    public void setPtAll(int[] pt0,int[] pt1) {
        this.pt1 = pt1;
        this.pt1 = pt1;
        calcPt2Pt3();
    }

    private void calcPt2Pt3(){
        pt2[0] = pt1[0];
        pt2[1] = pt0[1];
        pt3[0] = pt0[0];
        pt3[1] = pt1[1];
        invalidate();
    }

    public LineWidget(Context context) {
        super(context);
        init(context,null);
    }

    public LineWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public LineWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public LineWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        pt0 = new int[2];
        pt1 = new int[2];
        pt2 = new int[2];
        pt3 = new int[2];

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path.moveTo(pt0[0],pt0[1]);

        path.cubicTo(pt3[0],pt3[1],pt2[0],pt2[1],pt1[0],pt1[1]);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);

        canvas.drawPath(path,paint);

    }
}
