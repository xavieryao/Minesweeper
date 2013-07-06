package com.paperairplane.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class GameActivity extends Activity implements OnItemClickListener{
	private GridView mGridView;
	private MineFieldListAdapter mAdapter;
	private MineField mMineField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		mMineField = new MineField(7,6);
		mMineField.createMineField();
		mAdapter = new MineFieldListAdapter(mMineField,GameActivity.this);
		mGridView = (GridView) findViewById(R.id.grid_minefield);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		if(mMineField.getNum(position)==BlockState.HAS_MINE){
			Toast.makeText(this, "You Lose!",Toast.LENGTH_LONG).show();
		}else{
			mMineField.makeVisible(position);
			mMineField.remark(position);
			BaseAdapter adapter =(BaseAdapter)mGridView.getAdapter();
			adapter.notifyDataSetChanged();
			if(mMineField.won()){
				Toast.makeText(this, "You Win!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
