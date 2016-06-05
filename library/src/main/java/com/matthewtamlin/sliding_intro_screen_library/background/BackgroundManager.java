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

import com.matthewtamlin.sliding_intro_screen_library.core.IntroActivity;

/**
 * Updates the background of an {@link IntroActivity} as the introduction is scrolled.
 */
public interface BackgroundManager {
	/**
	 * Performs the update operation. This method may be called frequently, therefore it should not
	 * perform long running operations or block the UI thread.
	 * <p/>
	 * The leftPageIndex and offset parameters can be used to create backgrounds which depend on the
	 * exact scroll position. When scrolling between pages, there will always be a left page and a
	 * right page. The index parameter always refers to the left page. The offset parameter always
	 * indicates what fraction of the right page is currently visible, and varies between 0 and 1.
	 * When the offset is 0, the left page is entirely selected and the right page is not visible.
	 * When the offset is 0.5, the left page has been half scrolled out and the right page has been
	 * half scrolling in. The offset will approach 1 but never reach it (if 1 were reached then
	 * index would increment and offset would reset to 0).
	 *
	 * @param background
	 * 		the View to draw the background on, not null
	 * @param index
	 * 		the index of the current left page
	 * @param offset
	 * 		the fraction of the right page which is currently visible, as a value between 0 and 1
	 */
	void updateBackground(View background, int index, float offset);
}
