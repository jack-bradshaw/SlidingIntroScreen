package com.matthewtamlin.sliding_intro_screen_library.background;

import android.view.View;

import com.matthewtamlin.android_utilities_library.helpers.ColorHelper;

/**
 * A ColorBlender is a BackgroundManager which mixes colors together to create a continuous
 * scrolling effect when scrolling through the pages of an IntroActivity. The constructor accepts
 * an array of ARGB hex codes which are used for the backgrounds. The size of the array must
 * match the number of pages in the IntroActivity, or else exceptions will occur when the pages
 * are scrolled.
 */
public class ColorBlender implements BackgroundManager {
	/**
	 * The colors to use for the backgrounds.
	 */
	private final int[] colors;

	/**
	 * Constructs a new ColorBlender.
	 *
	 * @param colors
	 * 		the colors to use for the backgrounds, not null
	 * @throws IllegalArgumentException
	 * 		if {@code colors} is null
	 */
	public ColorBlender(int[] colors) {
		if (colors == null) {
			throw new IllegalArgumentException("colors cannot be null");
		} else if (colors.length <= 0) {
			throw new IllegalArgumentException("colors must have at least one element");
		}

		this.colors = colors;
	}

	@Override
	public void updateBackground(View background, int index, float offset) {
		// Check that index doesn't exceed array bounds before progressing
		if (index > colors.length - 1) {
			throw new IllegalArgumentException("index is too large");
		}

		// The left color is always directly referenced by index
		final int colorLeft = colors[index];

		// The right color may be the same as the left color if the last index has been reached
		final boolean isLast = index == colors.length - 1;
		final int colorRight = isLast ? colors[index] : colors[index + 1];

		// Blend the colors to make the final background color
		background.setBackgroundColor(ColorHelper.blendColors(colorLeft, colorRight, 1f - offset));
	}
}