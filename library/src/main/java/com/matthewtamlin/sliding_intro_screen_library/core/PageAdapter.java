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

import java.util.ArrayList;

/**
 * Adapts a collection of Fragments so that they can be displayed in an {@link
 * android.support.v4.view.ViewPager ViewPager}. Instances of this class automatically listen for
 * changes to the dataset.
 */
public class PageAdapter extends FragmentPagerAdapter {
	/**
	 * Used to identify this class during debugging.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "[PageAdapter]";

	/**
	 * The dataset of pages to adapt.
	 */
	private final ArrayList<Fragment> pages;

	/**
	 * Constructs a new PageAdapter instance.
	 *
	 * @param fm
	 * 		the FragmentManager for the Context this adapter is operating in
	 * @param pages
	 * 		the dataset of pages to adapt, null for an empty dataset
	 */
	public PageAdapter(final FragmentManager fm, final ArrayList<Fragment> pages) {
		super(fm);

		if (pages == null) {
			this.pages = new ArrayList<>();
		} else {
			this.pages = pages;
		}
	}

	/**
	 * @return the dataset of this adapter, not null
	 */
	public ArrayList<Fragment> getPages() {
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