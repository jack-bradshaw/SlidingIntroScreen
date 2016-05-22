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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.matthewtamlin.android_utilities_library.helpers.DimensionHelper;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;
import com.matthewtamlin.sliding_intro_screen_library.indicators.SelectionIndicator;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the configuration options for the page indicator.
 */
public class TestProgressIndicatorConfig extends ThreePageTestBase {
	/**
	 * Used to identify this class during testing.
	 */
	private static final String TAG = "[TestPageIndicatorC...]";

	/**
	 * Diameter to use for the unselected dots after triggering, measured in display-independent
	 * pixels.
	 */
	private static final int UNSELECTED_DIAMETER_DP = 3;

	/**
	 * Diameter to use for the selected dot after triggering, measured in display-independent
	 * pixels.
	 */
	private static final int SELECTED_DIAMETER_DP = 10;

	/**
	 * Color to use for the unselected dots after triggering.
	 */
	private static final int UNSELECTED_COLOR = Color.RED;

	/**
	 * Color to use for the selected dot after triggering.
	 */
	private static final int SELECTED_COLOR = Color.YELLOW;

	/**
	 * Spacing to use between dots after triggering, measured in display-independent pixels.
	 */
	private static final int SPACING_BETWEEN_DOTS_DP = 10;

	/**
	 * The duration to use when transitioning between selected and unselected, after triggering.
	 */
	private static final int TRANSITION_DURATION = 2000;

	int unselectedDiameterPx;
	int selectedDiameterPx;
	int spacingPx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initialisePxDimensions();

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		getRootView().addView(layout);

		Button showHideButton = new Button(this);
		layout.addView(showHideButton);
		showHideButton.setText("show/hide indicator");
		showHideButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [show/hide]");
				SelectionIndicator progressIndicator = getProgressIndicator();

				boolean initiallyHidden = progressIndicator.isVisible();
				progressIndicator.setVisibility(!initiallyHidden);
				assertThat("progress indicator did not hide/un-hide properly",
						progressIndicator.isVisible() == !initiallyHidden);

				Log.d(TAG, "[show/hide button] [assertion passed]");
			}
		});

		Button toggleAnimations = new Button(this);
		layout.addView(toggleAnimations);
		toggleAnimations.setText("toggle animations");
		toggleAnimations.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [toggle animations]");

				boolean initiallyEnabled = progressIndicatorAnimationsAreEnabled();
				enableProgressIndicatorAnimations(!initiallyEnabled);
				assertThat("animations did not enable/disable properly",
						progressIndicatorAnimationsAreEnabled() == !initiallyEnabled);

				Log.d(TAG, "[toggle animations] [assertion passed]");
			}
		});

		Button changeIndicatorPropertiesDp = new Button(this);
		layout.addView(changeIndicatorPropertiesDp);
		changeIndicatorPropertiesDp.setText("change indicator properties (dp)");
		changeIndicatorPropertiesDp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [change indicator (dp)]");

				resetIndicator();

				assertThat(
						"tests are designed to be used with a DotIndicator and cannot run " +
								"otherwise", getProgressIndicator() instanceof DotIndicator);

				DotIndicator indicator = (DotIndicator) getProgressIndicator();

				indicator.setUnselectedDotDiameterDp(UNSELECTED_DIAMETER_DP);
				indicator.setSelectedDotDiameterDp(SELECTED_DIAMETER_DP);
				indicator.setUnselectedDotColor(UNSELECTED_COLOR);
				indicator.setSelectedDotColor(SELECTED_COLOR);
				indicator.setSpacingBetweenDotsPx(spacingPx);
				indicator.setTransitionDuration(TRANSITION_DURATION);

				checkChangeAssumptions();
			}
		});

		Button changeIndicatorPropertiesPx = new Button(this);
		layout.addView(changeIndicatorPropertiesPx);
		changeIndicatorPropertiesPx.setText("change indicator properties (px)");
		changeIndicatorPropertiesPx.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [change indicator (px)]");

				resetIndicator();

				assertThat(
						"tests are designed to be used with a DotIndicator and cannot run " +
								"otherwise", getProgressIndicator() instanceof DotIndicator);

				DotIndicator indicator = (DotIndicator) getProgressIndicator();

				indicator.setUnselectedDotDiameterPx(unselectedDiameterPx);
				indicator.setSelectedDotDiameterPx(selectedDiameterPx);
				indicator.setUnselectedDotColor(UNSELECTED_COLOR);
				indicator.setSelectedDotColor(SELECTED_COLOR);
				indicator.setSpacingBetweenDotsPx(spacingPx);
				indicator.setTransitionDuration(TRANSITION_DURATION);

				checkChangeAssumptions();
			}
		});

		Button resetIndicatorProperties = new Button(this);
		layout.addView(resetIndicatorProperties);
		resetIndicatorProperties.setText("reset indicator properties");
		resetIndicatorProperties.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [reset indicator]");
				resetIndicator();
			}
		});
	}

	private void initialisePxDimensions() {
		unselectedDiameterPx = DimensionHelper.dpToPx(UNSELECTED_DIAMETER_DP,
				TestProgressIndicatorConfig.this);
		selectedDiameterPx = DimensionHelper.dpToPx(SELECTED_DIAMETER_DP,
				TestProgressIndicatorConfig.this);
		spacingPx = DimensionHelper.dpToPx(SPACING_BETWEEN_DOTS_DP,
				TestProgressIndicatorConfig.this);
	}

	/**
	 * Changes the indicator properties to a reset state.
	 */
	private void resetIndicator() {
		setProgressIndicator(new DotIndicator(this));
	}

	private void checkChangeAssumptions() {
		DotIndicator indicator = (DotIndicator) getProgressIndicator();

		indicator.setUnselectedDotDiameterPx(unselectedDiameterPx);
		indicator.setSelectedDotDiameterPx(selectedDiameterPx);
		indicator.setUnselectedDotColor(UNSELECTED_COLOR);
		indicator.setSelectedDotColor(SELECTED_COLOR);
		indicator.setSpacingBetweenDotsPx(spacingPx);
		indicator.setTransitionDuration(TRANSITION_DURATION);

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

		Log.d(TAG, "[checkChangeAssumptions] [assertions passed]");
	}
}
