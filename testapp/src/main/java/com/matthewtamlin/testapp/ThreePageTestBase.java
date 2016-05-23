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

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.matthewtamlin.android_utilities_library.helpers.BitmapHelper;
import com.matthewtamlin.android_utilities_library.helpers.ScreenSizeHelper;
import com.matthewtamlin.sliding_intro_screen_library.background.ColorBlender;
import com.matthewtamlin.sliding_intro_screen_library.core.IntroActivity;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton;
import com.matthewtamlin.sliding_intro_screen_library.pages.ParallaxPage;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class for use during testing as a base class. Three pages are displayed, and button presses are
 * logged.
 */
public abstract class ThreePageTestBase extends IntroActivity {
	/**
	 * Used during testing to identify this class.
	 */
	private static final String TAG = "[ThreePageTestBase]";

	/**
	 * Colors to use for the desired backgrounds.
	 */
	private static final int[] colors = {0xff3366cc, 0xffcc0066, 0xff9900ff};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setLeftButtonOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "[on click] [left button]");
			}
		});

		setRightButtonOnClickListener(new OnClickListener() {
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
		
		setBackgroundManager(new ColorBlender(colors));
	}

	@Override
	protected Collection<Fragment> generatePages(Bundle savedInstanceState) {
		ArrayList<Fragment> pages = new ArrayList<>();

		final int screenWidth = ScreenSizeHelper.getScreenWidth(getWindowManager());
		final int screenHeight = ScreenSizeHelper.getScreenHeight(getWindowManager());

		final Bitmap frontDots = BitmapHelper
				.decodeSampledBitmapFromResource(getResources(), R.raw.lines, screenWidth,
						screenHeight);
		final Bitmap backDots = BitmapHelper
				.decodeSampledBitmapFromResource(getResources(), R.raw.lines, screenWidth,
						screenHeight);

		for (int color : colors) {
			final ParallaxPage newPage = ParallaxPage.newInstance();
			newPage.setFrontImage(frontDots);
			newPage.setBackImage(backDots);
			pages.add(newPage);
		}

		return pages;
	}

	/**
	 * @return the colors used in this activity
	 */
	public int[] getColors() {
		return colors;
	}

	@Override
	protected IntroButton.Behaviour generateFinalButtonBehaviour() {
		return new IntroButton.DoNothing();
	}
}