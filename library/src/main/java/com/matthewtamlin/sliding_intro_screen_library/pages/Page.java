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

package com.matthewtamlin.sliding_intro_screen_library.pages;

import android.graphics.Color;
import android.support.v4.app.Fragment;

import com.matthewtamlin.sliding_intro_screen_library.core.IntroActivity;


/**
 * A single page to display in an {@link IntroActivity}. Each page stores a color it would prefer to
 * have drawn behind it when displayed, which allows the background color of the hosting
 * IntroActivity to be transitioned as its pages are scrolled. Subclass this class to define the
 * appearance and behaviour of your pages. To disable this behaviour, set the actually background
 * color as usual.
 *
 * @deprecated IntroActivity now supports Fragments directly, there is no need to use Pages
 */
@Deprecated
public class Page extends Fragment {
	/**
	 * Used to identify this class during debugging.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "[Page]";

	/**
	 * The color this page would prefer to have drawn behind it when displayed. This is distinct
	 * from the background color of this Fragment.
	 */
	private int desiredBackgroundColour = Color.TRANSPARENT;

	/**
	 * Required default empty constructor.
	 */
	public Page() {
		super();
	}

	/**
	 * @return a new Page instance
	 */
	public static Page newInstance() {
		return new Page();
	}

	/**
	 * Sets the color this Page would prefer to have drawn behind it when displayed.
	 *
	 * @param color
	 * 		the desired background color
	 */
	public void setDesiredBackgroundColor(final int color) {
		this.desiredBackgroundColour = color;
	}

	/**
	 * Returns the color this Page would prefer to have drawn behind it when displayed. If no color
	 * has been supplied to {@link #setDesiredBackgroundColor(int)}, then transparent (0x00000000)
	 * is returned.
	 *
	 * @return the desired background color
	 */
	public int getDesiredBackgroundColor() {
		return desiredBackgroundColour;
	}
}