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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;

import com.matthewtamlin.android_utilities_library.helpers.DimensionHelper;
import com.matthewtamlin.sliding_intro_screen_library.R;

import java.util.ArrayList;

import static android.widget.RelativeLayout.LayoutParams.MATCH_PARENT;

/**
 * Displays a set of dots to indicate the selected item in a set.
 */
public class DotIndicator extends RelativeLayout implements SelectionIndicator {
	/**
	 * Used to identify this class during debugging.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "[DotIndicator]";

	/**
	 * Default value for the {@code numberOfDots} attribute. This value is used if the attribute is
	 * not supplied.
	 */
	private static final int DEFAULT_NUMBER_OF_DOTS = 1;

	/**
	 * Default value for the {@code selectedDotIndex} attribute. This value is used if the attribute
	 * is not supplied.
	 */
	private static final int DEFAULT_SELECTED_DOT_INDEX = 0;

	/**
	 * Default value for the {@code unselectedDotDiameter} attribute. This value is used if the
	 * attribute is not supplied. This value has units of display-independent pixels.
	 */
	private static final int DEFAULT_UNSELECTED_DOT_DIAMETER_DP = 6;

	/**
	 * Default value for the {@code selectedDotDiameter} attribute. This value is used if the
	 * attribute is not supplied. This value has units of display-independent pixels.
	 */
	private static final int DEFAULT_SELECTED_DOT_DIAMETER_DP = 9;

	/**
	 * Default value for the {@code unselectedDotColor} attribute. This value is used if the
	 * attribute is not supplied. This value is an ARGB hex code.
	 */
	private static final int DEFAULT_UNSELECTED_DOT_COLOR = Color.WHITE;

	/**
	 * Default value for the {@code selectedDotColor} attribute. This value is used if the attribute
	 * is not supplied. This value is an ARGB hex code.
	 */
	private static final int DEFAULT_SELECTED_DOT_COLOR = Color.WHITE;

	/**
	 * Default value for the {@code spacingBetweenDots} attribute. This value is used if the
	 * attribute is not supplied. This value has units of display-independent pixels.
	 */
	private static final int DEFAULT_SPACING_BETWEEN_DOTS_DP = 7;

	/**
	 * Default value for the {@code dotTransitionDuration} attribute. This value is used if the
	 * attribute is not supplied. This value has units of milliseconds.
	 */
	private static final int DEFAULT_DOT_TRANSITION_DURATION_MS = 200;

	/**
	 * The number of dots shown.
	 */
	private int numberOfDots;

	/**
	 * The index of the selected dot, counting from zero.
	 */
	private int selectedDotIndex;

	/**
	 * The diameter to use for the unselected dots.
	 */
	private int unselectedDotDiameterPx;

	/**
	 * The diameter to use for the selected dot.
	 */
	private int selectedDotDiameterPx;

	/**
	 * The color to use for the unselected dots, as an ARGB hex code.
	 */
	private int unselectedDotColor;

	/**
	 * The colour to use for the selected dot, as an ARGB hex code.
	 */
	private int selectedDotColor;

	/**
	 * The spacing between dots. The spacing is measured as the distance between the edges of
	 * consecutive dots. The spacing is applied as if all dots are unselected, and when a dot
	 * changes size to become selected, it stays fixed at its centre.
	 */
	private int spacingBetweenDotsPx;

	/**
	 * The length of time for transitioning a dot between selected and unselected, measured in
	 * milliseconds.
	 */
	private int dotTransitionDuration;

	/**
	 * The dots shown in this View.
	 */
	private final ArrayList<Dot> dots = new ArrayList<>();

	/**
	 * Constructs a new DotIndicator instance. The following default parameters are used:
	 * <ul><li>numberOfDots: 1</li> <li>selectedDotIndex: 0</li> <li>unselectedDotDiameter: 6dp</li>
	 * <li>selectedDotDiameter: 9dp</li> <li>unselectedDotColor: opaque white (i.e. ARGB
	 * 0xFFFFFFFF)</li> <li>selectedDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>spacingBetweenDots: 7dp</li> <li>dotTransitionDuration: 200ms</li></ul>
	 *
	 * @param context
	 * 		the Context in which this DotIndicator is operating, not null
	 */
	public DotIndicator(final Context context) {
		super(context);
		init(null, 0, 0);
	}

	/**
	 * Constructs a new DotIndicator instance. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<ul><li>numberOfDots: 1</li>
	 * <li>selectedDotIndex: 0</li> <li>unselectedDotDiameter: 6dp</li> <li>selectedDotDiameter:
	 * 9dp</li> <li>unselectedDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>selectedDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li> <li>spacingBetweenDots:
	 * 7dp</li> <li>dotTransitionDuration: 200ms</li></ul>
	 *
	 * @param context
	 * 		the Context in which this SelectionIndicator is operating, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public DotIndicator(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0, 0);
	}

	/**
	 * Constructs a new DotIndicator instance. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<ul><li>numberOfDots: 1</li>
	 * <li>selectedDotIndex: 0</li> <li>unselectedDotDiameter: 6dp</li> <li>selectedDotDiameter:
	 * 9dp</li> <li>unselectedDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>selectedDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li> <li>spacingBetweenDots:
	 * 7dp</li> <li>dotTransitionDuration: 200ms</li></ul>
	 *
	 * @param context
	 * 		the context in which this SelectionIndicator is operating
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */
	public DotIndicator(final Context context, final AttributeSet attrs, final int
			defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, defStyleAttr, 0);
	}

	/**
	 * Constructs a new DotIndicator instance. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<ul><li>numberOfDots: 1</li>
	 * <li>selectedDotIndex: 0</li> <li>unselectedDotDiameter: 6dp</li> <li>selectedDotDiameter:
	 * 9dp</li> <li>unselectedDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>selectedDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li> <li>spacingBetweenDots:
	 * 7dp</li> <li>dotTransitionDuration: 200ms</li></ul>
	 *
	 * @param context
	 * 		the context in which this SelectionIndicator is operating
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 * @param defStyleRes
	 * 		a resource which supplies default attributes, only used if {@code defStyleAttr}	is zero,
	 * 		pass 0 to ignore
	 */
	@TargetApi(21)
	public DotIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(attrs, defStyleAttr, defStyleRes);
	}

	/**
	 * Initialises the member variables of this DotIndicator and creates the UI. This method should
	 * only be invoked during construction.
	 *
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 * @param defStyleRes
	 * 		a resource which supplies default attributes, only used if {@code defStyleAttr}	is zero,
	 * 		pass 0 to ignore
	 */
	private void init(final AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		// Use a TypedArray to process attrs
		final TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable
				.DotIndicator, defStyleAttr, defStyleRes);

		// Need to convert all default dimensions to px
		final int defaultSelectedDotDiameterPx =
				DimensionHelper.dpToPx(DEFAULT_SELECTED_DOT_DIAMETER_DP, getContext());
		final int defaultUnselectedDotDiameterPx = DimensionHelper.dpToPx(
				DEFAULT_UNSELECTED_DOT_DIAMETER_DP, getContext());
		final int defaultSpacingBetweenDotsPx =
				DimensionHelper.dpToPx(DEFAULT_SPACING_BETWEEN_DOTS_DP, getContext());

		// Assign provided attributes to member variables, or use the defaults if necessary
		numberOfDots = attributes
				.getInt(R.styleable.DotIndicator_numberOfDots, DEFAULT_NUMBER_OF_DOTS);
		selectedDotIndex = attributes
				.getInt(R.styleable.DotIndicator_selectedDotIndex,
						DEFAULT_SELECTED_DOT_INDEX);
		unselectedDotDiameterPx = attributes
				.getDimensionPixelSize(R.styleable.DotIndicator_unselectedDotDiameter,
						defaultUnselectedDotDiameterPx);
		selectedDotDiameterPx = attributes
				.getDimensionPixelSize(R.styleable.DotIndicator_selectedDotDiameter,
						defaultSelectedDotDiameterPx);
		unselectedDotColor = attributes.getColor(R.styleable.DotIndicator_unselectedDotColor,
				DEFAULT_UNSELECTED_DOT_COLOR);
		selectedDotColor = attributes
				.getColor(R.styleable.DotIndicator_selectedDotColor,
						DEFAULT_SELECTED_DOT_COLOR);
		spacingBetweenDotsPx = attributes
				.getDimensionPixelSize(R.styleable.DotIndicator_spacingBetweenDots,
						defaultSpacingBetweenDotsPx);
		dotTransitionDuration = attributes
				.getDimensionPixelSize(R.styleable.DotIndicator_dotTransitionDuration,
						DEFAULT_DOT_TRANSITION_DURATION_MS);

		// Attributes are no longer required
		attributes.recycle();

		// Setup UI
		setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
		setGravity(Gravity.CENTER);
		reflectParametersInView();
	}

	/**
	 * Constructs and displays dots based on current member variables.
	 */
	private void reflectParametersInView() {
		// Reset the root View and the dot Collection so that the UI can be entirely recreated
		removeAllViews();
		dots.clear();

		// Create the dots incrementally from left to right
		for (int i = 0; i < numberOfDots; i++) {
			// Create a dot and set its properties
			Dot dot = new Dot(getContext());
			dot.setInactiveDiameterPx(unselectedDotDiameterPx)
					.setActiveDiameterPx(selectedDotDiameterPx)
					.setActiveColor(selectedDotColor)
					.setInactiveColor(unselectedDotColor)
					.setTransitionDuration(dotTransitionDuration);


			// Make the dot active if necessary
			if (i == selectedDotIndex) {
				dot.setActive(false);
			} else {
				dot.setInactive(false);
			}

			// Create the positioning parameters
			int maxDiameterDimension = Math.max(selectedDotDiameterPx, unselectedDotDiameterPx);
			int startMargin = i * (spacingBetweenDotsPx + unselectedDotDiameterPx);
			LayoutParams params = new LayoutParams(maxDiameterDimension, maxDiameterDimension);
			params.setMargins(startMargin, 0, 0, 0);

			// RTL layout support
			if (Build.VERSION.SDK_INT >= 17) {
				params.setMarginStart(startMargin);
			}

			// Apply the positioning parameters and add the dot to the UI
			dot.setLayoutParams(params);
			addView(dot);

			// Keep a record of the dot for later use
			dots.add(i, dot);
		}
	}

	/**
	 * Forces the View to entirely destroy and recreate the UI.
	 */
	public void redrawDots() {
		reflectParametersInView();
	}

	/**
	 * Sets the diameter to use for the unselected dots.
	 *
	 * @param unselectedDotDiameterPx
	 * 		the diameter to use, measured in pixels
	 */
	public void setUnselectedDotDiameterPx(final int unselectedDotDiameterPx) {
		this.unselectedDotDiameterPx = unselectedDotDiameterPx;
		reflectParametersInView();
	}

	/**
	 * Sets the diameter to use for the unselected dots.
	 *
	 * @param unselectedDotDiameterDp
	 * 		the diameter to use, measured in display-independent pixels
	 */
	public void setUnselectedDotDiameterDp(final int unselectedDotDiameterDp) {
		final int diameterPx = DimensionHelper.dpToPx(unselectedDotDiameterDp, getContext());
		setUnselectedDotDiameterPx(diameterPx);
	}

	/**
	 * @return the current unselected dot diameter, measured in pixels
	 */
	public int getUnselectedDotDiameter() {
		return unselectedDotDiameterPx;
	}

	/**
	 * Sets the diameter to use for the selected dot.
	 *
	 * @param selectedDotDiameterPx
	 * 		the diameter to use, measured in pixels
	 */
	public void setSelectedDotDiameterPx(final int selectedDotDiameterPx) {
		this.selectedDotDiameterPx = selectedDotDiameterPx;
		reflectParametersInView();
	}

	/**
	 * Sets the diameter to use for the selected dot.
	 *
	 * @param selectedDotDiameterDp
	 * 		the diameter to use, measured in display-independent pixels
	 */
	public void setSelectedDotDiameterDp(final int selectedDotDiameterDp) {
		final int diameterPx = DimensionHelper.dpToPx(selectedDotDiameterDp, getContext());
		setSelectedDotDiameterPx(diameterPx);
	}

	/**
	 * @return the current selected dot diameter, measured in pixels
	 */
	public int getSelectedDotDiameter() {
		return selectedDotDiameterPx;
	}

	/**
	 * Sets the color to use for the unselected dots.
	 *
	 * @param unselectedDotColor
	 * 		the color to use, as an ARGB hex code
	 */
	public void setUnselectedDotColor(final int unselectedDotColor) {
		this.unselectedDotColor = unselectedDotColor;
		reflectParametersInView();
	}

	/**
	 * @return the current unselected dot color, as an ARGB hex code
	 */
	public int getUnselectedDotColor() {
		return unselectedDotColor;
	}

	/**
	 * Sets the color to use for the selected dot.
	 *
	 * @param selectedDotColor
	 * 		the color to use, as an ARGB hex code
	 */
	public void setSelectedDotColor(final int selectedDotColor) {
		this.selectedDotColor = selectedDotColor;
		reflectParametersInView();
	}

	/**
	 * @return the current unselected dot color, as an ARGB hex code
	 */
	public int getSelectedDotColor() {
		return selectedDotColor;
	}

	/**
	 * Sets the spacing between dots. The spacing is measured as the distance between the edges of
	 * consecutive unselected dots. The spacing is applied as if all dots are unselected, and when a
	 * dot changes size to become selected, it stays fixed at its centre.
	 *
	 * @param spacingBetweenDotsPx
	 * 		the spacing to use, measured in pixels
	 */
	public void setSpacingBetweenDotsPx(final int spacingBetweenDotsPx) {
		this.spacingBetweenDotsPx = spacingBetweenDotsPx;
		reflectParametersInView();
	}

	/**
	 * Sets the spacing between dots. The spacing is measured as the distance between the edges of
	 * consecutive unselected dots. The spacing is applied as if all dots are unselected, and when a
	 * dot changes size to become selected, it stays fixed at its centre.
	 *
	 * @param spacingBetweenDotsDp
	 * 		the spacing to use, measured in display-independent pixels
	 */
	public void setSpacingBetweenDotsDp(final int spacingBetweenDotsDp) {
		final int spacingPx = DimensionHelper.dpToPx(spacingBetweenDotsDp, getContext());
		setSpacingBetweenDotsPx(spacingPx);
	}

	/**
	 * Returns the current spacing between dots. The spacing is measured as the distance between the
	 * edges of consecutive unselected dots. The spacing is applied as if all dots are unselected,
	 * and when a dot changes size to become selected, it stays fixed at its centre.
	 *
	 * @return the current spacing, measured in pixels
	 */
	public int getSpacingBetweenDots() {
		return spacingBetweenDotsPx;
	}

	@Override
	public void setSelectedItem(final int index, final boolean animate) {
		try {
			dots.get(this.selectedDotIndex).setInactive(animate);
			dots.get(index).setActive(animate);
		} catch (IndexOutOfBoundsException e) {
			// Catch and rethrow the exception to avoid showing the internal implementation
			throw new IndexOutOfBoundsException();
		}

		this.selectedDotIndex = index;
	}

	@Override
	public int getSelectedItemIndex() {
		return selectedDotIndex;
	}

	@Override
	public void setNumberOfItems(final int numberOfItems) {
		numberOfDots = numberOfItems;
		reflectParametersInView();
	}

	@Override
	public int getNumberOfItems() {
		return numberOfDots;
	}

	@Override
	public void setTransitionDuration(final int transitionDurationMs) {
		dotTransitionDuration = transitionDurationMs;
		reflectParametersInView();
	}

	@Override
	public int getTransitionDuration() {
		return dotTransitionDuration;
	}

	@Override
	public void setVisibility(final boolean show) {
		setVisibility(show ? VISIBLE : INVISIBLE);
	}

	@Override
	public boolean isVisible() {
		return (getVisibility() == VISIBLE);
	}
}