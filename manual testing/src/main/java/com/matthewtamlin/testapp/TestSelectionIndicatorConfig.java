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

package com.matthewtamlin.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.matthewtamlin.sliding_intro_screen_library.indicators.SelectionIndicator;

/**
 * Tests the configuration options for the page indicator.
 */
public class TestSelectionIndicatorConfig extends ThreePageTestBase {
	/**
	 * Used to identify this class during testing.
	 */
	private static final String TAG = "[TestPageIndicatorC...]";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a layout to display the control buttons over the ViewPager
		final LinearLayout controlButtonHolder = new LinearLayout(this);
		controlButtonHolder.setOrientation(LinearLayout.VERTICAL);
		getRootView().addView(controlButtonHolder);

		// Add the test buttons to the control layout
		controlButtonHolder.addView(createToggleVisibilityButton());
		controlButtonHolder.addView(createToggleAnimationsButton());
	}

	/**
	 * @return a Button which toggles the visibility of the SelectionIndicator
	 */
	private Button createToggleVisibilityButton() {
		final Button button = new Button(this);
		button.setText("Toggle indicator visibility");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final SelectionIndicator indicator = getProgressIndicator();
				indicator.setVisibility(!indicator.isVisible());
			}
		});

		return button;
	}

	/**
	 * @return a Button which toggles whether or not SelectionIndicator animations are enabled
	 */
	private Button createToggleAnimationsButton() {
		final Button button = new Button(this);
		button.setText("Toggle indicator animations");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				enableProgressIndicatorAnimations(!progressIndicatorAnimationsAreEnabled());
			}
		});

		return button;
	}
}