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

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.matthewtamlin.android_utilities_library.helpers.DimensionHelper;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the configuration options for the page indicator.
 */
public class TestDotIndicator extends AppCompatActivity {
	/**
	 * Used to identify this class during testing.
	 */
	private static final String TAG = "[TestPageIndicatorC...]";

	/**
	 * The value to use for the unselected dot diameter when the appearance is changed, measured in
	 * display-independent pixels.
	 */
	private static final int UNSELECTED_DIAMETER_DP = 3;

	/**
	 * The value to use for the selected dot diameter when the appearance is changed, measured in
	 * display-independent pixels.
	 */
	private static final int SELECTED_DIAMETER_DP = 10;

	/**
	 * The value to use for the unselected dot color when the appearance is changed, as an ARGB hex
	 * code.
	 */
	private static final int UNSELECTED_COLOR = Color.RED;

	/**
	 * The value to use for the selected dot color when the appearance is changed, as an ARGB hex
	 * code.
	 */
	private static final int SELECTED_COLOR = Color.YELLOW;

	/**
	 * The value to use for the spacing between dots when the appearance is changed, measured in
	 * display-independent pixels.
	 */
	private static final int SPACING_BETWEEN_DOTS_DP = 10;

	/**
	 * The value to use for the transition duration when the appearance is changed, measured in
	 * milliseconds.
	 */
	private static final int TRANSITION_DURATION = 2000;

	/**
	 * The maximum number of dots to display when increasing the size of the indicator.
	 */
	private static final int MAX_NUMBER_OF_DOTS = 5;

	/**
	 * The value to use for the unselected dot diameter when the appearance is changed, measured in
	 * pixels. This value must be derived from the DP value when a Context is available.
	 */
	private int unselectedDiameterPx;

	/**
	 * The value to use for the selected dot diameter when the appearance is changed, measured in
	 * pixels. This value must be derived from the DP value when a Context is available.
	 */
	private int selectedDiameterPx;

	/**
	 * The value to use for the spacing between dots when the appearance is changed, measured in
	 * pixels. This value must be derived from the DP value when a Context is available.
	 */
	private int spacingPx;

	/**
	 * The root View of the activity's layout.
	 */
	private LinearLayout rootView;

	/**
	 * The DotIndicator under test.
	 */
	private DotIndicator indicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_dot_indicator);
		rootView = (LinearLayout) findViewById(R.id.activity_test_dot_indicator_root);

		// Validate testing preconditions
		assertThat("the root view cannot be null", rootView, is(notNullValue()));

		// These values are needed before testing can begin
		initialisePxDimensions();

		// Create the buttons which control the test
		rootView.addView(createMoveForwardAnimatedButton());
		rootView.addView(createMoveForwardNotAnimatedButton());
		rootView.addView(createMoveBackwardAnimatedButton());
		rootView.addView(createMoveBackwardNotAnimatedButton());
		rootView.addView(createChangeAppearanceDpButton());
		rootView.addView(createChangeAppearancePxButton());
		rootView.addView(createGrowIndicatorButton());
		rootView.addView(createShrinkIndicatorButton());
		rootView.addView(createResetIndicatorButton());
	}

	/**
	 * Converts the static DP constants to PX, and assigns the results to member variables.
	 */
	private void initialisePxDimensions() {
		unselectedDiameterPx = DimensionHelper.dpToPx(TestDotIndicator.this,
				UNSELECTED_DIAMETER_DP);
		selectedDiameterPx = DimensionHelper.dpToPx(TestDotIndicator.this,
				SELECTED_DIAMETER_DP);
		spacingPx = DimensionHelper.dpToPx(TestDotIndicator.this,
				SPACING_BETWEEN_DOTS_DP);
	}

	/**
	 * @return a Button which makes the next dot active using an animation
	 */
	private Button createMoveForwardAnimatedButton() {
		final Button button = new Button(this);
		button.setText("Next dot (animated)");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (indicator.getSelectedItemIndex() < indicator.getNumberOfItems() - 1) {
					indicator.setSelectedItem(indicator.getSelectedItemIndex() + 1, true);
				}
			}
		});

		return button;
	}

	/**
	 * @return a Button which makes the next dot active without using an animation
	 */
	private Button createMoveForwardNotAnimatedButton() {
		final Button button = new Button(this);
		button.setText("Next dot (not animated)");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (indicator.getSelectedItemIndex() < indicator.getNumberOfItems() - 1) {
					indicator.setSelectedItem(indicator.getSelectedItemIndex() + 1, false);
				}
			}
		});

		return button;
	}

	/**
	 * @return a Button which makes the next dot active using an animation
	 */
	private Button createMoveBackwardAnimatedButton() {
		final Button button = new Button(this);
		button.setText("Previous dot (animated)");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (indicator.getSelectedItemIndex() > 0) {
					indicator.setSelectedItem(indicator.getSelectedItemIndex() - 1, true);
				}
			}
		});

		return button;
	}

	/**
	 * @return a Button which makes the next dot active without using an animation
	 */
	private Button createMoveBackwardNotAnimatedButton() {
		final Button button = new Button(this);
		button.setText("Previous dot (not animated)");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (indicator.getSelectedItemIndex() > 0) {
					indicator.setSelectedItem(indicator.getSelectedItemIndex() - 1, false);
				}
			}
		});

		return button;
	}

	/**
	 * @return a Button which changes the indicator appearance using DP specific methods
	 */
	private Button createChangeAppearanceDpButton() {
		final Button button = new Button(this);
		button.setText("Change indicator properties (DP)");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resetIndicator();

				indicator.setUnselectedDotDiameterDp(UNSELECTED_DIAMETER_DP);
				indicator.setSelectedDotDiameterDp(SELECTED_DIAMETER_DP);
				indicator.setUnselectedDotColor(UNSELECTED_COLOR);
				indicator.setSelectedDotColor(SELECTED_COLOR);
				indicator.setSpacingBetweenDotsDp(SPACING_BETWEEN_DOTS_DP);
				indicator.setTransitionDuration(TRANSITION_DURATION);

				validateAppearanceChanges();
			}
		});

		return button;
	}

	/**
	 * @return a Button which changes the indicator appearance using PX specific methods
	 */
	private Button createChangeAppearancePxButton() {
		final Button button = new Button(this);
		button.setText("Change indicator properties (px)");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resetIndicator();

				indicator.setUnselectedDotDiameterPx(unselectedDiameterPx);
				indicator.setSelectedDotDiameterPx(selectedDiameterPx);
				indicator.setUnselectedDotColor(UNSELECTED_COLOR);
				indicator.setSelectedDotColor(SELECTED_COLOR);
				indicator.setSpacingBetweenDotsPx(spacingPx);
				indicator.setTransitionDuration(TRANSITION_DURATION);

				validateAppearanceChanges();
			}
		});

		return button;
	}

	/**
	 * @return a Button which increases the number of dots
	 */
	private Button createGrowIndicatorButton() {
		final Button button = new Button(this);
		button.setText("Increase number of dots");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resetIndicator();

				if (indicator.getNumberOfItems() < MAX_NUMBER_OF_DOTS) {
					indicator.setNumberOfItems(indicator.getNumberOfItems() + 1);
				}
			}
		});

		return button;
	}

	/**
	 * @return a Button which decreases the number of dots
	 */
	private Button createShrinkIndicatorButton() {
		final Button button = new Button(this);
		button.setText("Decrease number of dots");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resetIndicator();

				if (indicator.getNumberOfItems() > 1) {
					indicator.setNumberOfItems(indicator.getNumberOfItems() - 1);
				}
			}
		});

		return button;
	}

	/**
	 * @return a Button which resets the indicator
	 */
	private Button createResetIndicatorButton() {
		final Button button = new Button(this);
		button.setText("Reset indicator");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resetIndicator();
			}
		});

		return button;
	}

	/**
	 * Validates the current state of the indicator to ensure that the appearance was changed
	 * correctly.
	 */
	private void validateAppearanceChanges() {
		// Check that indicator parameters match the member variables and constants
		assertThat("unselected diameter was not set/returned correctly",
				indicator.getUnselectedDotDiameter() == unselectedDiameterPx);
		assertThat("selected diameter was not set/returned correctly",
				indicator.getSelectedDotDiameter() == selectedDiameterPx);
		assertThat("unselected color was not set/returned correctly",
				indicator.getUnselectedDotColor() == UNSELECTED_COLOR);
		assertThat("selected color was not set/returned correctly",
				indicator.getSelectedDotColor() == SELECTED_COLOR);
		assertThat("spacing between dots was not set/returned correctly",
				indicator.getSpacingBetweenDots() == spacingPx);
		assertThat("transition duration was not set/returned correctly",
				indicator.getTransitionDuration() == TRANSITION_DURATION);
	}

	/**
	 * Replaces the existing indicator with a new Indicator. The values are set to their defaults.
	 */
	private void resetIndicator() {
		rootView.removeView(indicator);
		indicator = new DotIndicator(this);
		rootView.addView(indicator);

	}
}