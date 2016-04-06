package com.matthewtamlin.exampleapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.matthewtamlin.android_utilities_library.helpers.BitmapHelper;
import com.matthewtamlin.android_utilities_library.helpers.ScreenSizeHelper;
import com.matthewtamlin.sliding_intro_screen_library.IntroActivity;
import com.matthewtamlin.sliding_intro_screen_library.IntroButton;
import com.matthewtamlin.sliding_intro_screen_library.Page;
import com.matthewtamlin.sliding_intro_screen_library.ParallaxPage;
import com.matthewtamlin.sliding_intro_screen_library.ParallaxTransformer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An example of how to use this library. This activity displays three pages with parallax dots in
 * the middle.
 */
public class ExampleActivity extends IntroActivity {
	/**
	 * Colors to use for the blended background.
	 */
	private static final int[] COLORS = {0xff304FFE, 0xffcc0066, 0xff9900ff};

	/**
	 * Name of the shared preferences which hold a key for preventing the intro screen from
	 * displaying again once completed.
	 */
	public static final String DONT_DISPLAY_AGAIN_NAME = "display_only_once_spfile";

	/**
	 * Key to use in {@code DONT_DISPLAY_AGAIN_NAME} to prevent the intro screen from displaying
	 * again once completed.
	 */
	public static final String DONT_DISPLAY_AGAIN_KEY = "display_only_once_spkey";

	/**
	 * The activity to launch after the intro screen.
	 */
	private Intent nextActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.NoActionBar);

		nextActivity = new Intent(this, SecondActivity.class);

		super.onCreate(savedInstanceState);

		setPageTransformer(false, new ParallaxTransformer());
		hideStatusBar();

		if (useHasCompletedIntroBefore()) {
			startActivity(nextActivity);
		}
	}

	/**
	 * Called by {@link #onCreate(Bundle)} to generate the pages to display in this activity. The
	 * returned collection is copied, so further changes to the collection will have no effect after
	 * this method returns. The natural ordering of the returned collection is used for the order of
	 * the pages.
	 *
	 * @param savedInstanceState
	 * 		if this activity is being re-initialized after previously being shut down, then this Bundle
	 * 		contains the data this activity most recently saved in {@link
	 * 		#onSaveInstanceState(Bundle)}, otherwise null
	 * @return the collection of Page elements to display, not null
	 */
	@Override
	protected Collection<Page> generatePages(Bundle savedInstanceState) {
		// This variable holds the pages while we create them
		final ArrayList<Page> pages = new ArrayList<>();

		// Load some bitmaps into memory efficiently
		final int screenWidth = ScreenSizeHelper.getScreenWidth(getWindowManager());
		final int screenHeight = ScreenSizeHelper.getScreenHeight(getWindowManager());
		final Bitmap frontDots = BitmapHelper
				.decodeSampledBitmapFromResource(getResources(), R.raw.front, screenWidth,
						screenHeight);
		final Bitmap backDots = BitmapHelper
				.decodeSampledBitmapFromResource(getResources(), R.raw.back, screenWidth,
						screenHeight);

		// Create the pages
		for (int color : COLORS) {
			final ParallaxPage newPage = ParallaxPage.newInstance();
			newPage.setDesiredBackgroundColor(color);
			newPage.setFrontImage(frontDots);
			newPage.setBackImage(backDots);
			pages.add(newPage);
		}

		// Return the pages so that they can be displayed in the activity
		return pages;
	}

	/**
	 * Called by {@link #onCreate(Bundle)} to generate the behaviour of the final button. This
	 * behaviour can be changed later using {@link #setFinalButtonBehaviour(Behaviour)}. The {@link
	 * IntroButton.ProgressToNextActivity} class is designed to simplify the implementation.
	 *
	 * @return the behaviour to use for the final button, not null
	 */
	@Override
	protected IntroButton.Behaviour generateFinalButtonBehaviour() {
		/* We create a pending shared preference edit. This edit will be applied when the intro
		 * screen is successfully completed. This can be used to prevent the screen being displayed
		 * twice
		 */
		final SharedPreferences sp = getSharedPreferences(DONT_DISPLAY_AGAIN_NAME, MODE_PRIVATE);
		final SharedPreferences.Editor pendingEdits =
				sp.edit().putBoolean(DONT_DISPLAY_AGAIN_KEY, true);

		// Pass the pending edits to a Behaviour class
		return new IntroButton.ProgressToNextActivity(nextActivity, pendingEdits) {
			@Override
			public boolean shouldLaunchActivity() {
				return true;
			}
		};
	}

	/**
	 * @return true if the user has completed the intro once before, false otherwise
	 */
	private boolean useHasCompletedIntroBefore() {
		SharedPreferences sp = getSharedPreferences(DONT_DISPLAY_AGAIN_NAME, MODE_PRIVATE);
		return sp.getBoolean(DONT_DISPLAY_AGAIN_KEY, false);
	}
}
