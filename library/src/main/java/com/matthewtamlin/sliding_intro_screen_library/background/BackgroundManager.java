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
	 * <p>
	 * The leftPageIndex and offset parameters can be used to create backgrounds which depend on the
	 * current scroll position. When scrolling between pages, there will always be a left page and a
	 * right page. The index parameter is always with reference to the left page. The offset varies
	 * between 0 and 1 as the right pages transitions to being the selected page. When the offset is
	 * 0, the left page is entirely selected and the right page is unselected. When the offset is
	 * 0.5, the left page has been half scrolled out and the right page has been half scrolling in.
	 * When the offset is 1, the right page is entirely selected and the left page is unselected.
	 *
	 * @param background
	 * 		the View which is furthest back in the View hierarchy of the calling IntroActivity, not
	 * 		null
	 * @param index
	 * 		the index of the current left page
	 * @param offset
	 * 		the proportion of the right page , as a value between 0 and 1
	 */
	void updateBackground(View background, int index, float offset);
}
