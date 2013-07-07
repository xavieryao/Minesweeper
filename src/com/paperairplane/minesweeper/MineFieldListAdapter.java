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

	static class ViewHolder {
		TextView tvMine;
	}

	@Override
	public int getCount() {
		return mMineField.getSize();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mCtx)
					.inflate(R.layout.mine, null);
			holder = new ViewHolder();
			holder.tvMine = (TextView) convertView.findViewById(R.id.tv_mine);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mMineField.isEmpty(position)) {
			holder.tvMine.setVisibility(View.INVISIBLE);
			holder.tvMine.setClickable(false);
		} else if (mMineField.isVisable(position)) {
			int num = mMineField.getNum(position);
			holder.tvMine
					.setText(Integer.toString(num));
			holder.tvMine.setBackgroundColor(0xFFFFFFFF);
			int color = 0xFF000000;
			switch(num){
			case 1:
				color = 0xFF000000;
				break;
			case 2:
				color = 0xFFFFEFD5;
				break;
			case 3:
				color = 0xFFDB7093;
				break;
			case 4:
				color = 0xFF87CEEB;
				break;
			case 5:
				color = 0xFFC71585;
				break;
			case 6:
				color = 0xFFFF4500;
				break;
			case 7:
				color = 0xFFFF00FF;
				break;
			case 8:
				color = 0xFFDC143C;
				break;
			}
			holder.tvMine.setTextColor(color);
			holder.tvMine.setClickable(false);
		}
		return convertView;
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

}
