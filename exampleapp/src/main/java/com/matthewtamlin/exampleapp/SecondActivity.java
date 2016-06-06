/*
 * Copyright 2016 Matthew Tamlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matthewtamlin.exampleapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {
	@Override
	@SuppressWarnings("ConstantConditions")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);

		// When pressed, allow the introduction screen to show again
		findViewById(R.id.clear_shared_prefs_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					allowIntroductionToShowAgain();
				}
			});
	}

	/**
	 * Clears the shared preferences flag which prevents the introduction from being shown twice.
	 */
	private void allowIntroductionToShowAgain() {
		final SharedPreferences sp = getSharedPreferences(ExampleActivity.DISPLAY_ONCE_PREFS,
				MODE_PRIVATE);
		sp.edit().putBoolean(ExampleActivity.DISPLAY_ONCE_KEY, false).apply();
	}
}