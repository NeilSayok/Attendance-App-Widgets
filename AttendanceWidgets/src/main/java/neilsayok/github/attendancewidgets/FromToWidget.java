package neilsayok.github.attendancewidgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import neilsayok.github.attendancewidgets.TouchListeners.DetectFaceTouchListener;
import neilsayok.github.attendancewidgets.TouchListeners.FromToWidgetTouchListener;

import static neilsayok.github.attendancewidgets.DisplayConverters.DisplayMerticsConverters.dp;
import static neilsayok.github.attendancewidgets.DisplayConverters.DisplayMerticsConverters.sp;

public class FromToWidget extends View {

    float viewWidth;
    float viewHeight;
    float viewHeight10percent;
    float viewHeight90percent;
    float errorRadius;
    float textSize;
    float viewWidth25percent;
    float viewWidth75percent;
    float triangle_base;

    float xFrom,xStart_time,xTo,xEnd_time;

    String st_time;
    String en_time;


    int[] pt;
    float dp4;


    int baseColor;
    int textXPos,textYPos;



    final boolean debug = true;
    boolean Warning = true;

    ViewGroup parent;


    Paint paint;
    TextPaint from, to, start_time,end_time;
    Typeface typeface;
    DisplayMetrics displayMetrics;
    Drawable warn;
    Path triPathPos,triPathNeg,undSTime,unETime;





    public FromToWidget(Context context) {
        super(context);
        init(context,null);
    }

    public FromToWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public FromToWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,null);
    }

    public FromToWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,null);
    }

    private void init(Context context, @Nullable AttributeSet attrs){

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        from = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        to= new TextPaint(Paint.ANTI_ALIAS_FLAG);
        start_time= new TextPaint(Paint.ANTI_ALIAS_FLAG);
        end_time = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        st_time = "St Time";
        en_time = "En Time";

        triPathPos = new Path();
        triPathNeg = new Path();
        undSTime = new Path();
        unETime = new Path();

        dp4 = dp(getContext(),4);
        pt = new int[2];
        //displayMetrics = new DisplayMetrics();
        baseColor = Color.rgb(255,171,25);

        if (!isInEditMode())
            typeface = ResourcesCompat.getFont(getContext(),R.font.roboto_medium);
        else
            typeface = Typeface.create("Arial", Typeface.BOLD);


        this.setOnLongClickListener(longClickListener);

        displayMetrics = context.getResources().getDisplayMetrics();






    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = (int) (dp(getContext(),250) + getPaddingLeft() + getPaddingRight());
        int desiredHeight = (int) (dp(getContext(),100) + getPaddingTop() + getPaddingBottom());

        setMeasuredDimension(measureDimension(desiredWidth, widthMeasureSpec),
                measureDimension(desiredHeight, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDetails(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHeight = h;

        viewHeight10percent = h*0.10f;
        viewHeight90percent = h*0.90f;

        viewWidth25percent = w * 0.25f;
        viewWidth75percent = w * 0.75f;

        triangle_base = (viewHeight-viewHeight90percent);

        errorRadius = h*0.20f;

        if (errorRadius >= dp(getContext(),200)*0.20f){
            errorRadius = dp(getContext(),200)*0.20f;
        }

        textSize = w/h * 9;
        if (textSize >= 20.0f){
            textSize = 20.0f;
        }


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        parent = (ViewGroup) this.getParent();
        this.setOnTouchListener(new FromToWidgetTouchListener(parent));
        parent.getLocationOnScreen(pt);
    }


    private int measureDimension(int desiredSize, int measureSpec){
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = desiredSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }


        return result;
    }

    private void displayPopup(Context context){
        PopupMenu popupMenu = new PopupMenu(context, this);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.detect_face_popup_menu,popupMenu.getMenu());
        popupMenu.show();
    }


    public void setWarning(boolean warning){
        this.Warning = warning;
        this.invalidate();
    }

    OnLongClickListener longClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (debug)
                Toast.makeText(getContext(),"Long Clicked",Toast.LENGTH_SHORT).show();
            displayPopup(getContext());
            return true;
        }
    };

    private void drawDetails(Canvas canvas){

        paint.setColor(Color.parseColor("#344955"));
        RectF rectF = new RectF(viewWidth/2,0,viewWidth/2 + viewHeight10percent,viewHeight10percent);
        canvas.drawArc(rectF,0,-180,true,paint);

        paint.setColor(baseColor);

        canvas.drawRect(0,viewHeight10percent/2,viewWidth,viewHeight90percent,paint);

        paint.setColor(Color.parseColor("#39B765"));
        paint.setStyle(Paint.Style.FILL);

        triPathPos.moveTo(viewWidth25percent,viewHeight90percent);
        triPathPos.lineTo(viewWidth25percent+(viewHeight-viewHeight90percent)/2,viewHeight);
        triPathPos.lineTo(viewWidth25percent+(viewHeight-viewHeight90percent),viewHeight90percent);
        canvas.drawPath(triPathPos,paint);

        paint.setColor(Color.parseColor("#D0021B"));
        paint.setStyle(Paint.Style.FILL);

        triPathNeg.moveTo(viewWidth75percent,viewHeight90percent);
        triPathNeg.lineTo(viewWidth75percent+triangle_base/2,viewHeight);
        triPathNeg.lineTo(viewWidth75percent+triangle_base,viewHeight90percent);
        canvas.drawPath(triPathNeg,paint);

        from.setTypeface(typeface);
        from.setColor(Color.WHITE);
        from.setTextSize(sp(getContext(),textSize));


        start_time.setTypeface(typeface);
        start_time.setColor(Color.WHITE);
        start_time.setTextSize(sp(getContext(),textSize));
        start_time.setFlags(TextPaint.UNDERLINE_TEXT_FLAG);
        start_time.setUnderlineText(true);
        xStart_time = from.measureText("From ");




        to.setTypeface(typeface);
        to.setColor(Color.WHITE);
        to.setTextSize(sp(getContext(),textSize));
        xTo = xStart_time +start_time.measureText(st_time+" ");


        end_time.setTypeface(typeface);
        end_time.setColor(Color.WHITE);
        end_time.setTextSize(sp(getContext(),textSize));
        end_time.setFlags(TextPaint.UNDERLINE_TEXT_FLAG);
        end_time.setUnderlineText(true);
        xEnd_time = xTo +to.measureText("TO ") ;


        xFrom = (viewWidth - (xEnd_time+end_time.measureText(en_time+" ")))/2;


        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(dp(getContext(),2));

        canvas.drawText("From",xFrom,viewHeight/2,from);
        canvas.drawText(st_time,xFrom+xStart_time,viewHeight/2,start_time);
        canvas.drawLine(xFrom+xStart_time,viewHeight/2+dp(getContext(),4),xFrom+xStart_time+start_time.measureText(st_time),viewHeight/2+dp(getContext(),4),paint);
        canvas.drawText("To",xFrom+xTo,viewHeight/2,to);
        canvas.drawText(en_time,xFrom+xEnd_time,viewHeight/2,end_time);
        canvas.drawLine(xFrom+xEnd_time,viewHeight/2+dp(getContext(),4),xFrom+xEnd_time+end_time.measureText(en_time),viewHeight/2+dp(getContext(),4),paint);




    }



}
