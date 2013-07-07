package com.paperairplane.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class GameActivity extends Activity implements OnItemClickListener {
	private GridView mGridView;
	private MineFieldListAdapter mAdapter;
	private TextView mTvMineCount;
	private MineField mMineField;
	private boolean mFirstClicked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		mTvMineCount = (TextView) findViewById(R.id.tv_remained_mine_count);

		Bundle extras = getIntent().getExtras();
		int sideLength = extras.getInt("sideLength");
		int mineAmount = extras.getInt("mineAmount");

		mTvMineCount.setText(Integer.toString(mineAmount));
		mMineField = new MineField(sideLength, mineAmount);
		mMineField.createMineField();
		mAdapter = new MineFieldListAdapter(mMineField, GameActivity.this);
		mGridView = (GridView) findViewById(R.id.grid_minefield);
		mGridView.setNumColumns(sideLength);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (mMineField.getNum(position) == BlockState.HAS_MINE) {
			if (mFirstClicked) {
				Toast.makeText(this, "You Lose!", Toast.LENGTH_LONG).show();
			}else{
				mMineField.createMineField(position);
				BaseAdapter adapter = (BaseAdapter) mGridView.getAdapter();
				adapter.notifyDataSetChanged();
				mFirstClicked = true;
			}
		} else {
			mMineField.makeVisible(position);
			mMineField.remark(position);
			BaseAdapter adapter = (BaseAdapter) mGridView.getAdapter();
			adapter.notifyDataSetChanged();
			mTvMineCount.setText(Integer.toString(mMineField
					.getRemainedMineCount()));
			if (mMineField.won()) {
				Toast.makeText(this, "You Win!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
