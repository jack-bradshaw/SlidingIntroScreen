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

import java.util.HashMap;

/**
 * A ViewPager transformer which allows custom parallax effects to be applied to some or all of the
 * Views displayed in the pages. Parallax effects are assigned by mapping parallax factors to
 * resource IDs. A parallax factor is a scale factor, which determines how Views are positioned
 * compared to the drag position.
 * <p/>
 * For example, if a parallax factor of 1.2 is provided for the resource ID {@code R.id.myview},
 * then all Views with that resource ID will have a 20% parallax effect. This means that when the
 * page is scrolled in some direction by some number of pixels (call this variable x), then the
 * affected views will be scrolled 1.2*x pixels in the same direction.
 * <p/>
 * This class can safely be used with ViewPager instances where not all pages have the same
 * views/layout.
 */
public class MultiViewParallaxTransformer implements ViewPager.PageTransformer {
	/**
	 * Stores the resource ID for each view to transform, and maps each ID to a parallax effect
	 * factor.
	 */
	private final HashMap<Integer, Float> parallaxFactors = new HashMap<>();

	/**
	 * Maps the root view of each page to a SavedViewUtility so that view references can be
	 * efficiently retrieved.
	 */
	private final HashMap<View, SavedViewUtility> savedViews = new HashMap<>();

	@Override
	public void transformPage(final View page, final float position) {
		final boolean pageIsSelected = (position == 0f);
		final boolean pageIsScrolling = (position > -1f && position < 1f);

		if (pageIsSelected) {
			page.invalidate();
		} else if (pageIsScrolling) {
			for (final Integer id : parallaxFactors.keySet()) {
				final View viewToTransform = getViewToTransform(page, id);

				if (viewToTransform != null) {
					final float parallaxFactor = parallaxFactors.get(id);
					final float pageDisplacementFromCentrePixels = (page.getWidth() / 2) * position;

					viewToTransform.setTranslationX(
							pageDisplacementFromCentrePixels * (parallaxFactor - 1));
				}
			}
		}
	}

	/**
	 * Constructs a new MultiViewParallaxTransformer instance.
	 *
	 * @return the new instance, not null
	 */
	public static MultiViewParallaxTransformer newInstance() {
		return new MultiViewParallaxTransformer();
	}

	/**
	 * Sets this MultiViewParallaxTransformer to apply a parallax effect to all Views with the
	 * provided resource id. The parallax factor determines how fast the affected views are
	 * translated, relative to the normal scrolling speed. For example, consider a parallax factor
	 * of 1.2, and a ViewPager which has been dragged to the left by 100 pixels. Any views with this
	 * parallax factor will translate 120 pixels to the left.
	 * <p/>
	 * It is strongly recommended that parallax factors less than 1 not be used. Most
	 * implementations of ViewPager will clip any affected Views since they will translate slower
	 * than the boundaries of the pages. This recommendation is not enforced.
	 *
	 * @param id
	 * 		the resource ID of the views to apply the parallax effect to
	 * @param parallaxFactor
	 * 		determines how fast
	 * @return this MultiViewParallaxTransformer
	 */
	public MultiViewParallaxTransformer withParallaxView(int id, float parallaxFactor) {
		parallaxFactors.put(id, parallaxFactor);
		savedViews.clear(); // Recache all views to be safe
		return this;
	}

	/**
	 * Removes a parallax effect from all Views with the provided resource id.
	 *
	 * @param id
	 * 		the resource if of the Views to remove the effect from
	 * @return this MultiViewParallaxTransformer
	 */
	public MultiViewParallaxTransformer withoutParallaxView(int id) {
		parallaxFactors.remove(id);
		return this;
	}

	/**
	 * Returns a reference to the child view of {@code rootView} with the resource ID of {@code id}.
	 * Using this method is more efficient that frequent calls to {@link View#findViewById(int)}.
	 *
	 * @param rootView
	 * 		the view to get the child view from, not null
	 * @param id
	 * 		the resource ID of the child view
	 * @return the child view of {@code rootView} with the resource ID of {@code id}, or null if no
	 * such child view exists
	 */
	public View getViewToTransform(View rootView, int id) {
		if (rootView == null) {
			throw new IllegalArgumentException("rootView cannot be null");
		}

		if (!savedViews.containsKey(rootView)) {
			savedViews.put(rootView, new SavedViewUtility(rootView));
		}

		return savedViews.get(rootView).getChildView(id);
	}

}

/**
 * A utility for efficiently retrieving the children of a View. Using this class is more efficient
 * that frequently calling {@link View#findViewById(int)}.
 */
class SavedViewUtility {
	/**
	 * The view to retrieve the children from.
	 */
	private final View rootView;

	/**
	 * Stores the child Views of {@code view}. Each child view is mapped by its resource id.
	 */
	private final HashMap<Integer, View> cachedViews = new HashMap<>();

	/**
	 * Constructs a new SavedViewUtility instance. The View passed as an argument is set as the root
	 * view of this utility.
	 *
	 * @param rootView
	 * 		the View to get the children of with this utility, not null
	 */
	public SavedViewUtility(View rootView) {
		if (rootView == null) {
			throw new IllegalArgumentException("rootView cannot be null");
		}

		this.rootView = rootView;
	}

	/**
	 * Provides efficient access to the child Views of the root view of this utility.
	 *
	 * @param id
	 * 		the resource ID of the view to get
	 * @return the child view which has the provided resource id, or null if no such child view
	 * exists
	 */
	public final View getChildView(final int id) {
		if (cachedViews.containsKey(id)) {
			return cachedViews.get(id);
		} else {
			cachedViews.put(id, rootView.findViewById(id));
			return cachedViews.get(id);
		}
	}

	/**
	 * @return the View which is queried to get the child Views
	 */
	public View getRootView() {
		return rootView;
	}

	/**
	 * Calling this method will force each view to be retrieved using {@link View#findViewById(int)}
	 * next time {@link #getChildView(int)} is called for that view.
	 */
	public void reset() {
		cachedViews.clear();
	}
}
