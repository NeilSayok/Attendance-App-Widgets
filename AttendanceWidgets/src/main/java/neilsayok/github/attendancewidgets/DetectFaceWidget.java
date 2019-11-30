package neilsayok.github.attendancewidgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import neilsayok.github.attendancewidgets.TouchListeners.DetectFaceTouchListener;

import static neilsayok.github.attendancewidgets.DisplayConverters.DisplayMerticsConverters.dp;
import static neilsayok.github.attendancewidgets.DisplayConverters.DisplayMerticsConverters.dp2px;
import static neilsayok.github.attendancewidgets.DisplayConverters.DisplayMerticsConverters.sp;

public class DetectFaceWidget extends View {

    float viewWidth;
    float viewHeight;
    float viewHeight75percent;
    float viewHeight90percent;
    float borderRadius;
    float nodeRadius;
    float errorRadius;
    float textSize;

    int[] pt;



    final boolean debug = true;
    boolean Warning = true;

    ViewGroup parent;


    int baseColor;
    int textXPos,textYPos;

    Paint paint;
    Paint p;
    TextPaint textPaint;
    Typeface typeface;
    DisplayMetrics displayMetrics;
    Drawable d;





    public DetectFaceWidget(Context context) {
        super(context);
        init(context,null);
    }

    public DetectFaceWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);

    }

    public DetectFaceWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);

    }

    public DetectFaceWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = (int) (dp(getContext(),200) + getPaddingLeft() + getPaddingRight());
        int desiredHeight = (int) (dp(getContext(),100) + getPaddingTop() + getPaddingBottom());

        setMeasuredDimension(measureDimension(desiredWidth, widthMeasureSpec),
                measureDimension(desiredHeight, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDetails(canvas);    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHeight = h;
        viewHeight75percent = h*0.75f;
        viewHeight90percent = h*0.90f;
        nodeRadius = h*0.05f;


        errorRadius = h*0.20f;

        if (errorRadius >= dp(getContext(),200)*0.20f){
            errorRadius = dp(getContext(),200)*0.20f;
        }
        if (nodeRadius >= 20.0f){
            nodeRadius = 20.0f;
        }

        textSize = w/h * 9;
        if (textSize >= 20.0f){
            textSize = 20.0f;
        }

        this.invalidate();

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        parent = (ViewGroup) this.getParent();
        this.setOnTouchListener(new DetectFaceTouchListener(parent));
        parent.getLocationOnScreen(pt);
    }

    private void init(Context context,@Nullable AttributeSet attrs){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        p = new Paint(Paint.ANTI_ALIAS_FLAG);

        //points = new int[4][2];
        pt = new int[2];
        //center = new int[2];

        //displayMetrics = new DisplayMetrics();

        borderRadius = dp(getContext(),8);
        baseColor = Color.rgb(92,177,214);

        textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);

        if (!isInEditMode())
            //typeface = Typeface.create("Arial", Typeface.BOLD);

            typeface = ResourcesCompat.getFont(getContext(),R.font.roboto_medium);
        else
            typeface = Typeface.create("Arial", Typeface.BOLD);
            //typeface = ResourcesCompat.getFont(getContext(),R.font.roboto);


        this.setOnLongClickListener(longClickListener);
        if(debug)
            Log.e("Parent","hello");

//        ((Activity)getContext().getApplicationContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        displayMetrics = context.getResources().getDisplayMetrics();




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

    private void drawDetails(Canvas canvas){
        //viewWidth = viewWidth - errorRadius/2;
        paint.setColor(baseColor);
        canvas.drawRect(0,0,viewWidth,viewHeight/3,paint);
        canvas.drawRoundRect(0.0f,0.0f,viewWidth, viewHeight75percent,borderRadius,borderRadius,paint);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10.0f);
        canvas.drawLine(viewWidth/2, viewHeight75percent,viewWidth/2,viewHeight90percent-15.0f,paint);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10.0f);
        canvas.drawCircle(viewWidth/2,viewHeight90percent,nodeRadius,p);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(sp(getContext(),textSize));
        textPaint.setTypeface(typeface);
        textPaint.setTextAlign(Paint.Align.CENTER);


        textXPos = (int) (viewWidth/2);

        textYPos = (int) ((viewHeight75percent/2) - ((textPaint.descent() + textPaint.ascent())/2));

        canvas.drawText("Detect Face",textXPos,textYPos,textPaint);


        if (Warning) {
            d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_report_problem_black_24dp, null);
            //d.setBounds(d.getBounds());
            d.setBounds((int) (viewWidth-errorRadius), (int) 0, (int) (viewWidth), (int) errorRadius);
            d.draw(canvas);
        }



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




}
