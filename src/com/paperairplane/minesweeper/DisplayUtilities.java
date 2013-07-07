package com.paperairplane.minesweeper;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtilities {

	private Context mCtx;

	public DisplayUtilities(Context ctx) {
		mCtx = ctx;
	}

	public int getBlockLength(int sideLength) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		int length;
		length = dm.widthPixels/sideLength - 2;
		return length;
	}

}
