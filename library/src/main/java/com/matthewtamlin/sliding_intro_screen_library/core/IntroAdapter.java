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

package com.matthewtamlin.sliding_intro_screen_library.core;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.matthewtamlin.android_utilities_library.collections.ArrayListWithCallbacks;
import com.matthewtamlin.android_utilities_library.collections.ArrayListWithCallbacks
		.OnListChangedListener;

/**
 * Adapts a collection of Fragments so that they can be displayed in an {@link
 * android.support.v4.view.ViewPager ViewPager}. Instances of this class automatically listen for
 * changes to the dataset.
 */
public class IntroAdapter extends FragmentPagerAdapter {
	/**
	 * Used to identify this class during debugging.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "[IntroAdapter]";

	/**
	 * The dataset of pages to adapt.
	 */
	private final ArrayListWithCallbacks<Fragment> pages;

	/**
	 * Receive list change events from the dataset. Using a delegate hides the internal
	 * implementation from the public class signature.
	 */
	private final OnListChangedListener listChangeListener = new OnListChangedListener() {
		@Override
		public void onItemAdded(final ArrayListWithCallbacks list, final Object itemAdded,
				final int index) {
			notifyDataSetChanged();
		}

		@Override
		public void onItemRemoved(final ArrayListWithCallbacks list, final Object itemRemoved,
				final int index) {
			notifyDataSetChanged();
		}

		@Override
		public void onListCleared(final ArrayListWithCallbacks list) {
			notifyDataSetChanged();
		}
	};

	/**
	 * Constructs a new IntroAdapter instance.
	 *
	 * @param fm
	 * 		the FragmentManager for the Context this adapter is operating in
	 * @param pages
	 * 		the dataset of pages to adapt, null for an empty dataset
	 */
	public IntroAdapter(final FragmentManager fm, final ArrayListWithCallbacks<Fragment> pages) {
		super(fm);

		if (pages == null) {
			this.pages = new ArrayListWithCallbacks<>();
		} else {
			this.pages = pages;
		}

		this.pages.addOnListChangedListener(listChangeListener);
	}

	/**
	 * @return the dataset of this adapter, not null
	 */
	public ArrayListWithCallbacks<Fragment> getPages() {
		return pages;
	}

	@Override
	public Fragment getItem(final int position) {
		return pages.get(position);
	}

	@Override
	public int getCount() {
		return pages.size();
	}
}