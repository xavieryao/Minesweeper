package com.paperairplane.minesweeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MineFieldListAdapter extends BaseAdapter {
	private MineField mMineField;
	private Context mCtx;

	public MineFieldListAdapter(MineField mf, Context ctx) {
		this.mMineField = mf;
		this.mCtx = ctx;
	}

	@Override
	public int getCount() {
		return mMineField.getSize();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		v = LayoutInflater.from(mCtx).inflate(R.layout.mine, null);
		TextView tvMine = (TextView) v.findViewById(R.id.tv_mine);
		if (mMineField.isEmpty(position)) {
			tvMine.setVisibility(View.INVISIBLE);
		} else if (mMineField.isVisable(position)) {
			tvMine.setText(Integer.toString(mMineField.getNum(position)));
			tvMine.setBackgroundColor(0xFFFFFFFF);
		}
		return v;
	}

}
