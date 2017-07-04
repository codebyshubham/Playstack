package com.design;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LogoView extends TextView{

	public LogoView(Context context, AttributeSet attrs) {
		super(context, attrs);		
		
		Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/logo.ttf");
	    this.setTypeface(myTypeface);
	}

}
