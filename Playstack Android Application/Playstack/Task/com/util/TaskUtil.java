package com.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class TaskUtil {

	public static InputStream drawbleToInputStream(Drawable drawable) {
		return drawable == null ? null : bitmapToInputStream(drawbleToBitmap(drawable));
	}
	
	public static Bitmap drawbleToBitmap(Drawable drawable) {
		if(drawable == null)
			return null;
		
		BitmapDrawable bitDw = ((BitmapDrawable) drawable);
        return bitDw.getBitmap();
	}
	
	
	public static InputStream bitmapToInputStream(Bitmap bitmap) {
		if(bitmap == null)
			return null;
		
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos); 
		byte[] bitmapdata = bos.toByteArray();
	    return new ByteArrayInputStream(bitmapdata);
	}
	
}
