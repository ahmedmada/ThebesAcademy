package com.eaapps.thebesacademy.Utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.eaapps.thebesacademy.R;


public class EATextInputEditText extends android.support.v7.widget.AppCompatEditText {
    Context context;

    public EATextInputEditText(Context context) {
        super(context);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "hacan.ttf");
        setTypeface(typeface);
        init(context);
    }

    public EATextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "hacan.ttf");
        setTypeface(typeface);

        init(context);
    }

    public EATextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "hacan.ttf");
        setTypeface(typeface);

        init(context);
    }

    private void init(Context context) {
        getBackground().mutate().setColorFilter(ContextCompat.getColor(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
    }
}
