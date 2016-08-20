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

package com.matthewtamlin.sliding_intro_screen_manual_testing;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Tests ability to hide status bar. The status bar should be hidden when the button is pressed.
 */
public class TestHideStatusBar extends ThreePageTestBase {
	/**
	 * Used to identify this class during testing.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "[TestHideStatusBar]";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a layout to display the control buttons over the ViewPager
		LinearLayout controlButtonHolder = new LinearLayout(this);
		controlButtonHolder.setOrientation(LinearLayout.VERTICAL);
		getRootView().addView(controlButtonHolder);

		// Add a button for hiding the status bar to the control layout
		final Button hideStatusBar = new Button(this);
		controlButtonHolder.addView(hideStatusBar);
		hideStatusBar.setText("Hide status bar");
		hideStatusBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideStatusBar();
			}
		});

		// Add a button for show the status bar to the control layout
		final Button showStatusBar = new Button(this);
		controlButtonHolder.addView(showStatusBar);
		showStatusBar.setText("Show status bar");
		showStatusBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showStatusBar();
			}
		});
	}
}
