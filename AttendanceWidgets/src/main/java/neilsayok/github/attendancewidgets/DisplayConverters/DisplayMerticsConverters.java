package neilsayok.github.attendancewidgets.DisplayConverters;

import android.content.Context;
import android.util.TypedValue;

public class DisplayMerticsConverters {

    public static float dp(Context context, float value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static float dp(Context context, int value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static float dp(Context context, double value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) value, context.getResources().getDisplayMetrics());
    }


    public static float sp(Context context, float value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,value , context.getResources().getDisplayMetrics());
    }

    public static float sp(Context context, int value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,value , context.getResources().getDisplayMetrics());
    }

    public static float sp(Context context, double value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (float) value, context.getResources().getDisplayMetrics());
    }

    public static float dp2px(Context context, int value){
        return value * context.getResources().getDisplayMetrics().density;
    }

    public static float dp2px(Context context, float value){
        return value * context.getResources().getDisplayMetrics().density;
    }

    public static float dp2px(Context context, double value){
        return (float) (value * context.getResources().getDisplayMetrics().density);
    }

}
