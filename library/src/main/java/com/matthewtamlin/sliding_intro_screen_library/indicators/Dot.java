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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.matthewtamlin.android_utilities_library.helpers.ColorHelper;
import com.matthewtamlin.android_utilities_library.helpers.DimensionHelper;
import com.matthewtamlin.sliding_intro_screen_library.R;

/**
 * A Dot is a {@code View} which shows a solid circular shape. Each Dot has two configurations,
 * active and inactive, and each configuration has two parameters, color and diameter. Dots can be
 * made to smoothly transition between the two configurations.
 */
public final class Dot extends RelativeLayout {
	/**
	 * Used to identify this class during debugging.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "[Dot]";

	/**
	 * Default value for the {@code inactiveDiameter} attribute. This value is used if the attribute
	 * is not supplied. This value has units of display-independent pixels.
	 */
	private static final int DEFAULT_INACTIVE_DIAMETER_DP = 6;

	/**
	 * Default value for the {@code activeDotDiameter} attribute. This value is used if the
	 * attribute is not supplied. This value has units of display-independent pixels.
	 */
	private static final int DEFAULT_ACTIVE_DIAMETER_DP = 9;

	/**
	 * Default value for the {@code inactiveColor} attribute. This value is used if the attribute is
	 * not supplied. This value is an ARGB hex code.
	 */
	private static final int DEFAULT_INACTIVE_COLOR = Color.WHITE;

	/**
	 * Default value for the {@code activeColor} attribute. This value is used if the attribute is
	 * not supplied. This value is an ARGB hex code.
	 */
	private static final int DEFAULT_ACTIVE_COLOR = Color.WHITE;

	/**
	 * Default value for the {@code transitionDuration} attribute. This value is used if the
	 * attribute is not supplied. This value is measured in milliseconds.
	 */
	private static final int DEFAULT_TRANSITION_DURATION_MS = 200;

	/**
	 * Default value for the {@code initiallyActive} attribute. This value is used if the attribute
	 * is not supplied.
	 */
	private static final boolean DEFAULT_INITIALLY_ACTIVE = false;

	/**
	 * The diameter of this Dot when inactive.
	 */
	private int inactiveDiameterPx;

	/**
	 * The diameter of this Dot when active.
	 */
	private int activeDiameterPx;

	/**
	 * The solid color fill of this Dot when inactive, as an ARGB hex code.
	 */
	private int inactiveColor;

	/**
	 * The solid color fill of this Dot when active, as an ARGB hex code.
	 */
	private int activeColor;

	/**
	 * The amount of time to use when animating this Dot between active and inactive, measured in
	 * milliseconds.
	 */
	private int transitionDurationMs;

	/**
	 * The current state of this Dot, in terms of active/inactive/transitioning.
	 */
	private State state;

	/**
	 * The Drawable used to create the visible part of this Dot.
	 */
	private ShapeDrawable shape;

	/**
	 * Displays the drawable representing this Dot.
	 */
	private ImageView drawableHolder;

	/**
	 * The Animator currently acting on this Dot, null if not animating currently.
	 */
	private AnimatorSet currentAnimator = null;

	/**
	 * Constructs a new Dot instance. The following default parameters are used:<ul>
	 * <li>inactiveDiameter: 6dp</li> <li>activeDiameter: 9dp</li> <li>inactiveColor: opaque white
	 * (i.e. ARGB 0xFFFFFFFF)</li> <li>activeColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>transitionDuration: 200ms</li> <li>initiallyActive: false</li></ul>
	 *
	 * @param context
	 * 		the Context in which this Dot is operating, not null
	 */
	public Dot(final Context context) {
		super(context);
		init(null, 0, 0);
	}

	/**
	 * Constructs a new Dot instance. If an attribute specific to this class is not provided, the
	 * relevant default is used. The defaults are:<ul> <li>inactiveDiameter: 6dp</li>
	 * <li>activeDiameter: 9dp</li> <li>inactiveColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>activeColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li> <li>transitionDuration: 200ms</li>
	 * <li>initiallyActive: false</li></ul>
	 *
	 * @param context
	 * 		the Context in which this Dot is operating, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public Dot(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0, 0);
	}

	/**
	 * Constructs a new Dot instance. If an attribute specific to this class is not provided, the
	 * relevant default is used. The defaults are:<ul> <li>inactiveDiameter: 6dp</li>
	 * <li>activeDiameter: 9dp</li> <li>inactiveColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>activeColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li> <li>transitionDuration: 200ms</li>
	 * <li>initiallyActive: false</li></ul>
	 *
	 * @param context
	 * 		the Context in which this Dot is operating, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */
	public Dot(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, defStyleAttr, 0);
	}

	/**
	 * Constructs a new Dot instance. If an attribute specific to this class is not provided, the
	 * relevant default is used. The defaults are:<p/> <li>inactiveDiameter: 6dp</li>
	 * <li>activeDiameter: 9dp</li> <li>inactiveColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>activeColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li> <li>transitionDuration: 200ms</li>
	 * <li>initiallyActive: false</li>
	 *
	 * @param context
	 * 		the Context in which this Dot is operating, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 * @param defStyleRes
	 * 		a resource which supplies default attributes, only used if {@code defStyleAttr}	is 0, pass
	 * 		0 to ignore
	 */
	@TargetApi(21)
	public Dot(final Context context, final AttributeSet attrs, final int defStyleAttr,
			final int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(attrs, defStyleAttr, defStyleRes);
	}

	/**
	 * Initialises the member variables of this Dot and creates the UI. This method should only be
	 * invoked during construction.
	 *
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 * @param defStyleRes
	 * 		a resource which supplies default attributes, only used if {@code defStyleAttr}	is 0, pass
	 * 		0 to ignore
	 */
	private void init(final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
		// Use a TypedArray to process attrs
		final TypedArray attributes = getContext()
				.obtainStyledAttributes(attrs, R.styleable.Dot, defStyleAttr, defStyleRes);

		// Convert default dimensions to px
		final int defaultActiveDiameterPx = DimensionHelper.dpToPx(getContext(),
				DEFAULT_ACTIVE_DIAMETER_DP);
		final int defaultInactiveDiameterPx =
				DimensionHelper.dpToPx(getContext(), DEFAULT_INACTIVE_DIAMETER_DP);

		// Assign provided attributes to member variables, or use the defaults if necessary
		inactiveDiameterPx = attributes
				.getDimensionPixelSize(R.styleable.Dot_inactiveDiameter,
						defaultInactiveDiameterPx);
		activeDiameterPx = attributes
				.getDimensionPixelSize(R.styleable.Dot_activeDiameter, defaultActiveDiameterPx);
		inactiveColor = attributes.getColor(R.styleable.Dot_inactiveColor, DEFAULT_INACTIVE_COLOR);
		activeColor = attributes.getColor(R.styleable.Dot_activeColor, DEFAULT_ACTIVE_COLOR);
		transitionDurationMs = attributes
				.getInt(R.styleable.Dot_transitionDuration, DEFAULT_TRANSITION_DURATION_MS);
		state = attributes.getBoolean(R.styleable.Dot_initiallyActive, DEFAULT_INITIALLY_ACTIVE) ?
				State.ACTIVE : State.INACTIVE;

		// Attributes are no longer required
		attributes.recycle();

		// Ensure the view reflects the attributes
		reflectParametersInView();
	}

	/**
	 * Recreates the UI to reflect the current values of the member variables.
	 */
	private void reflectParametersInView() {
		// Reset root View so that the UI can be entirely recreated
		removeAllViews();

		// Make the root View bounds big enough to encompass the maximum diameter
		final int maxDimension = Math.max(inactiveDiameterPx, activeDiameterPx);
		setLayoutParams(new LayoutParams(maxDimension, maxDimension));

		// Set the gravity to centre for simplicity
		setGravity(Gravity.CENTER);

		// Create the drawable based on the current member variables
		final int diameter = (state == State.ACTIVE) ? activeDiameterPx : inactiveDiameterPx;
		final int color = (state == State.ACTIVE) ? activeColor : inactiveColor;
		shape = new ShapeDrawable(new OvalShape());
		shape.setIntrinsicWidth(diameter);
		shape.setIntrinsicHeight(diameter);
		shape.getPaint().setColor(color);

		// Add the drawable to the drawable holder
		drawableHolder = new ImageView(getContext());
		drawableHolder.setImageDrawable(null); // Forces redraw
		drawableHolder.setImageDrawable(shape);

		// Add the drawable holder to root View
		addView(drawableHolder);
	}

	/**
	 * Plays animations to transition the size and color of this Dot.
	 *
	 * @param startSize
	 * 		the width and height of this Dot at the start of the animation, measured in pixels
	 * @param endSize
	 * 		the width and height of this Dot at the end of the animation, measured in pixels
	 * @param startColor
	 * 		the colour of this Dot at the start of the animation, as an ARGB hex code
	 * @param endColor
	 * 		the colour of this Dot at the end of the animation, as an ARGB hex code
	 * @param duration
	 * 		the duration of the animation, measured in milliseconds
	 * @throws IllegalArgumentException
	 * 		if startSize, endSize or duration are less than 0
	 */
	private void animateDotChange(final int startSize, final int endSize, final int startColor,
			final int endColor, final int duration) {
		if (startSize < 0) {
			throw new IllegalArgumentException("startSize cannot be less than 0");
		} else if (endSize < 0) {
			throw new IllegalArgumentException("endSize cannot be less than 0");
		} else if (duration < 0) {
			throw new IllegalArgumentException("duration cannot be less than 0");
		}

		// To avoid conflicting animations, cancel any existing animation
		if (currentAnimator != null) {
			currentAnimator.cancel();
		}

		// Use an animator set to coordinate shape and color change animations
		currentAnimator = new AnimatorSet();
		currentAnimator.setDuration(duration);
		currentAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				// The state must be updated to reflect the transition
				if (state == State.INACTIVE) {
					state = State.TRANSITIONING_TO_ACTIVE;
				} else if (state == State.ACTIVE) {
					state = State.TRANSITIONING_TO_INACTIVE;
				}
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// Make sure state is stable (i.e. unchanging) at the end of the animation
				if (!state.isStable()) {
					state = state.transitioningTo();
				}

				// Make sure the properties are correct
				changeSize(endSize);
				changeColor(endColor);

				// Declare the animation finished
				currentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// Make sure state is stable (i.e. unchanging) at the end of the animation
				if (!state.isStable()) {
					state = state.transitioningFrom();
				}

				// Make sure the properties are correct
				changeSize(startSize);
				changeColor(startColor);

				// Declare the animation finished
				currentAnimator = null;
			}
		});

		ValueAnimator transitionSize = ValueAnimator.ofInt(startSize, endSize);
		transitionSize.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int size = (Integer) animation.getAnimatedValue();
				changeSize(size);
			}
		});

		ValueAnimator transitionColor = ValueAnimator.ofFloat(1f, 0f);
		transitionColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float mixValue = (float) animation.getAnimatedValue();
				changeColor(ColorHelper.blendColors(startColor, endColor, mixValue));
			}
		});

		currentAnimator.playTogether(transitionSize, transitionColor);
		currentAnimator.start();
	}

	/**
	 * Utility for updating the size of the Dot and reflecting the change in the UI.
	 *
	 * @param newSizePx
	 * 		the desired size, measured in pixels
	 */
	private void changeSize(final int newSizePx) {
		shape.setIntrinsicWidth(newSizePx);
		shape.setIntrinsicHeight(newSizePx);
		drawableHolder.setImageDrawable(null); // Forces ImageView to update drawable
		drawableHolder.setImageDrawable(shape);
	}

	/**
	 * Utility for updating the color of the Dot and reflecting the change in the UI.
	 *
	 * @param newColor
	 * 		the desired color, as an ARGB hex code
	 */
	private void changeColor(final int newColor) {
		shape.getPaint().setColor(newColor);
	}

	/**
	 * Sets the inactive diameter of this Dot and updates the UI to reflect the changes. The update
	 * is instantaneous and does not trigger any animations.
	 *
	 * @param inactiveDiameterPx
	 * 		the diameter to use for this Dot when inactive, measured in pixels, not less than 0
	 * @return this Dot
	 * @throws IllegalArgumentException
	 * 		if {@code inactiveDiameterPx} is less than 0
	 */
	public Dot setInactiveDiameterPx(final int inactiveDiameterPx) {
		if (inactiveDiameterPx < 0) {
			throw new IllegalArgumentException("inactiveDiameterPx cannot be less than 0");
		}

		this.inactiveDiameterPx = inactiveDiameterPx;
		reflectParametersInView();
		return this;
	}

	/**
	 * Sets the inactive diameter of this Dot and updates the UI to reflect the changes. The update
	 * is instantaneous and does not trigger any animations.
	 *
	 * @param inactiveDiameterDp
	 * 		the diameter to use for this Dot when inactive, measured in display-independent pixels, not
	 * 		less than 0
	 * @return this Dot
	 * @throws IllegalArgumentException
	 * 		if {@code inactiveDiameterDp} is less than 0
	 */
	public Dot setInactiveDiameterDp(final int inactiveDiameterDp) {
		if (inactiveDiameterDp < 0) {
			throw new IllegalArgumentException("inactiveDiameterDp cannot be less than 0");
		}

		setInactiveDiameterPx(DimensionHelper.dpToPx(getContext(), inactiveDiameterDp));
		return this;
	}

	/**
	 * @return the inactive diameter, measured in pixels
	 */
	public int getInactiveDiameter() {
		return inactiveDiameterPx;
	}

	/**
	 * Sets the active diameter of this Dot and updates the UI to reflect the changes. The update is
	 * instantaneous and does not trigger any animations.
	 *
	 * @param activeDiameterPx
	 * 		the diameter to use for this Dot when active, measured in pixels, not less than 0
	 * @return this Dot
	 * @throws IllegalArgumentException
	 * 		if {@code activeDiameterPx} is less than 0
	 */
	public Dot setActiveDiameterPx(final int activeDiameterPx) {
		if (activeDiameterPx < 0) {
			throw new IllegalArgumentException("activeDiameterPx cannot be less than 0");
		}

		this.activeDiameterPx = activeDiameterPx;
		reflectParametersInView();
		return this;
	}

	/**
	 * Sets the active diameter of this Dot and updates the UI to reflect the changes. The update is
	 * instantaneous and does not trigger any animations.
	 *
	 * @param activeDiameterDp
	 * 		the diameter to use for this Dot when active, measured in display-independent pixels, not
	 * 		less than 0
	 * @return this Dot
	 * @throws IllegalArgumentException
	 * 		if {@code activeDiameterDp} is less than 0
	 */
	public Dot setActiveDiameterDp(final int activeDiameterDp) {
		if (activeDiameterDp < 0) {
			throw new IllegalArgumentException("activeDiameterDp cannot be less than 0");
		}

		setActiveDiameterPx(activeDiameterDp);
		return this;
	}

	/**
	 * @return the active diameter, measured in pixels
	 */
	public int getActiveDiameter() {
		return activeDiameterPx;
	}

	/**
	 * Sets the inactive color of this Dot and updates the UI to reflect the changes. The update is
	 * instantaneous and does not trigger any animations.
	 *
	 * @param inactiveColor
	 * 		the color to use for this Dot when inactive, as an ARGB hex code
	 * @return this Dot
	 */
	public Dot setInactiveColor(final int inactiveColor) {
		this.inactiveColor = inactiveColor;
		reflectParametersInView();
		return this;
	}

	/**
	 * @return the inactive color, as an ARGB hex code
	 */
	public int getInactiveColor() {
		return inactiveColor;
	}

	/**
	 * Sets the active color of this Dot and updates the UI to reflect the changes. The update is
	 * instantaneous and does not trigger any animations.
	 *
	 * @param activeColor
	 * 		the color to use for this Dot when active, as an ARGB hex code
	 * @return this Dot
	 */
	public Dot setActiveColor(final int activeColor) {
		this.activeColor = activeColor;
		reflectParametersInView();
		return this;
	}

	/**
	 * @return the active color, as an ARGB hex code
	 */
	public int getActiveColor() {
		return activeColor;
	}

	/**
	 * Sets the length of time to use for animations between active and inactive.
	 *
	 * @param transitionDurationMs
	 * 		the length to use for the animations, measured in milliseconds, not less than 0
	 * @return this Dot
	 * @throws IllegalArgumentException
	 * 		if {@code transitionDurationMs} is less than 0
	 */
	public Dot setTransitionDuration(final int transitionDurationMs) {
		if (transitionDurationMs < 0) {
			throw new IllegalArgumentException("transitionDurationMs cannot be less than 0");
		}

		this.transitionDurationMs = transitionDurationMs;
		return this;
	}

	/**
	 * @return the length of time to use for animations between active and inactive, measured in
	 * milliseconds
	 */
	public int getTransitionDuration() {
		return transitionDurationMs;
	}

	/**
	 * Toggles the state of this Dot between active and inactive.
	 *
	 * @param animate
	 * 		whether or not the transition should be animated
	 */
	public void toggleState(final boolean animate) {
		if (currentAnimator != null) {
			currentAnimator.cancel();
		}

		if (state != State.ACTIVE) {
			setActive(animate);
		} else if (state != State.INACTIVE) {
			setInactive(animate);
		} else {
			Log.e(TAG, "[Animation trying to start from illegal state]");
		}
	}

	/**
	 * Transitions this Dot to inactive. Animations are shown if the Dot is not already inactive.
	 *
	 * @param animate
	 * 		whether or not the transition should be animated
	 */
	public void setInactive(final boolean animate) {
		// Any existing animation will conflict with this animations and must be cancelled
		if (currentAnimator != null) {
			currentAnimator.cancel();
		}

		// Animate only if the animation is requested, is necessary, and will actually display
		final boolean shouldAnimate =
				animate && (state != State.INACTIVE) && (transitionDurationMs > 0);

		if (shouldAnimate) {
			animateDotChange(activeDiameterPx, inactiveDiameterPx, activeColor, inactiveColor,
					transitionDurationMs);
		} else {
			// The UI must still be changed, just without animations
			changeSize(inactiveDiameterPx);
			changeColor(inactiveColor);
			state = State.INACTIVE;
		}
	}

	/**
	 * Transitions this Dot to active. Animations are shown if the Dot is not already active.
	 *
	 * @param animate
	 * 		whether or not the transition should be animated
	 */
	public void setActive(final boolean animate) {
		// Any existing animation will conflict with this animations and must be cancelled
		if (currentAnimator != null) {
			currentAnimator.cancel();
		}

		// Animate only if the animation is requested, is necessary, and will actually display
		final boolean shouldAnimate =
				animate && (state != State.ACTIVE) && (transitionDurationMs > 0);


		if (shouldAnimate) {
			animateDotChange(inactiveDiameterPx, activeDiameterPx, inactiveColor, activeColor,
					transitionDurationMs);
		} else {
			// The UI must still be changed, just without animations
			changeSize(activeDiameterPx);
			changeColor(activeColor);
			state = State.ACTIVE;
		}
	}

	/**
	 * Returns the current state of this Dot. This method exists for testing purposes only.
	 *
	 * @return the current state
	 */
	protected State getCurrentState() {
		return state;
	}

	/**
	 * Returns the current diameter of this Dot. This method exists for testing purposes only.
	 * Results will be inconsistent if this Dot is currently transitioning between active and
	 * inactive.
	 *
	 * @return the current diameter, measured in pixels
	 */
	protected int getCurrentDiameter() {
		return shape.getIntrinsicHeight();
	}

	/**
	 * Returns the current color of this Dot. This method exists for testing purposes only. Results
	 * will be inconsistent if this Dot is currently transitioning between active and inactive.
	 *
	 * @return the current color, as an ARGB hex code
	 */
	protected int getCurrentColor() {
		return shape.getPaint().getColor();
	}

	/**
	 * Returns the default inactive diameter. This method exists for testing purposes only.
	 *
	 * @return the default inactive diameter, measured in display-independent pixels
	 */
	protected int getDefaultInactiveDiameterDp() {
		return DEFAULT_INACTIVE_DIAMETER_DP;
	}

	/**
	 * Returns the default active diameter. This method exists for testing purposes only.
	 *
	 * @return the default active diameter, measured in display-independent pixels
	 */
	protected int getDefaultActiveDiameterDp() {
		return DEFAULT_ACTIVE_DIAMETER_DP;
	}

	/**
	 * Returns the default inactive color. This method exists for testing purposes only.
	 *
	 * @return the default inactive color, as an ARGB hex code
	 */
	protected int getDefaultInactiveColor() {
		return DEFAULT_INACTIVE_COLOR;
	}

	/**
	 * Returns the default active color. This method exists for testing purposes only.
	 *
	 * @return the default active color, as an ARGB hex code
	 */
	protected int getDefaultActiveColor() {
		return DEFAULT_ACTIVE_COLOR;
	}

	/**
	 * Returns the default transition duration. This method exists for testing purposes only.
	 *
	 * @return the default transition duration, measured in milliseconds
	 */
	protected int getDefaultTransitionDuration() {
		return DEFAULT_TRANSITION_DURATION_MS;
	}

	/**
	 * Returns whether or not Dots are active by default. This method exists for testing purposes
	 * only.
	 *
	 * @return true if Dots are active by default default, false otherwise
	 */
	protected boolean getDefaultInitiallyActive() {
		return DEFAULT_INITIALLY_ACTIVE;
	}

	/**
	 * The possible states of a Dot.
	 */
	protected enum State {
		/**
		 * A Dot in this State currently reflects the inactive parameters, and is not
		 * transitioning.
		 */
		INACTIVE(true, null, null),

		/**
		 * A Dot in this State currently reflects the active parameters, and is not transitioning.
		 */
		ACTIVE(true, null, null),

		/**
		 * A Dot in this State does not currently reflect either the active or inactive parameters,
		 * and is transitioning towards the active state.
		 */
		TRANSITIONING_TO_ACTIVE(false, ACTIVE, INACTIVE),

		/**
		 * A Dot in this State does not currently reflect either the active or inactive parameters,
		 * and is transitioning towards the inactive state.
		 */
		TRANSITIONING_TO_INACTIVE(false, INACTIVE, ACTIVE);

		/**
		 * Indicates whether or not a Dot in this State has constant size and color.
		 */
		private final boolean isStable;

		/**
		 * The State this State is transitioning towards, null if this State is stable.
		 */
		private final State to;

		/**
		 * The State this State is transitioning from, null if this State is stable.
		 */
		private final State from;

		/**
		 * Constructs a new State instance.
		 *
		 * @param isStable
		 * 		whether or not a Dot in this State has constant size and color
		 * @param to
		 * 		the State this State is transitioning to, null if this State is stable
		 * @param from
		 * 		the State this State is transitioning from, null if this State is stable
		 */
		State(final boolean isStable, final State to, final State from) {
			this.isStable = isStable;
			this.to = to;
			this.from = from;
		}

		/**
		 * @return whether or not a Dot in this State has constant size and color
		 */
		public boolean isStable() {
			return isStable;
		}

		/**
		 * @return the State this State is transitioning towards, null if this State is stable
		 */
		public State transitioningTo() {
			return to;
		}

		/**
		 * @return the State this State is transitioning from, null if this State is stable
		 */
		public State transitioningFrom() {
			return from;
		}
	}
}