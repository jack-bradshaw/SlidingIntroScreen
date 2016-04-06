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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.matthewtamlin.android_utilities_library.helpers.BitmapHelper;
import com.matthewtamlin.android_utilities_library.helpers.ScreenSizeHelper;
import com.matthewtamlin.sliding_intro_screen_library.IntroActivity;
import com.matthewtamlin.sliding_intro_screen_library.IntroButton;
import com.matthewtamlin.sliding_intro_screen_library.Page;
import com.matthewtamlin.sliding_intro_screen_library.ParallaxPage;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Test the activity when multiple pages are shown. The activity should show: <li>Window: Status bar
 * shown.</li> <li>Pages: 1.</li> <li>Background: Blue.</li> <li>Transformation: Not
 * applicable.</li> <li>Page indicator: Shown with 1 dot. Animations not applicable.</li> <li>Left
 * button: Not displayed.</li> <li>Right button: Not displayed.</li> <li>Final button: Displayed on
 * last page. Displays "FINAL" with no icon.</li>
 */
public class TestSinglePageBehaviour extends IntroActivity {
	/**
	 * Used to identify this class during debugging.
	 */
	private static final String TAG = "[TestSinglePageBeha...]";

	/**
	 * The desired background color of the page.
	 */
	private static final int color = 0xff3366cc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setLeftButtonOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [left button]");
			}
		});

		setRightButtonOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [right button]");
			}
		});

		setFinalButtonOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [final button]");
			}
		});
	}

	@Override
	protected Collection<Page> generatePages(Bundle savedInstanceState) {
		ArrayList<Page> pages = new ArrayList<>();

		int screenWidth = ScreenSizeHelper.getScreenWidth(getWindowManager());
		int screenHeight = ScreenSizeHelper.getScreenHeight(getWindowManager());

		final Bitmap frontDots = BitmapHelper
				.decodeSampledBitmapFromResource(getResources(), R.raw.front, screenWidth,
						screenHeight);
		final Bitmap backDots = BitmapHelper
				.decodeSampledBitmapFromResource(getResources(), R.raw.back, screenWidth,
						screenHeight);

		final ParallaxPage p = new ParallaxPage();
		p.setDesiredBackgroundColor(color);
		p.setFrontImage(frontDots);
		p.setBackImage(backDots);
		pages.add(p);

		return pages;
	}

	/**
	 * Called by {@link #onCreate(Bundle)} to generate the behaviour of the final button. This
	 * behaviour can be changed later using {@link #setFinalButtonBehaviour(IntroButton.Behaviour)}.
	 * The {@link IntroButton.ProgressToNextActivity} class is designed to simplify the
	 * implementation.
	 *
	 * @return the behaviour to use for the final button, not null
	 */
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
