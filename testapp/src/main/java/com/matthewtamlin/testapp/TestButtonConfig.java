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
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.matthewtamlin.android_utilities_library.helpers.DimensionHelper;
import com.matthewtamlin.sliding_intro_screen_library.buttons.FadeAnimatorFactory;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Appearance;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Behaviour;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test the ability to change button appearance and behaviour. When the button is pressed: <li>The
 * left button acts as restart.</li> <li>The left button displays "Restart"</li> <li>The left button
 * text color is black.</li> <li>The left button text size is 30sp (big)</li> <li>The left button
 * displays a "<<" icon to the left of the text.</li> <li>The left button is displayed on all
 * pages.</li> <li>The right button acts as back.</li> <li>The right button displays "Back"</li>
 * <li>The right button text color is blue.</li> <li>The right button text size is 10sp
 * (small).</li> <li>The right button displays a "<" icon to the left of the text.</li> <li>The
 * right button is displayed on all pages but the last.</li> <li>The final button closes the
 * app.</li> <li>The final button displays "Close".</li> <li>The final button text color is
 * green.</li> <li>The final button text size is 15sp (moderate).</li> <li>The final button displays
 * a ">" icon to the right of the text.</li> <li>The final button is displayed only on the last
 * page.</li>
 * <p/>
 * When the show/hide buttons are pressed, the respective navigation bar buttons should be
 * affected.
 */
public class TestButtonConfig extends ThreePageTestBase {
	/**
	 * Used to identify this class during debugging.
	 */
	private static final String TAG = "[TestButtonConfig]";

	/**
	 * Text to display in the left button after triggering.
	 */
	private static final String LEFT_BUTTON_TEXT = "Restart";

	/**
	 * Text to display in the right button after triggering.
	 */
	private static final String RIGHT_BUTTON_TEXT = "Back";

	/**
	 * Text to display in the final button after triggering.
	 */
	private static final String FINAL_BUTTON_TEXT = "Close";

	/**
	 * Behaviour to use for the left button after triggering.
	 */
	private static final Behaviour LEFT_BUTTON_BEHAVIOUR = new IntroButton.GoToFirstPage();

	/**
	 * Behaviour to use for the right button after triggering.
	 */
	private static final Behaviour RIGHT_BUTTON_BEHAVIOUR = new IntroButton.GoToPreviousPage();

	/**
	 * Behaviour to use for the final button after triggering.
	 */
	private static final Behaviour FINAL_BUTTON_BEHAVIOUR = new IntroButton.CloseApp();

	/**
	 * Appearance to use for the left button after triggering.
	 */
	private static final Appearance LEFT_BUTTON_APPEARANCE = Appearance.TEXT_WITH_LEFT_ICON;

	/**
	 * Appearance to use for the right button after triggering.
	 */
	private static final Appearance RIGHT_BUTTON_APPEARANCE = Appearance.TEXT_WITH_LEFT_ICON;

	/**
	 * Appearance to use for the final button after triggering.
	 */
	private static final Appearance FINAL_BUTTON_APPEARANCE = Appearance.TEXT_WITH_RIGHT_ICON;

	/**
	 * Text color to use for the left button after triggering.
	 */
	private static final int LEFT_BUTTON_COLOR = Color.BLACK;

	/**
	 * Text color to use for the right button after triggering.
	 */
	private static final int RIGHT_BUTTON_COLOR = Color.BLUE;

	/**
	 * Text button to use for the final button after triggering.
	 */
	private static final int FINAL_BUTTON_COLOR = Color.GREEN;

	/**
	 * Text size to use for the left button after triggering.
	 */
	private static final float LEFT_BUTTON_TEXT_SIZE_SP = 30;

	/**
	 * Text size to use for the right button after triggering.
	 */
	private static final float RIGHT_BUTTON_TEXT_SIZE_SP = 10;

	/**
	 * Text size to use for the final button after triggering.
	 */
	private static final float FINAL_BUTTON_TEXT_SIZE_SP = 15;

	/**
	 * Drawable to display in the left button after triggering.
	 */
	private Drawable leftDrawable;

	/**
	 * Drawable to display in the right button after triggering.
	 */
	private Drawable rightDrawable;

	/**
	 * Drawable to display in the final button after triggering.
	 */
	private Drawable finalDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createControlButtons();
	}

	/**
	 * Creates the buttons which control the test.
	 */
	private void createControlButtons() {
		final LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		getRootView().addView(layout);

		Button modifyButtons = new Button(this);
		layout.addView(modifyButtons);
		modifyButtons.setText("change buttons");
		modifyButtons.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [modify buttons]");
				initialiseDrawables();
				changeAppearance();
				checkAppearance();
			}
		});

		Button showHideLeft = new Button(this);
		layout.addView(showHideLeft);
		showHideLeft.setText("Enable/disable left button");
		showHideLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean initiallyDisabled = leftButtonIsDisabled();
				disableLeftButton(!initiallyDisabled, true);
				assertThat("left button didn't disable/enable correctly",
						initiallyDisabled != leftButtonIsDisabled());
			}
		});

		Button showHideRight = new Button(this);
		layout.addView(showHideRight);
		showHideRight.setText("Enable/disable right button");
		showHideRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean initiallyDisabled = rightButtonIsDisabled();
				disableRightButton(!initiallyDisabled, true);
				assertThat("right button didn't disable/enable correctly",
						initiallyDisabled != rightButtonIsDisabled());
			}
		});

		Button showHideFinal = new Button(this);
		layout.addView(showHideFinal);
		showHideFinal.setText("Enable/disable final button");
		showHideFinal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean initiallyDisabled = finalButtonIsDisabled();
				disableFinalButton(!initiallyDisabled, true);
				assertThat("left button didn't disable/enable correctly",
						initiallyDisabled != finalButtonIsDisabled());
			}
		});

		Button toggleLeftVisibility = new Button(this);
		layout.addView(toggleLeftVisibility);
		toggleLeftVisibility.setText("Show/hide left on last page");
		toggleLeftVisibility.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				disableLeftButtonOnLastPage(!leftButtonIsDisabledOnLastPage());
			}
		});
	}


	/**
	 * Initialise the drawables to display in the buttons after triggering.
	 */
	private void initialiseDrawables() {
		leftDrawable = ContextCompat.getDrawable(TestButtonConfig.this,
				R.drawable.introbutton_behaviour_first);
		rightDrawable = ContextCompat.getDrawable(TestButtonConfig.this,
				R.drawable.introbutton_behaviour_previous);
		finalDrawable = ContextCompat.getDrawable(TestButtonConfig.this,
				R.drawable.introbutton_behaviour_progress);
	}

	/**
	 * Modify the buttons.
	 */
	private void changeAppearance() {
		// Modify left button
		setLeftButtonBehaviour(LEFT_BUTTON_BEHAVIOUR);
		setLeftButtonAppearance(LEFT_BUTTON_APPEARANCE);
		setLeftButtonText(LEFT_BUTTON_TEXT, null);
		setLeftButtonIcon(leftDrawable, null);
		setLeftButtonTextColor(LEFT_BUTTON_COLOR);
		setLeftButtonTextSize(LEFT_BUTTON_TEXT_SIZE_SP);
		setLeftButtonTypeface(Typeface.DEFAULT_BOLD);

		// Modify right button
		setRightButtonBehaviour(RIGHT_BUTTON_BEHAVIOUR);
		setRightButtonAppearance(RIGHT_BUTTON_APPEARANCE);
		setRightButtonText(RIGHT_BUTTON_TEXT, null);
		setRightButtonIcon(rightDrawable, null);
		setRightButtonTextColor(RIGHT_BUTTON_COLOR);
		setRightButtonTypeface(Typeface.MONOSPACE);
		setRightButtonTextSize(RIGHT_BUTTON_TEXT_SIZE_SP);

		// Modify final button
		setFinalButtonBehaviour(FINAL_BUTTON_BEHAVIOUR);
		setFinalButtonAppearance(FINAL_BUTTON_APPEARANCE);
		setFinalButtonText(FINAL_BUTTON_TEXT, null);
		setFinalButtonIcon(finalDrawable, null);
		setFinalButtonTextColor(FINAL_BUTTON_COLOR);
		setFinalButtonTypeface(Typeface.SANS_SERIF);
		setFinalButtonTextSize(FINAL_BUTTON_TEXT_SIZE_SP);
	}

	/**
	 * Check that the buttons were modified correctly.
	 */
	private void checkAppearance() {
		// Check that left button properties changed correctly
		assertThat("left button text not set/returned correctly when using implicit behaviour",
				getLeftButtonText(null).equals(LEFT_BUTTON_TEXT));
		assertThat("left button text not set/returned correctly when using explicit behaviour",
				getLeftButtonText(LEFT_BUTTON_BEHAVIOUR.getClass()).equals(LEFT_BUTTON_TEXT));
		assertThat("left button icon not set/returned correctly when using implicit behaviour",
				getLeftButtonIcon(null).equals(leftDrawable));
		assertThat("left button icon not set/returned correctly when using explicit behaviour",
				getLeftButtonIcon(LEFT_BUTTON_BEHAVIOUR.getClass()).equals(leftDrawable));
		assertThat("left button color not set/returned correctly",
				getLeftButtonTextColor() == LEFT_BUTTON_COLOR);
		assertThat("left button behaviour not set/returned correctly",
				getLeftButtonBehaviour().equals(LEFT_BUTTON_BEHAVIOUR));
		assertThat("left button appearance not set/returned correctly",
				getLeftButtonAppearance().equals(LEFT_BUTTON_APPEARANCE));
		assertThat("left button text size not set/returned correctly",
				getLeftButtonTextSize() ==
						DimensionHelper.spToPx(LEFT_BUTTON_TEXT_SIZE_SP, TestButtonConfig.this));

		// Check that right button properties changed correctly
		assertThat("right button text not set/returned correctly when using implicit behaviour",
				getRightButtonText(null).equals(RIGHT_BUTTON_TEXT));
		assertThat("right button text not set/returned correctly when using explicit behaviour",
				getRightButtonText(RIGHT_BUTTON_BEHAVIOUR.getClass()).equals(
						RIGHT_BUTTON_TEXT));
		assertThat("right button icon not set/returned correctly when using implicit behaviour",
				getRightButtonIcon(null).equals(rightDrawable));
		assertThat("right button icon not set/returned correctly when using explicit behaviour",
				getRightButtonIcon(RIGHT_BUTTON_BEHAVIOUR.getClass()).equals(rightDrawable));
		assertThat("right button color not set/returned correctly",
				getRightButtonTextColor() == RIGHT_BUTTON_COLOR);
		assertThat("right button behaviour not set/returned correctly",
				getRightButtonBehaviour().equals(RIGHT_BUTTON_BEHAVIOUR));
		assertThat("right button appearance not set/returned correctly",
				getRightButtonAppearance().equals(RIGHT_BUTTON_APPEARANCE));
		assertThat("right button text size not set/returned correctly",
				getRightButtonTextSize() ==
						DimensionHelper.spToPx(RIGHT_BUTTON_TEXT_SIZE_SP, TestButtonConfig.this));

		// Check that final button properties changed correctly
		assertThat("final button text not set/returned correctly",
				getFinalButtonText(null).equals(FINAL_BUTTON_TEXT));
		assertThat("final button icon not set/returned correctly",
				getFinalButtonIcon(null).equals(finalDrawable));
		assertThat("final button color not set/returned correctly",
				getFinalButtonTextColor() == FINAL_BUTTON_COLOR);
		assertThat("final button behaviour not set/returned correctly",
				getFinalButtonBehaviour().equals(FINAL_BUTTON_BEHAVIOUR));
		assertThat("final button appearance not set/returned correctly",
				getFinalButtonAppearance().equals(FINAL_BUTTON_APPEARANCE));
		assertThat("final button text size not set/returned correctly",
				getFinalButtonTextSize() ==
						DimensionHelper.spToPx(FINAL_BUTTON_TEXT_SIZE_SP, TestButtonConfig.this));

		Log.d(TAG, "[checkAppearance] [assertions passed]");
	}
}