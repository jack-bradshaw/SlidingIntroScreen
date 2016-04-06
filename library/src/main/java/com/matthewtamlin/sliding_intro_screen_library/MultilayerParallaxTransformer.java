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

package com.matthewtamlin.sliding_intro_screen_library;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;

public class MultilayerParallaxTransformer implements ViewPager.PageTransformer {
	private final HashMap<Integer, Float> layers = new HashMap<>();
	private final HashMap<Integer, View> storedViews = new HashMap<>();

	@Override
	public void transformPage(final View page, final float position) {
		final boolean pageIsSelected = (position == 0f);
		final boolean pageIsScrolling = (position > -1f && position < 1f);

		if (pageIsSelected) {
			page.invalidate();
		} else if (pageIsScrolling) {
			for (Integer id : layers.keySet()) {
				final View v = getView(page, id);

				if (v != null) {
					v.setTranslationX(page.getWidth() * position * layers.get(id) / 2);
				}
			}
		}
	}

	public static MultilayerParallaxTransformer newInstance() {
		return new MultilayerParallaxTransformer();
	}

	public static MultilayerParallaxTransformer newInstance(HashMap<Integer, Float> layers) {
		final MultilayerParallaxTransformer transformer = new MultilayerParallaxTransformer();

		for (Integer i : layers.keySet()) {
			transformer.layers.put(i, layers.get(i));
		}

		return transformer;
	}

	public MultilayerParallaxTransformer withLayer(int id, float speed) {
		layers.put(id, speed);
		return this;
	}


	//TODO refactor
	private View getView(View rootView, int id) {
		View view = storedViews.get(id);

		if (view == null) {
			view = rootView.findViewById(id);
			storedViews.put(id, view);
		}

		return view;
	}
}
