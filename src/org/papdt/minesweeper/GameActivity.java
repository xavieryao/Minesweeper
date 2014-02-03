package org.papdt.minesweeper;

import org.papdt.minesweeper.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ToggleButton;

public class GameActivity extends Activity implements OnItemClickListener,
		OnTouchListener {
	private GridView mGridView;
	private MineFieldListAdapter mAdapter;
	private TextView mTvMineCount;
	private MineField mMineField;
	private ToggleButton mTgFlag;
	private boolean mFirstClicked;
	private DisplayUtilities mDu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		setupActionBar();

		mTvMineCount = (TextView) findViewById(R.id.tv_remained_mine_count);
		mTgFlag = (ToggleButton) findViewById(R.id.tb_flag);

		mDu = new DisplayUtilities(this);

		Bundle extras = getIntent().getExtras();
		int sideLength = extras.getInt("sideLength");
		int mineAmount = extras.getInt("mineAmount");

		mTvMineCount.setText(Integer.toString(mineAmount));

		mMineField = new MineField(sideLength, mineAmount);
		mMineField.createMineField();
		Log.i("GameActivity", mMineField.toString());

		mAdapter = new MineFieldListAdapter(mMineField, GameActivity.this);

		mGridView = (GridView) findViewById(R.id.grid_minefield);
		mGridView.setNumColumns(sideLength);
		mGridView.setAdapter(mAdapter);
		mGridView.setColumnWidth(mDu.getBlockLength(sideLength));
		mGridView.setOnItemClickListener(this);
		mGridView.setOnTouchListener(this);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		MenuItem item = menu.add(Menu.NONE, 0, 1, "Restart").setIcon(
				android.R.drawable.ic_menu_revert);
		if (Build.VERSION.SDK_INT > 10)
			item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			// Reset
			mMineField.createMineField();
			Log.i("GameActivity",mMineField.toString());
			mAdapter.notifyDataSetInvalidated();
			mTvMineCount.setText(Integer.toString(mMineField.getRemainedMineCount()));
			break;
		case android.R.id.home:
			finish();
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mMineField.flagged(position)) {
			mMineField.flag(position);
			mAdapter.notifyDataSetChanged();
			mTvMineCount.setText(Integer.toString(mMineField
					.getRemainedMineCount()));
			return;
		}

		if (mTgFlag.isChecked()) {
			mMineField.flag(position);
			mAdapter.notifyDataSetChanged();
			mFirstClicked = true;
			if (mMineField.won()) {
				win();
				return;
			}
			mTvMineCount.setText(Integer.toString(mMineField
					.getRemainedMineCount()));
			return;
		}

		if (mMineField.getNum(position) == BlockState.HAS_MINE) {
			if (mFirstClicked) {
				lose();
				return;
			} else {
				Log.w("GameActivity", "FirstClick and Boom!");
				mMineField.createMineField(position);
				mAdapter.notifyDataSetChanged();
			}
		}
		mMineField.makeVisible(position);
		mMineField.remark(position);
		mAdapter.notifyDataSetChanged();
		if (mMineField.won()) {
			win();
			return;
		}
		mTvMineCount
				.setText(Integer.toString(mMineField.getRemainedMineCount()));
		mFirstClicked = true;
	}

	private void win() {
		Toast.makeText(this, "You Win!", Toast.LENGTH_LONG).show();
		finish();
	}

	private void lose() {
		Toast.makeText(this, "You Lose!", Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		return false;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT > 10) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
}
