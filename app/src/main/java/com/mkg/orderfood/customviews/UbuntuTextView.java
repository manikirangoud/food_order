package com.mkg.orderfood.customviews;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;



public class UbuntuTextView extends androidx.appcompat.widget.AppCompatTextView {

    private static final String SCHEMA = "http://schemas.android.com/apk/res/android";

    public UbuntuTextView(Context context) {
        super(context);
        this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "ubuntu_regular.ttf"));
    }

    public UbuntuTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int textStyle = attrs.getAttributeIntValue(SCHEMA, "textStyle", Typeface.NORMAL);

        if (textStyle == 0) {
            this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "ubuntu_regular.ttf"));

        } else if (textStyle == 1) {
            this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "ubuntu_bold.ttf"));

        }

    }

    public UbuntuTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int textStyle = attrs.getAttributeIntValue(SCHEMA, "textStyle", Typeface.NORMAL);

        if (textStyle == 0) {
            this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "ubuntu_regular.ttf"));

        } else if (textStyle == 1) {
            this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "ubuntu_bold.ttf"));

        }
    }

}
