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

package com.matthewtamlin.sliding_intro_screen_library.transformers;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.matthewtamlin.sliding_intro_screen_library.R;

import java.util.HashMap;

/**
 * Transforms a ParallaxPage by translating its views left and right when scrolling. Front images
 * are translated faster than back images, which creates a parallax scrolling effect. This class is
 * designed to function with ParallaxPage elements. Use with other types of pages will yield default
 * ViewPager scrolling behaviour.
 * @deprecated use {@link MultiViewParallaxTransformer} instead
 */
@Deprecated
public final class ParallaxTransformer implements ViewPager.PageTransformer {
	/**
	 * Used to identify this class during debugging.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "[ParallaxTransformer]";

	/**
	 * Stores references to the views to animate. Using this data-structure is more efficient than
	 * frequent calls to {@code findViewById(int)}.
	 */
	private final HashMap<View, ImageView> cachedViews = new HashMap<>();

	@Override
	public void transformPage(final View pageRootView, final float position) {
		ImageView frontImageHolder = getFrontImageHolder(pageRootView);

		final boolean pageIsSelected = (position == 0f);
		final boolean pageIsScrolling = (position > -1f && position < 1f);

		if (pageIsSelected) {
			pageRootView.invalidate();
		} else if (pageIsScrolling) {
			// This value creates a parallax effect
			final float n = 0.5f;

			// Transform front image holder
			if (frontImageHolder != null) {
				frontImageHolder.setTranslationX(pageRootView.getWidth() * position * n / 2);
			}
		}
	}

	/**
	 * Returns the front image of {@code pageRootView}, null if it does not exist. Using this method
	 * is more efficient than calling {@code findViewById(int)} each time the View is needed.
	 *
	 * @param pageRootView
	 * 		the root view of the ParallaxPage to transform
	 * @return the front image holder of the ParallaxPage to transform
	 */
	private ImageView getFrontImageHolder(final View pageRootView) {
		ImageView frontImage = cachedViews.get(pageRootView);

		if (frontImage == null) {
			frontImage = (ImageView) pageRootView.findViewById(R.id
					.page_fragment_imageHolderFront);
			cachedViews.put(pageRootView, frontImage);
		}

		return frontImage;
	}
}