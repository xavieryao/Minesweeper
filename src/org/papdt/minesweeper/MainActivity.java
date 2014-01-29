package org.papdt.minesweeper;

import org.papdt.minesweeper.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	private EditText mEtSideLength;
	private EditText mEtMineAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btnStart = (Button) findViewById(R.id.btn_start);
		btnStart.setOnClickListener(this);
		
		mEtSideLength=(EditText)findViewById(R.id.et_side_length);
		mEtMineAmount=(EditText)findViewById(R.id.et_mine_amount);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_start:
			int sideLength = Integer.parseInt(mEtSideLength.getText().toString());
			int mineAmount = Integer.parseInt(mEtMineAmount.getText().toString());
			if(sideLength<=0||mineAmount<=0||mineAmount>=sideLength*sideLength){
				Toast.makeText(this, "Illegal Argument!!", Toast.LENGTH_LONG).show();
				break;
			}
			Intent i = new Intent(MainActivity.this, GameActivity.class);
			i.putExtra("sideLength", sideLength);
			i.putExtra("mineAmount", mineAmount);
			startActivity(i);
			break;
		}
	}
}
