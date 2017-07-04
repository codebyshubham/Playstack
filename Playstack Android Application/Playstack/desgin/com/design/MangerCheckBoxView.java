package com.design;

import com.shubham.playstack.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CheckBox;

public class MangerCheckBoxView extends CheckBox{

	public MangerCheckBoxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
		setPadding(0, 0, (int) context.getResources().getDimension(R.dimen.setting_manager_padding), 0);
		setGravity(Gravity.CENTER);
	}

}
