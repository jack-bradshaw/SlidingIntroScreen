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

package com.matthewtamlin.sliding_intro_screen_library.indicators;

import android.content.Context;
import android.graphics.Color;

import com.matthewtamlin.android_utilities_library.helpers.DimensionHelper;
import com.matthewtamlin.sliding_intro_screen_library.BuildConfig;
import com.matthewtamlin.sliding_intro_screen_library.indicators.Dot.State;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 18, constants = BuildConfig.class)
public class TestDot {
	/**
	 * Inactive diameter value to use during testing, measured in display-independent pixels.
	 */
	private static final int INACTIVE_DIAMETER_DP = 10;

	/**
	 * Active diameter value to use during testing, measured in display-independent pixels.
	 */
	private static final int ACTIVE_DIAMETER_DP = 20;

	/**
	 * Inactive color to use during testing, as an ARGB hex code.
	 */
	private static final int INACTIVE_COLOR = Color.CYAN;

	/**
	 * Active color to use during testing, as an ARGB hex code.
	 */
	private static final int ACTIVE_COLOR = Color.RED;

	/**
	 * Transition duration to use during testing, measured in milliseconds.
	 */
	private static final int TRANSITION_DURATION_MS = 600;

	/**
	 * Whether or not dot should be initially active during testing.
	 */
	private static final boolean INITIALLY_ACTIVE = false;

	/**
	 * Inactive diameter value to use during testing, measured in pixels. This value must be
	 * calculated before running tests.
	 */
	private int INACTIVE_DIAMETER_PX;

	/**
	 * Active diameter value to use during testing, measured in pixels. This value must be
	 * calculated before running tests.
	 */
	private int ACTIVE_DIAMETER_PX;

	/**
	 * A dot for use during tests.
	 */
	private Dot dot;

	/**
	 * A context for use during tests.
	 */
	private Context context;

	@Before
	public void init() {
		context = RuntimeEnvironment.application.getApplicationContext();
		dot = new Dot(context);

		INACTIVE_DIAMETER_PX = DimensionHelper.dpToPx(context, INACTIVE_DIAMETER_DP);
		ACTIVE_DIAMETER_PX = DimensionHelper.dpToPx(context, ACTIVE_DIAMETER_DP);
	}

	@Test(expected = NullPointerException.class)
	public void constructor_context_invalidArg_shouldThrowException() {
		new Dot(null); // Should throw exception
	}

	@Test
	public void constructor_context_validArg_shouldInitialiseToDefaults() {
		// Check variables
		assertThat("inactive diameter was not initialised to default",
				dot.getInactiveDiameter() ==
						DimensionHelper.dpToPx(context, dot.getDefaultInactiveDiameterDp()));
		assertThat("active diameter was not initialised to default",
				dot.getActiveDiameter() ==
						DimensionHelper.dpToPx(context, dot.getDefaultActiveDiameterDp()));
		assertThat("inactive color was not initialised to default",
				dot.getInactiveColor() == dot.getDefaultInactiveColor());
		assertThat("active color was not initialised to default",
				dot.getActiveColor() == dot.getDefaultActiveColor());
		assertThat("transition duration was not initialised to default",
				dot.getTransitionDuration() == dot.getDefaultTransitionDuration());

		// Check status
		assertThat(dot.getCurrentDiameter() == dot.getDefaultActiveDiameterDp(),
				is(dot.getDefaultInitiallyActive()));
		assertThat(dot.getCurrentDiameter() == dot.getDefaultInactiveDiameterDp(),
				is(!dot.getDefaultInitiallyActive()));
		assertThat(dot.getCurrentState() == (INITIALLY_ACTIVE ? State.ACTIVE : State.INACTIVE),
				is(true));
	}

	@Test(expected = IllegalArgumentException.class)
	public void setInactiveDiameterPx_int_invalidArg_shouldThrowException() {
		dot.setInactiveDiameterPx(-1); // Should throw exception
	}

	@Test
	public void setInactiveDiameterPx_int_validArg_shouldChangeDiameter() {
		dot.setInactiveDiameterPx(INACTIVE_DIAMETER_PX);

		assertThat("inactive diameter was not set correctly", dot.getInactiveDiameter() ==
				INACTIVE_DIAMETER_PX);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setInactiveDiameterDp_int_invalidArg_shouldThrowException() {
		dot.setInactiveDiameterDp(-1); // Should throw exception
	}

	@Test
	public void setInactiveDiameterDp_int_validArg_shouldChangeDiameter() {
		dot.setInactiveDiameterDp(INACTIVE_DIAMETER_DP);

		assertThat("inactive diameter was not set correctly",
				dot.getInactiveDiameter() == INACTIVE_DIAMETER_PX);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setActiveDiameterPx_int_invalidArg_shouldThrowException() {
		dot.setActiveDiameterPx(-1); // Should throw exception
	}

	@Test
	public void setActiveDiameterPx_int_validArg_shouldChangeDiameter() {
		dot.setActiveDiameterPx(ACTIVE_DIAMETER_PX);

		assertThat("active diameter was not set correctly",
				dot.getActiveDiameter() == ACTIVE_DIAMETER_PX);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setActiveDiameterDp_int_invalidArg_shouldThrowException() {
		dot.setActiveDiameterDp(-1); // Should throw exception
	}

	@Test
	public void setActiveDiameterDp_int_validArg_shouldChangeDiameter() {
		dot.setActiveDiameterDp(ACTIVE_DIAMETER_DP);

		assertThat("active diameter was not set correctly",
				dot.getActiveDiameter() == ACTIVE_DIAMETER_PX);
	}

	@Test
	public void setInactiveColor_int_shouldChangeColor() {
		dot.setInactiveColor(INACTIVE_COLOR);

		assertThat("inactive color was not set correctly", dot.getInactiveColor() ==
						INACTIVE_COLOR,
				is(true));
	}

	@Test
	public void setActiveColor_int_shouldChangeColor() {
		dot.setActiveColor(ACTIVE_COLOR);

		assertThat("active color was not set correctly", dot.getActiveColor() == ACTIVE_COLOR,
				is(true));
	}

	@Test(expected = IllegalArgumentException.class)
	public void setTransitionDurationMs_int_invalidArg_shouldThrowException() {
		dot.setTransitionDuration(-1); // Should throw exception
	}

	@Test
	public void setTransitionDurationMs_int_validArg_shouldChangeTransitionDuration() {
		dot.setTransitionDuration(TRANSITION_DURATION_MS);

		assertThat("transition duration was not set correctly",
				dot.getTransitionDuration() == TRANSITION_DURATION_MS);
	}

	@Test
	public void toggleState_boolean_animationDisabled_shouldChangeState() {
		State initialState = dot.getCurrentState();

		if (!initialState.isStable()) {
			throw new RuntimeException("initial state must be stable for valid test conditions");
		}

		dot.toggleState(false);

		if (initialState == State.ACTIVE) {
			assertThat("state did not change correctly", dot.getCurrentState() == State.INACTIVE);
			assertThat("diameter did not change correctly",
					dot.getCurrentDiameter() == dot.getDefaultInactiveDiameterDp());
			assertThat("color did not change correctly",
					dot.getCurrentColor() == dot.getDefaultInactiveColor());
		} else if (initialState == State.INACTIVE) {
			assertThat("state did not change correctly", dot.getCurrentState() == State.ACTIVE);
			assertThat("diameter did not change correctly",
					dot.getCurrentDiameter() == dot.getDefaultActiveDiameterDp());
			assertThat("color did not change correctly",
					dot.getCurrentColor() == dot.getDefaultActiveColor());
		} else {
			throw new RuntimeException("state after toggling must be stable");
		}
	}
}