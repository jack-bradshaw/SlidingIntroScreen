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

package com.matthewtamlin.sliding_intro_screen_library.background;

import android.view.View;

import com.matthewtamlin.android_utilities_library.helpers.ColorHelper;

/**
 * A ColorBlender is a BackgroundManager which presents a variable color as the background. Each
 * page has a color associated with it, which will be displayed when that page is fully selected.
 * When the scrolling is between pages, the colors are blended together to create a continuous color
 * effect.
 */
public class ColorBlender implements BackgroundManager {
	/**
	 * The colors to use for the backgrounds.
	 */
	private final int[] colors;

	/**
	 * Constructs a new ColorBlender. The length of the array must match the number of pages in the
	 * IntroActivity this BackgroundManager is used with. The colors are mapped to the pages using
	 * the ordering of the array (e.g. page 1 maps to the color at index 0).
	 *
	 * @param colors
	 * 		the background colors to use, not null
	 * @throws IllegalArgumentException
	 * 		if {@code colors} is null or if the length of colors is less than 1
	 */
	public ColorBlender(final int[] colors) {
		if (colors == null) {
			throw new IllegalArgumentException("colors cannot be null");
		} else if (colors.length == 0) {
			throw new IllegalArgumentException("colors must have at least one element");
		}

		this.colors = colors;
	}

	@Override
	public void updateBackground(final View background, final int index, final float offset) {
		// Check that index doesn't exceed array bounds before progressing
		if (index > colors.length - 1) {
			throw new IllegalArgumentException("index is too large");
		}

		// The left color is always directly referenced by index
		final int colorLeft = colors[index];

		// Must be careful to avoid index out of bounds exceptions
		final boolean isLast = index == colors.length - 1;
		final int colorRight = isLast ? colors[index] : colors[index + 1];

		// Blend the colors to make the final background color
		background.setBackgroundColor(ColorHelper.blendColors(colorLeft, colorRight, offset));
	}
}