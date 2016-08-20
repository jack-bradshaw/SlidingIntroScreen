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
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Appearance;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Behaviour;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButtonAccessor;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests to verify that the IntroButtons can be configured correctly.
 * <p>
 * When the change behaviour/appearance test is triggered, the buttons should have the following
 * properties:<ul><li>The left button moves the user to the first page.</li> <li>The left button
 * displays "Restart"</li> <li>The left button text color is black.</li> <li>The left button text
 * size is 30sp (big)</li> <li>The left button displays a "<<" icon to the left of the text.</li>
 * <li>The right button moves the user to the previous page.</li> <li>The right button displays
 * "Back"</li> <li>The right button text color is blue.</li> <li>The right button text size is 10sp
 * (small).</li> <li>The right button displays a "<" icon to the left of the text.</li> <li>The
 * final button does nothing.</li> <li>The final button displays "Nill".</li> <li>The final button
 * text color is green.</li> <li>The final button text size is 15sp (moderate).</li> <li>The final
 * button displays a ">" icon to the right of the text.</li> </ul>
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
	private static final String FINAL_BUTTON_TEXT = "Nill";

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
	private static final Behaviour FINAL_BUTTON_BEHAVIOUR = new IntroButton.DoNothing();

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
		initialiseDrawables(); // Needed before tests can run

		// Create a layout to display the control buttons over the ViewPager
		final LinearLayout controlButtonHolder = new LinearLayout(this);
		controlButtonHolder.setOrientation(LinearLayout.VERTICAL);
		getRootView().addView(controlButtonHolder);

		// Add the test buttons to the control layout
		controlButtonHolder.addView(createModifyAppearanceAndBehaviourButton());
		controlButtonHolder.addView(createToggleLeftButtonButton());
		controlButtonHolder.addView(createToggleRightButtonButton());
		controlButtonHolder.addView(createToggleFinalButtonButton());
		controlButtonHolder.addView(createShowLeftButtonOnLastPageButton());
	}

	/**
	 * Initialise the drawables to display in the navigation buttons after the appearance/behaviour
	 * change is triggered.
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
	 * @return a Button which modifies the appearance/behaviour of the buttons and verifies that the
	 * changes are correct
	 */
	private Button createModifyAppearanceAndBehaviourButton() {
		final Button button = new Button(this);
		button.setText("Modify appearance and behaviour");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				makeChanges();
				checkChanges();
			}
		});

		return button;
	}

	/**
	 * @return a Button which enables/disables the left button
	 */
	private Button createToggleLeftButtonButton() {
		final Button button = new Button(this);
		button.setText("Enable/disable left button");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final boolean initiallyDisabled = leftButtonIsEntirelyDisabled();
				disableLeftButton(!initiallyDisabled);
				assertThat("left button didn't disable/enable correctly",
						initiallyDisabled != leftButtonIsEntirelyDisabled());
			}
		});

		return button;
	}

	/**
	 * @return a Button which enables/disables the right button
	 */
	private Button createToggleRightButtonButton() {
		final Button button = new Button(this);
		button.setText("Enable/disable right button");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final boolean initiallyDisabled = rightButtonIsDisabled();
				disableRightButton(!initiallyDisabled);
				assertThat("right button didn't disable/enable correctly",
						initiallyDisabled != rightButtonIsDisabled());
			}
		});

		return button;
	}

	/**
	 * @return a Button which enables/disables the final button
	 */
	private Button createToggleFinalButtonButton() {
		final Button button = new Button(this);
		button.setText("Enable/disable final button");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final boolean initiallyDisabled = finalButtonIsDisabled();
				disableFinalButton(!initiallyDisabled);
				assertThat("left button didn't disable/enable correctly",
						initiallyDisabled != finalButtonIsDisabled());
			}
		});

		return button;
	}

	/**
	 * @return a Button which enables/disables the left button on the last page
	 */
	private Button createShowLeftButtonOnLastPageButton() {
		final Button button = new Button(this);
		button.setText("Enable/disable left on last page");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				disableLeftButtonOnLastPage(!leftButtonIsDisabledOnLastPage());
			}
		});

		return button;
	}

	/**
	 * Modifies the Behaviour and Appearance of the buttons.
	 */
	private void makeChanges() {
		// Modify the left button
		final IntroButtonAccessor leftButtonAccessor = getLeftButtonAccessor();
		leftButtonAccessor.setBehaviour(LEFT_BUTTON_BEHAVIOUR);
		leftButtonAccessor.setAppearance(LEFT_BUTTON_APPEARANCE);
		leftButtonAccessor.setText(LEFT_BUTTON_TEXT, null);
		leftButtonAccessor.setIcon(leftDrawable, null);
		leftButtonAccessor.setTextColor(LEFT_BUTTON_COLOR);
		leftButtonAccessor.setTextSize(LEFT_BUTTON_TEXT_SIZE_SP);
		leftButtonAccessor.setTypeface(Typeface.DEFAULT_BOLD);

		// Modify the right button
		final IntroButtonAccessor rightButtonAccessor = getRightButtonAccessor();
		rightButtonAccessor.setBehaviour(RIGHT_BUTTON_BEHAVIOUR);
		rightButtonAccessor.setAppearance(RIGHT_BUTTON_APPEARANCE);
		rightButtonAccessor.setText(RIGHT_BUTTON_TEXT, null);
		rightButtonAccessor.setIcon(rightDrawable, null);
		rightButtonAccessor.setTextColor(RIGHT_BUTTON_COLOR);
		rightButtonAccessor.setTextSize(RIGHT_BUTTON_TEXT_SIZE_SP);
		rightButtonAccessor.setTypeface(Typeface.MONOSPACE);

		// Modify the final button
		final IntroButtonAccessor finalButtonAccessor = getFinalButtonAccessor();
		finalButtonAccessor.setBehaviour(FINAL_BUTTON_BEHAVIOUR);
		finalButtonAccessor.setAppearance(FINAL_BUTTON_APPEARANCE);
		finalButtonAccessor.setText(FINAL_BUTTON_TEXT, null);
		finalButtonAccessor.setIcon(finalDrawable, null);
		finalButtonAccessor.setTextColor(FINAL_BUTTON_COLOR);
		finalButtonAccessor.setTextSize(FINAL_BUTTON_TEXT_SIZE_SP);
		finalButtonAccessor.setTypeface(Typeface.SANS_SERIF);
	}

	/**
	 * Verifies that the buttons were modified correctly by {@link #makeChanges()}.
	 */
	private void checkChanges() {
		// Check that left button properties changed correctly
		final IntroButtonAccessor leftButtonAccessor = getLeftButtonAccessor();
		assertThat("left button text not set/returned correctly (implicit behaviour reference)",
				leftButtonAccessor.getText(null).equals(LEFT_BUTTON_TEXT));
		assertThat("left button text not set/returned correctly (explicit behaviour reference)",
				leftButtonAccessor.getText(LEFT_BUTTON_BEHAVIOUR.getClass())
						.equals(LEFT_BUTTON_TEXT));
		assertThat("left button icon not set/returned correctly (implicit behaviour reference)",
				leftButtonAccessor.getIcon(null).equals(leftDrawable));
		assertThat("left button icon not set/returned correctly (explicit behaviour reference)",
				leftButtonAccessor.getIcon(LEFT_BUTTON_BEHAVIOUR.getClass()).equals(leftDrawable));
		assertThat("left button color not set/returned correctly",
				leftButtonAccessor.getTextColor() == LEFT_BUTTON_COLOR);
		assertThat("left button behaviour not set/returned correctly",
				leftButtonAccessor.getBehaviour().equals(LEFT_BUTTON_BEHAVIOUR));
		assertThat("left button appearance not set/returned correctly",
				leftButtonAccessor.getAppearance().equals(LEFT_BUTTON_APPEARANCE));
		assertThat("left button text size not set/returned correctly",
				leftButtonAccessor.getTextSize() ==
						DimensionHelper.spToPx(TestButtonConfig.this, LEFT_BUTTON_TEXT_SIZE_SP));

		// Check that right button properties changed correctly
		final IntroButtonAccessor rightButtonAccessor = getRightButtonAccessor();
		assertThat("right button text not set/returned correctly (implicit behaviour reference)",
				rightButtonAccessor.getText(null).equals(RIGHT_BUTTON_TEXT));
		assertThat("right button text not set/returned correctly (explicit behaviour reference)",
				rightButtonAccessor.getText(RIGHT_BUTTON_BEHAVIOUR.getClass()).equals(
						RIGHT_BUTTON_TEXT));
		assertThat("right button icon not set/returned correctly when using implicit behaviour",
				rightButtonAccessor.getIcon(null).equals(rightDrawable));
		assertThat("right button icon not set/returned correctly (explicit behaviour reference)",
				rightButtonAccessor.getIcon(RIGHT_BUTTON_BEHAVIOUR.getClass())
						.equals(rightDrawable));
		assertThat("right button color not set/returned correctly",
				rightButtonAccessor.getTextColor() == RIGHT_BUTTON_COLOR);
		assertThat("right button behaviour not set/returned correctly",
				rightButtonAccessor.getBehaviour().equals(RIGHT_BUTTON_BEHAVIOUR));
		assertThat("right button appearance not set/returned correctly",
				rightButtonAccessor.getAppearance().equals(RIGHT_BUTTON_APPEARANCE));
		assertThat("right button text size not set/returned correctly",
				rightButtonAccessor.getTextSize() ==
						DimensionHelper.spToPx(TestButtonConfig.this, RIGHT_BUTTON_TEXT_SIZE_SP));

		// Check that final button properties changed correctly
		final IntroButtonAccessor finalButtonAccessor = getFinalButtonAccessor();
		assertThat("final button text not set/returned correctly when using implicit behaviour",
				finalButtonAccessor.getText(null).equals(FINAL_BUTTON_TEXT));
		assertThat("final button text not set/returned correctly (explicit behaviour reference)",
				finalButtonAccessor.getText(FINAL_BUTTON_BEHAVIOUR.getClass())
						.equals(FINAL_BUTTON_TEXT));
		assertThat("final button icon not set/returned correctly when using implicit behaviour",
				finalButtonAccessor.getIcon(null).equals(finalDrawable));
		assertThat("final button icon not set/returned correctly (explicit behaviour reference)",
				finalButtonAccessor.getIcon(FINAL_BUTTON_BEHAVIOUR.getClass())
						.equals(finalDrawable));
		assertThat("final button color not set/returned correctly",
				finalButtonAccessor.getTextColor() == FINAL_BUTTON_COLOR);
		assertThat("final button behaviour not set/returned correctly",
				finalButtonAccessor.getBehaviour().equals(FINAL_BUTTON_BEHAVIOUR));
		assertThat("final button appearance not set/returned correctly",
				finalButtonAccessor.getAppearance().equals(FINAL_BUTTON_APPEARANCE));
		assertThat("final button text size not set/returned correctly",
				finalButtonAccessor.getTextSize() ==
						DimensionHelper.spToPx(TestButtonConfig.this, FINAL_BUTTON_TEXT_SIZE_SP));

		Log.d(TAG, "[checkChanges] [assertions passed]");
	}
}