package com.example.personalproject.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.example.personalproject.R;

public class DashboardActivity extends Activity {
	private ImageButton mConnection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_activity);

		initializerVariables();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void initializerVariables() {
		mConnection = (ImageButton) findViewById(R.id.btn_connect);
		mConnection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// go to the next activity
				intentFuelPrice();
			}
		});
	}

	private void intentFuelPrice() {
		Intent intent = new Intent(this, FuelPriceActivity.class);
		startActivity(intent);
	}

}
