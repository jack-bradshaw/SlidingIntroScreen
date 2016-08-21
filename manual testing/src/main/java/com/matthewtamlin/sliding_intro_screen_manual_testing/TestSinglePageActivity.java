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

package com.matthewtamlin.sliding_intro_screen_manual_testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.matthewtamlin.sliding_intro_screen_library.background.ColorBlender;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton;
import com.matthewtamlin.sliding_intro_screen_library.core.IntroActivity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Test the activity when multiple pages are shown. The activity should be displayed as follows:
 * <ul> <li>Window: Status bar shown.</li> <li>Pages: 1.</li> <li>Background: Blue.</li>
 * <li>Transformation: Not applicable.</li> <li>Page indicator: Shown with 1 dot.</li>
 * <li>Animations: Not applicable.</li> <li>Left button: Not displayed.</li> <li>Right button: Not
 * displayed.</li> <li>Final button: Displayed, showing "FINAL" with no icon.</li> </ul>
 */
public class TestSinglePageActivity extends IntroActivity {
	/**
	 * Used to identify this class during debugging.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "[TestSinglePageBeha...]";

	/**
	 * The desired background color of the page.
	 */
	private static final int BACKGROUND_COLOR = 0xff3366cc; // Blue

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBackgroundManager(new ColorBlender(new int[]{BACKGROUND_COLOR}));
	}

	@Override
	protected Collection<Fragment> generatePages(Bundle savedInstanceState) {
		final ArrayList<Fragment> pages = new ArrayList<>();
		pages.add(new Fragment());
		return pages;
	}

	@Override
	protected IntroButton.Behaviour generateFinalButtonBehaviour() {
		final Intent i = new Intent(this, EndActivity.class);

		return new IntroButton.ProgressToNextActivity(i, null) {
			@Override
			public boolean shouldLaunchActivity() {
				return true;
			}
		};
	}
}
