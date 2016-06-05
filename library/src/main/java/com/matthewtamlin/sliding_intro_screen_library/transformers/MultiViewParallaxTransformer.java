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
 * A ViewPager transformer which can apply parallax effects to the Views it transforms. By assigning
 * a parallax factor to a resource ID, all Views with that resource ID will experience a parallax
 * effect. A parallax factor is a scale factor, which determines how Views are positioned compared
 * to the nominal position.
 * <p/>
 * For example, if a parallax factor of 1.2 is provided for the resource ID {@code R.id.myview},
 * then all Views with that resource ID will have a 20% parallax effect. This means that the Views
 * will appear to scroll 20% faster than views with no parallax effect.
 * <p/>
 */
public class MultiViewParallaxTransformer implements ViewPager.PageTransformer {
	/**
	 * Stores the parallax factors, and maps each parallax factor to the resource ID of the View(s)
	 * it should be applied to. If a resource ID does not exist in the keyset, then Views with that
	 * ID should have a parallax factor of 1 applied (i.e normal scrolling without parallax).
	 */
	private final HashMap<Integer, Float> parallaxFactors = new HashMap<>();

	/**
	 * Maps the root View of each page to a ChildViewCache. This allows the child Views of each page
	 * to be efficiently accessed, which is necessary considering how frequently the Views are
	 * accessed when the transformer is in use.
	 */
	private final HashMap<View, ChildViewCache> savedViews = new HashMap<>();

	@Override
	public void transformPage(final View page, final float position) {
		// The status of the transformation
		final boolean pageIsSelected = (position == 0f);
		final boolean pageIsScrolling = (-1f < position && position < 1f);

		if (pageIsSelected) {
			page.invalidate(); // Make sure page displays correctly
		} else if (pageIsScrolling) {
			// For every resource ID which has been nominated for a parallax factor
			for (final Integer id : parallaxFactors.keySet()) {
				// Check to see if the current page has a View with that resource ID
				final View viewToTransform = getViewToTransform(page, id);

				// If the parallax factor is applicable, apply the parallax effect
				if (viewToTransform != null) {
					final float parallaxFactor = parallaxFactors.get(id);

					// The displacement which is automatically applied by the transformer superclass
					final float nominalDisplacement = (page.getWidth() / 2) * position;

					// Subtract 1 from the parallax factor because the View is already moved the
					// nominal displacement by the transformer superclass
					final float modifiedDisplacement = nominalDisplacement * (parallaxFactor - 1);

					// Apply the extra displacement using the X translation method
					viewToTransform.setTranslationX(modifiedDisplacement);
				}
			}
		}
	}

	/**
	 * Applies a parallax effect to all Views with the provided resource ID. The parallax factor
	 * determines how fast the affected views are translated, relative to a View with no parallax
	 * effect. It is recommended that this method not be called while the Views are being
	 * transformed.
	 * <p/>
	 * Parallax factors less than 1 cause the affected Views to move slower than non-affected Views,
	 * and parallax factors greater than 1 cause the affected Views to move faster than non-affected
	 * Views. Parallax factors equal to 1 have no effect. Note that that parallax factors less than
	 * 1 may not display properly. ViewPager may clip Views which translate slower than the
	 * boundaries of the pages.
	 * <p/>
	 * For example, a parallax factor of 1.2 will cause the affected Views to move 20% faster than
	 * non-affected Views, and a parallax factor of 0.8 will cause the affected Views to move 20%
	 * slower than non-affected Views.
	 *
	 * @param id
	 * 		the resource ID of the views to apply the parallax effect to
	 * @param parallaxFactor
	 * 		the parallax factor to apply
	 * @return this MultiViewParallaxTransformer
	 */
	public MultiViewParallaxTransformer withParallaxView(final int id, final float parallaxFactor) {
		parallaxFactors.put(id, parallaxFactor);
		return this;
	}

	/**
	 * Removes any existing parallax effect from all Views with the provided resource id. It is
	 * recommended that this method not be called while the Views are being transformed.
	 *
	 * @param id
	 * 		the resource ID of the Views to remove the effect from
	 * @return this MultiViewParallaxTransformer
	 */
	public MultiViewParallaxTransformer withoutParallaxView(final int id) {
		parallaxFactors.remove(id);
		return this;
	}

	/**
	 * Returns a reference to the child View of {@code parentView} with the resource ID of {@code
	 * id}. Using this method is more efficient that frequent calls to {@link
	 * View#findViewById(int)}.
	 *
	 * @param rootView
	 * 		the View to get the child view from, not null
	 * @param id
	 * 		the resource ID of the child View
	 * @return the child view of {@code parentView} with the resource ID of {@code id}, null if no
	 * such child view exists
	 * @throws IllegalArgumentException if {@code rootView} is null
	 */
	private View getViewToTransform(final View rootView, final int id) {
		if (rootView == null) {
			throw new IllegalArgumentException("rootView cannot be null");
		}

		// Create a new ChildViewCache if none exists for the root View
		if (!savedViews.containsKey(rootView)) {
			savedViews.put(rootView, new ChildViewCache(rootView));
		}

		// Use the ChildViewCache to efficiently access the requested View
		return savedViews.get(rootView).getChildView(id);
	}

}

/**
 * A cache for efficiently retrieving the children of a single parent View. Using this class is more
 * efficient that frequently calling {@link View#findViewById(int)}.
 */
class ChildViewCache {
	/**
	 * The parent of the Views to cache.
	 */
	private final View parentView;

	/**
	 * Stores the children of the parent View, so that they can be efficiently retrieved by their
	 * resource ID.
	 */
	private final HashMap<Integer, View> cachedViews = new HashMap<>();

	/**
	 * Constructs a new ChildViewCache instance.
	 *
	 * @param parentView
	 * 		the parent View of the Views to cache, not null
	 * @throws IllegalArgumentException
	 * 		if {@code parentView} is null
	 */
	public ChildViewCache(final View parentView) {
		if (parentView == null) {
			throw new IllegalArgumentException("parentView cannot be null");
		}

		this.parentView = parentView;
	}

	/**
	 * Provides efficient access to the children of the parent View provided to the constructor. If
	 * the View being accessed has not yet been cached, then it will be accessed using {@link
	 * View#findViewById(int)}. Subsequent calls will return the cached View.
	 *
	 * @param id
	 * 		the resource ID of the child View to access
	 * @return the child of the parent View with the provided resource ID, null if the parent View
	 * has no such child
	 */
	public final View getChildView(final int id) {
		if (!cachedViews.containsKey(id)) {
			cachedViews.put(id, parentView.findViewById(id));
		}

		return cachedViews.get(id);
	}

	/**
	 * @return the parent View
	 */
	public View getParentView() {
		return parentView;
	}

	/**
	 * Clears the cache entirely.
	 */
	public void reset() {
		cachedViews.clear();
	}
}
