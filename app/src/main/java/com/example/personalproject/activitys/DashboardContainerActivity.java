package com.example.personalproject.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.personalproject.R;
import com.example.personalproject.fragments.GasPriceFragment;

public class DashboardContainerActivity extends AppCompatActivity {
	//private static final String TAG = "TAG_EXCEPTION";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_container_activity);

		// Initializer and set the toolbar.
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


		// Set the principal fragment.
		Fragment fragment = new GasPriceFragment();

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.dashboard_container, fragment);
		fragmentTransaction.commit();
	}

//	/**
//	 * Initializer the variable.
//	 */
//
//	private void initializerVariables() {
//
//	}
}
