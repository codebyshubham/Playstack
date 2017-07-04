package com.design.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class IconTextView extends TextView {

	public IconTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
			
		
		Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/icon.ttf");
		this.setTypeface(myTypeface);
	}

}
