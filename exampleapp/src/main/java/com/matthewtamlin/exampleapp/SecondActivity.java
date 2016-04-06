package com.matthewtamlin.exampleapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		findViewById(R.id.clear_shared_prefs).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.clear_shared_prefs) {
			final SharedPreferences sp =
					getSharedPreferences(ExampleActivity.DONT_DISPLAY_AGAIN_NAME, MODE_PRIVATE);
			sp.edit().putBoolean(ExampleActivity.DONT_DISPLAY_AGAIN_KEY, false).apply();
		}
	}
}
