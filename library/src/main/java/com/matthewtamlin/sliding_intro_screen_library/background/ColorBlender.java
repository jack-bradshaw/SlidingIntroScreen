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
	 * Constructs a new ColorBlender. The length of the array must match the number of pages in
	 * the IntroActivity this BackgroundManager is used with. The colors are mapped to the pages
	 * using the ordering of the array (e.g. page 1 maps to the color at index 0).
	 *
	 * @param colors
	 * 		the background colors to use, not null
	 * @throws IllegalArgumentException
	 * 		if {@code colors} is null
	 */
	public ColorBlender(int[] colors) {
		if (colors == null) {
			throw new IllegalArgumentException("colors cannot be null");
		} else if (colors.length == 0) {
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