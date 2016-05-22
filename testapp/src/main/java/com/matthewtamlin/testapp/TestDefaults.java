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

package com.matthewtamlin.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.matthewtamlin.sliding_intro_screen_library.core.IntroActivity;
import com.matthewtamlin.sliding_intro_screen_library.core.IntroButton;
import com.matthewtamlin.sliding_intro_screen_library.pages.Page;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Test the default behaviour. Should display: <li>Window: Status bar shown.</li> <li>Pages: Has 0
 * pages.</li> <li>Background: Grey consistently.</li> <li>Transformation: Not applicable.</li>
 * <li>Page indicator: Shown with 1 dot.</li> <li>Left button: Not shown.</li> <li>Right button: Not
 * shown.</li> <li>Final button: Displays "FINAL" with no icon, and advances to the post activity
 * when clicked.</li>
 */
public class TestDefaults extends IntroActivity {
	/**
	 * Used to identify this class during debugging.
	 */
	private static final String TAG = "[TestDefaults]";

	@Override
	protected Collection<Fragment> generatePages(Bundle savedInstanceState) {
		// Return empty collection so that default behaviour may be tested
		return new ArrayList<>();
	}

	@Override
	protected IntroButton.Behaviour generateFinalButtonBehaviour() {
		final Intent i = new Intent(this, PostActivity.class);

		return new IntroButton.ProgressToNextActivity(i, null) {
			@Override
			public boolean shouldLaunchActivity() {
				return true;
			}
		};
	}
}