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
 * Displays a set of dots to indicate the selected item in a set. The properties and behaviours of
 * the dots can be configured using the methods of this class. The default appearance and behaviour
 * replicate the functionality of indicators in Google-made apps.
 */
public class DotIndicator extends RelativeLayout implements SelectionIndicator {
	/**
	 * Used to identify this class during debugging.
	 */
	private static final String TAG = "[SelectionIndicator]";

	/**
	 * Default value for the attribute {@code numberOfDots}.
	 */
	private static final int DEFAULT_NUMBER_OF_DOTS = 1;

	/**
	 * Default value for the {@code selectedDotIndex} attribute.
	 */
	private static final int DEFAULT_SELECTED_DOT_INDEX = 0;

	/**
	 * Default value for the {@code unselectedDotDiameter} attribute.
	 */
	private static final int DEFAULT_UNSELECTED_DOT_DIAMETER_DP = 6;

	/**
	 * Default value for the {@code selectedDotDiameter} attribute.
	 */
	private static final int DEFAULT_SELECTED_DOT_DIAMETER_DP = 9;

	/**
	 * Default value for the {@code unselectedDotColor} attribute.
	 */
	private static final int DEFAULT_UNSELECTED_DOT_COLOR = Color.WHITE;

	/**
	 * Default value for the {@code selectedDotColor} attribute.
	 */
	private static final int DEFAULT_SELECTED_DOT_COLOR = Color.WHITE;

	/**
	 * Default value for the {@code spacingBetweenDots} attribute.
	 */
	private static final int DEFAULT_SPACING_BETWEEN_DOTS_DP = 7;

	/**
	 * Default value for the {@code dotTransitionDuration} attribute.
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
	 * The diameter to use for each unselected Dot.
	 */
	private int unselectedDotDiameterPx;

	/**
	 * The diameter to use for the selected Dot.
	 */
	private int selectedDotDiameterPx;

	/**
	 * The ARGB hex code of the color to use for each unselected Dot.
	 */
	private int unselectedDotColor;

	/**
	 * The ARGB hex code of the colour to use for the selected Dot.
	 */
	private int selectedDotColor;

	/**
	 * The spacing between the edges of consecutive unselected dots.
	 */
	private int spacingBetweenDotsPx;

	/**
	 * The length of time for transitioning a Dot between selected and unselected, measured in
	 * milliseconds.
	 */
	private int dotTransitionDuration;

	/**
	 * The Dot elements shown in this View.
	 */
	private final ArrayList<Dot> dots = new ArrayList<>();

	/**
	 * Constructs a new SelectionIndicator instance. The following default parameters are used:<p/>
	 * <li>numberOfDots: 1</li> <li>selectedDotIndex: 0</li> <li>unselectedDotDiameter: 6dp</li>
	 * <li>selectedDotDiameter: 9dp</li> <li>unselectedDotColor: opaque white (i.e. ARGB
	 * 0xFFFFFFFF)</li> <li>selectedDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>spacingBetweenDots: 7dp</li> <li>dotTransitionDuration: 200ms</li>
	 *
	 * @param context
	 * 		the context in which this SelectionIndicator is operating
	 */
	public DotIndicator(final Context context) {
		super(context);
		init(null);
	}

	/**
	 * Constructs a new SelectionIndicator instance. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<p/> <li>numberOfDots: 1</li>
	 * <li>selectedDotIndex: 0</li> <li>unselectedDotDiameter: 6dp</li> <li>selectedDotDiameter:
	 * 9dp</li> <li>unselectedDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>selectedDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li> <li>spacingBetweenDots:
	 * 7dp</li> <li>dotTransitionDuration: 200ms</li>
	 *
	 * @param context
	 * 		the context in which this SelectionIndicator is operating
	 * @param attrs
	 * 		configuration attributes
	 */
	public DotIndicator(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	/**
	 * Constructs a new SelectionIndicator instance. If an attribute specific to this class is not
	 * provided, the relevant default is used. The defaults are:<p/> <li>numberOfDots: 1</li>
	 * <li>selectedDotIndex: 0</li> <li>unselectedDotDiameter: 6dp</li> <li>selectedDotDiameter:
	 * 9dp</li> <li>unselectedDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li>
	 * <li>selectedDotColor: opaque white (i.e. ARGB 0xFFFFFFFF)</li> <li>spacingBetweenDots:
	 * 7dp</li> <li>dotTransitionDuration: 200ms</li>
	 *
	 * @param context
	 * 		the context in which this SelectionIndicator is operating
	 * @param attrs
	 * 		configuration attributes
	 * @param defStyleAttr
	 * 		an attribute in the current theme that contains a reference to a style resource that
	 * 		supplies defaults values for the StyledAttributes, or 0 to not look for defaults
	 */
	public DotIndicator(final Context context, final AttributeSet attrs, final int
			defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}

	/**
	 * Initialises this DotIndicator. The provided attributes are read and assigned to member
	 * variables, and the UI is created. This method should only be invoked during construction.
	 *
	 * @param attrs
	 * 		configuration attributes
	 */
	private void init(final AttributeSet attrs) {
		// Use a TypedArray to process attrs
		final TypedArray attributes =
				getContext().obtainStyledAttributes(attrs, R.styleable.DotIndicator);

		// Need to convert all default dimensions to px from dp
		final int defaultSelectedDotDiameterPx =
				DimensionHelper.dpToPx(DEFAULT_SELECTED_DOT_DIAMETER_DP, getContext());
		final int defaultUnselectedDotDiameterPx = DimensionHelper.dpToPx(
				DEFAULT_UNSELECTED_DOT_DIAMETER_DP, getContext());
		final int defaultSpacingBetweenDotsPx =
				DimensionHelper.dpToPx(DEFAULT_SPACING_BETWEEN_DOTS_DP, getContext());

		// Assign attributes to member variables
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
	 * Constructs and displays dots based on current member variables. Calling this method will
	 * remove and recreate all existing dots.
	 */
	private void reflectParametersInView() {
		dots.clear();
		removeAllViews();

		for (int i = 0; i < numberOfDots; i++) {
			Dot dot = new Dot(getContext());
			dot.setInactiveDiameterPx(unselectedDotDiameterPx).setActiveDiameterPx(
					selectedDotDiameterPx)
					.setActiveColor(selectedDotColor).setInactiveColor(unselectedDotColor)
					.setTransitionDuration(dotTransitionDuration);

			if (i == selectedDotIndex) {
				dot.setActive(false);
			} else {
				dot.setInactive(false);
			}

			int maxDiameterDimension = Math.max(selectedDotDiameterPx, unselectedDotDiameterPx);
			int startMargin = i * (spacingBetweenDotsPx + unselectedDotDiameterPx);
			LayoutParams params = new LayoutParams(maxDiameterDimension, maxDiameterDimension);
			params.setMargins(startMargin, 0, 0, 0);

			if (Build.VERSION.SDK_INT >= 17) {
				params.setMarginStart(startMargin);
			}

			dot.setLayoutParams(params);
			dots.add(i, dot);
			addView(dot);
		}
	}

	/**
	 * Forces a redraw of all dots. All existing dots are destroyed and recreated. The new dots will
	 * reflect the current parameters.
	 */
	public void redrawDots() {
		reflectParametersInView();
	}

	/**
	 * Sets the diameter to use for each unselected dot.
	 *
	 * @param unselectedDotDiameterPx
	 * 		the diameter to use, measured in pixels
	 */
	public void setUnselectedDotDiameterPx(final int unselectedDotDiameterPx) {
		this.unselectedDotDiameterPx = unselectedDotDiameterPx;
		reflectParametersInView();
	}

	/**
	 * Sets the diameter to use for each unselected dot.
	 *
	 * @param unselectedDotDiameterDp
	 * 		the diameter to use, measured in display-independent pixels
	 */
	public void setUnselectedDotDiameterDp(final int unselectedDotDiameterDp) {
		final int diameterPx = DimensionHelper.dpToPx(unselectedDotDiameterDp, getContext());
		setUnselectedDotDiameterPx(diameterPx);
	}

	/**
	 * Returns the diameter currently used for each unselected dot.
	 *
	 * @return the diameter currently used, measured in pixels
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
	 * Returns the diameter currently used for the selected dot.
	 *
	 * @return the diameter currently used, measured in pixels
	 */
	public int getSelectedDotDiameter() {
		return selectedDotDiameterPx;
	}

	/**
	 * Sets the color to use for each unselected dot.
	 *
	 * @param unselectedDotColor
	 * 		the color to use, as an ARGB hex code
	 */
	public void setUnselectedDotColor(final int unselectedDotColor) {
		this.unselectedDotColor = unselectedDotColor;
		reflectParametersInView();
	}

	/**
	 * Returns the color currently used for each unselected dot.
	 *
	 * @return the color currently used, as an ARGB hex code
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
	 * Returns the color currently used for the selected dot.
	 *
	 * @return the color currently used, as an ARGB hex code
	 */
	public int getSelectedDotColor() {
		return selectedDotColor;
	}

	/**
	 * Sets the spacing between consecutive dots, as measured from the edges of the unselected
	 * dots.
	 *
	 * @param spacingBetweenDotsPx
	 * 		the spacing to use, measured in pixels
	 */
	public void setSpacingBetweenDotsPx(final int spacingBetweenDotsPx) {
		this.spacingBetweenDotsPx = spacingBetweenDotsPx;
		reflectParametersInView();
	}

	/**
	 * Sets the spacing between consecutive dots, as measured from the edges of the unselected
	 * dots.
	 *
	 * @param spacingBetweenDotsDp
	 * 		the spacing to use, measured in display-independent pixels
	 */
	public void setSpacingBetweenDotsDp(final int spacingBetweenDotsDp) {
		final int spacingPx = DimensionHelper.dpToPx(spacingBetweenDotsDp, getContext());
		setSpacingBetweenDotsPx(spacingPx);
	}

	/**
	 * Returns the spacing between consecutive dots, as measured from the edges of the unselected
	 * dots.
	 *
	 * @return the current spacing, measured in pixels
	 */
	public int getSpacingBetweenDots() {
		return spacingBetweenDotsPx;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException
	 * 		if {@code index} is too large for the current indicator size
	 * @throws IllegalArgumentException
	 * 		if {@code index} is less than 0
	 */
	@Override
	public void setSelectedItem(final int index, final boolean animate) {
		if (selectedDotIndex < 0) {
			throw new IllegalArgumentException("newActiveItemIndex must be greater than 0");
		}

		try {
			dots.get(this.selectedDotIndex).setInactive(animate);
			dots.get(index).setActive(animate);
		} catch (IndexOutOfBoundsException e) {
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
		this.numberOfDots = numberOfItems;
		reflectParametersInView();
	}

	@Override
	public int getNumberOfItems() {
		return numberOfDots;
	}

	@Override
	public void setTransitionDuration(final int transitionDurationMs) {
		this.dotTransitionDuration = transitionDurationMs;
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