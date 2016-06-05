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

/**
 * A visual representation of the selected item in a finite collection.
 */
public interface SelectionIndicator {
	/**
	 * Sets the index of the currently selected item and updates the UI to reflect the change. The
	 * {@code index} parameter must not be less than 0, and it must not be equal to or greater than
	 * the current size of the indicator.
	 *
	 * @param index
	 * 		the index of the selected item, counting from zero
	 * @param animate
	 * 		whether or not the UI change should be animated
	 */
	void setSelectedItem(int index, boolean animate);

	/**
	 * @return the index of the currently selected item, counting from zero
	 */
	int getSelectedItemIndex();

	/**
	 * Sets the number of items represented by this indicator and updates the UI to reflect the
	 * change. This number should be equal to the size of the Collection being represented by the
	 * indicator.
	 *
	 * @param numberOfItems
	 * 		the number of items in the set this indicator represents, not negative
	 */
	void setNumberOfItems(int numberOfItems);

	/**
	 * @return the total number of items represented by this indicator
	 */
	int getNumberOfItems();

	/**
	 * Sets the duration to use when animating items between selected and unselected.
	 *
	 * @param durationMs
	 * 		the duration to use, measured in milliseconds
	 */
	void setTransitionDuration(int durationMs);

	/**
	 * @return the duration to use when animating items between selected and unselected
	 */
	int getTransitionDuration();

	/**
	 * Changes the visibility of this indicator.
	 *
	 * @param show
	 * 		true to make this indicator visible, false to make it invisible
	 */
	void setVisibility(boolean show);

	/**
	 * @return true if this indicator is currently visible, false otherwise
	 */
	boolean isVisible();
}
