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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.matthewtamlin.android_utilities_library.collections.ArrayListWithCallbacks;
import com.matthewtamlin.android_utilities_library.helpers.ColorHelper;
import com.matthewtamlin.android_utilities_library.helpers.StatusBarHelper;
import com.matthewtamlin.sliding_intro_screen_library.R;
import com.matthewtamlin.sliding_intro_screen_library.background.BackgroundManager;
import com.matthewtamlin.sliding_intro_screen_library.buttons.AnimatorFactory;
import com.matthewtamlin.sliding_intro_screen_library.buttons.FadeAnimatorFactory;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Appearance;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Behaviour;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButtonModifier;
import com.matthewtamlin.sliding_intro_screen_library.core.LockableViewPager.LockMode;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;
import com.matthewtamlin.sliding_intro_screen_library.indicators.SelectionIndicator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Displays an introduction screen to the user, consisting of a series of pages and a navigation
 * bar. The pages display the content of the introduction screen, and the navigation bar displays
 * the user's progress through the activity. By default, a simple {@link DotIndicator} is shown in
 * the navigation bar, however the methods of the activity allow custom SelectionIndicators to be
 * set. The navigation bar also provides buttons for moving between pages and advancing to the next
 * activity.  It is recommended that the manifest entry declare {@code android:noHistory="true"} to
 * prevent the user from navigating back to this activity once finished. <p> To use this class,
 * subclass it and implement {@link #generatePages(Bundle)} and {@link
 * #generateFinalButtonBehaviour()}. {@link #generatePages(Bundle)} is called by {@link
 * #onCreate(Bundle)} to generate the pages (i.e. Fragments) to display in the activity. The method
 * must return a collection, it cannot return null. Pages cannot be added or removed from the
 * activity after this method returns. {@link #generateFinalButtonBehaviour()} is called by {@link
 * #onCreate(Bundle)} to generate the behaviour to assign to the final button. The method must
 * return a Behaviour, it cannot return null. The behaviour of the button defines what happens when
 * the button is pressed. The {@link IntroButton.ProgressToNextActivity} abstract class is designed
 * to facilitate validation conditions to check that the activity should finish, and it provides a
 * mechanism for setting a shared preferences flag to prevent the user from being shown the intro
 * screen again. <p> The navigation bar contains three buttons: a left button, a right button and a
 * final button. The left button is present on all pages, but by default it is not displayed on the
 * last page. The button can be shown on the last page by calling {@link
 * #disableLeftButtonOnLastPage (boolean)}. The right button is present on all pages but the last,
 * and this cannot be changed as the final button replaces the right button on the last page. By
 * default, the left button skips ahead to the last page and the right button moves to the next
 * page. The behaviour of the final button is generated in {@link #generateFinalButtonBehaviour()}.
 * The behaviour of each button can be changed using the respective 'set behaviour' method. The
 * appearance of each button can also be changed using the respective 'set appearance' method. These
 * methods make it easy to display text, an icon, or both in each button. The other methods of this
 * activity allow finer control over the appearance of each button. <p> The background of an
 * IntroActivity can be changed in two ways: manually changing the root View (using {@link
 * #getRootView()}, or supplying a BackgroundManager to {@link #setBackgroundManager(BackgroundManager)}.
 * For static backgrounds, the former method is simpler and less error prone. To make dynamic
 * backgrounds which change as the user scrolls, a BackgroundManager is needed. <p> The methods of
 * this activity also provide the following customisation options: <li>Hiding/showing the status
 * bar.</li> <li>Programmatically changing the page.</li> <li>Locking the page to prevent touch
 * events and/or commands (e.g. button presses) from changing the page.</li> <li>Modifying/replacing
 * the progress indicator.</li>
 */
public abstract class IntroActivity extends AppCompatActivity {
	// Constants

	/**
	 * Used to identify this class during debugging.
	 */
	private static final String TAG = "[IntroActivity]";

	/**
	 * Constant used to save and restore the current page on configuration changes.
	 */
	private static final String STATE_KEY_CURRENT_PAGE_INDEX = "current page index";

	/**
	 * The default current page index to be used when there is no state to restore.
	 */
	private final static int DEFAULT_CURRENT_PAGE_INDEX = 0;

	/**
	 * The Appearance to use for the left button until it is explicitly set.
	 */
	private static final Appearance DEFAULT_LEFT_BUTTON_APPEARANCE = Appearance.TEXT_ONLY;

	/**
	 * The Appearance to use for the right button until it is explicitly set.
	 */
	private static final Appearance DEFAULT_RIGHT_BUTTON_APPEARANCE = Appearance.ICON_ONLY;

	/**
	 * The appearance to use for the final button until it is explicitly set.
	 */
	private static final Appearance DEFAULT_FINAL_BUTTON_APPEARANCE = Appearance.TEXT_ONLY;

	/**
	 * The text to display in the final button until it is explicitly set.
	 */
	private static final CharSequence DEFAULT_FINAL_BUTTON_TEXT = "DONE";

	/**
	 * The length of time to use when animating button appearance/disappearance, measured in
	 * milliseconds.
	 */
	private static final int BUTTON_ANIMATION_DURATION_MS = 150;

	/**
	 * The Behaviour to use for the left button until it is explicitly set.
	 */
	private final Behaviour DEFAULT_LEFT_BUTTON_BEHAVIOUR = new IntroButton.GoToLastPage();

	/**
	 * The Behaviour to use for the right button until it is explicitly set.
	 */
	private final Behaviour DEFAULT_RIGHT_BUTTON_BEHAVIOUR = new IntroButton.GoToNextPage();


	// Miscellaneous View handles

	/**
	 * The root view of this activity.
	 */
	private RelativeLayout rootView;

	/**
	 * Displays the elements of {@code pages} to the user.
	 */
	private LockableViewPager viewPager;


	// Progress indicator variables

	/**
	 * Holds the selection indicator.
	 */
	private FrameLayout progressIndicatorHelper;

	/**
	 * Displays the user's progress through the intro screen.
	 */
	private SelectionIndicator progressIndicator;

	/**
	 * Whether or not changes in the progress indicator should be animated.
	 */
	private boolean progressIndicatorAnimationsEnabled = true;


	// Button variables

	/**
	 * An IntroButton which is displayed at the bottom left of the navigation bar. This button is
	 * hidden on the last page by default, however this can be changed using {@link
	 * #disableLeftButtonOnLastPage(boolean)}.
	 */
	private IntroButton leftButton;

	/**
	 * An IntroButton which is displayed at the bottom right of the navigation bar. This button is
	 * not displayed on the last page.
	 */
	private IntroButton rightButton;

	/**
	 * An IntroButton which is displayed at the bottom right of the navigation bar. This button is
	 * only displayed on the last page.
	 */
	private IntroButton finalButton;

	/**
	 * Whether or not the left button should be disabled.
	 */
	private boolean leftButtonDisabled = false;

	/**
	 * Whether or not the right button should be disabled.
	 */
	private boolean rightButtonDisabled = false;

	/**
	 * Whether or not the final button should be disabled.
	 */
	private boolean finalButtonDisabled = false;

	/**
	 * Whether or not {@code leftButton} should be disabled on the last page.
	 */
	private boolean disableLeftButtonOnLastPage = true;

	/**
	 * Supplies the Animators used to make the buttons appear and disappear when being enabled and
	 * disabled.
	 */
	private AnimatorFactory buttonAnimatorFactory;

	/**
	 * Maps each button to the animation which is currently acting on it. This allows animations to
	 * be cancelled if another is requested. If a button is not currently being animated, then that
	 * button does not exist in the keyset.
	 */
	private final HashMap<IntroButton, Animator> buttonAnimations = new HashMap<>();


	// Dataset related variables

	/**
	 * The pages to display in {@code viewPager}.
	 */
	private final ArrayListWithCallbacks<Fragment> pages = new ArrayListWithCallbacks<>();

	/**
	 * Adapts the elements of {@code pages} to {@code viewPager}.
	 */
	private final IntroAdapter adapter = new IntroAdapter(getSupportFragmentManager(), pages);


	// Background manager related variables

	/**
	 * Responsible for updating the background as the pages scroll.
	 */
	private BackgroundManager backgroundManager = null;


	// Listener delegates

	/**
	 * Page change events from {@code viewPager} are delegated to this receiver. Using a delegate as
	 * the receiver is better than using the public class, since this approach groups common code
	 * and hides the internal implementation from the public class signature.
	 */
	private final OnPageChangeListener pageChangeListenerDelegate = new OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			if (backgroundManager != null) {
				backgroundManager.updateBackground(rootView, position, positionOffset);
			}
		}

		@Override
		public void onPageSelected(int position) {
			// Page changes are often user initiated events, so show animations
			reflectMemberVariablesInAllButtons();

			if (progressIndicator != null) {
				progressIndicator.setSelectedItem(position, progressIndicatorAnimationsEnabled);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// Forced to implement this method, just do nothing
		}
	};


	// Overridden methods

	/**
	 * Initialises the UI and behaviour of this activity. This method calls {@link
	 * #generatePages(Bundle)} to create the pages to display in this activity.
	 *
	 * @param savedInstanceState
	 * 		if this activity is being re-initialized after previously being shut down, then this Bundle
	 * 		contains the data this activity most recently saved in {@link
	 * 		#onSaveInstanceState(Bundle)}, otherwise null
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_intro);
		bindViews();

		buttonAnimatorFactory = defineButtonAnimations();

		viewPager.addOnPageChangeListener(pageChangeListenerDelegate);
		pages.addAll(generatePages(savedInstanceState)); // Avoids external changes to pages

		initialiseNavigationButtons();
		initialiseViewPager(savedInstanceState);

		progressIndicator = new DotIndicator(this);
		regenerateProgressIndicator();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		reflectMemberVariablesInAllButtons();

		// When the activity is displayed (and the final page isn't shown) the final button
		// disappear animation needs to occur so that the appear animation occurs properly later
		if (hasFocus && getIndexOfCurrentPage() + 1 != pages.size()) {
			Animator finalAnimator =
					buttonAnimatorFactory.newFinalButtonDisappearAnimator(finalButton);
			finalAnimator.start();
		}
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_KEY_CURRENT_PAGE_INDEX, viewPager.getCurrentItem());
	}

	/**
	 * Called when the back button is pressed. If this activity is currently displaying the first
	 * page or the left lock mode prevents commands, then the default back behaviour occurs.
	 * Otherwise, the previous page is displayed.
	 */
	@Override
	public void onBackPressed() {
		if (viewPager.getCurrentItem() == 0 || !getPagingLockMode().allowsCommands()) {
			super.onBackPressed();
		} else {
			viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
		}
	}


	// Private methods

	/**
	 * Binds the View elements used in this activity to member variables.
	 */
	private void bindViews() {
		rootView = (RelativeLayout) findViewById(R.id.intro_activity_root);
		viewPager = (LockableViewPager) findViewById(R.id.intro_activity_viewPager);
		progressIndicatorHelper =
				(FrameLayout) findViewById(R.id.intro_activity_progressIndicatorHolder);
		leftButton = (IntroButton) findViewById(R.id.intro_activity_leftButton);
		rightButton = (IntroButton) findViewById(R.id.intro_activity_rightButton);
		finalButton = (IntroButton) findViewById(R.id.intro_activity_finalButton);
	}

	/**
	 * Initialises the UI elements for displaying the current page. If this activity is being
	 * restored, then the page which was previously open will be opened.
	 *
	 * @param savedInstanceState
	 * 		if this activity is being re-initialized after previously being shut down, then this Bundle
	 * 		contains the data this activity most recently saved in {@link
	 * 		#onSaveInstanceState(Bundle)}, otherwise null
	 */
	private void initialiseViewPager(final Bundle savedInstanceState) {
		final int index = (savedInstanceState == null) ? DEFAULT_CURRENT_PAGE_INDEX :
				savedInstanceState
						.getInt(STATE_KEY_CURRENT_PAGE_INDEX, DEFAULT_CURRENT_PAGE_INDEX);

		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(index);

		// Make sure the background for the first page is displayed
		if (backgroundManager != null) {
			backgroundManager.updateBackground(rootView, 0, 1);
		}
	}

	/**
	 * Sets the behaviour and appearance of the buttons, and sets {@code viewPager} as their
	 * target.
	 */
	private void initialiseNavigationButtons() {
		leftButton.setBehaviour(DEFAULT_LEFT_BUTTON_BEHAVIOUR);
		leftButton.setAppearance(DEFAULT_LEFT_BUTTON_APPEARANCE);
		leftButton.setActivity(this);

		rightButton.setBehaviour(DEFAULT_RIGHT_BUTTON_BEHAVIOUR);
		rightButton.setAppearance(DEFAULT_RIGHT_BUTTON_APPEARANCE);
		rightButton.setActivity(this);

		finalButton.setBehaviour(generateFinalButtonBehaviour());
		finalButton.setAppearance(DEFAULT_FINAL_BUTTON_APPEARANCE);
		finalButton.setText(DEFAULT_FINAL_BUTTON_TEXT, null);
		finalButton.setActivity(this);
	}

	/**
	 * Enables or disables each button separately, so that the buttons match the current member
	 * variables.
	 */
	private void reflectMemberVariablesInAllButtons() {
		reflectMemberVariablesInLeftButton();
		reflectMemberVariablesInRightButton();
		reflectMemberVariablesInFinalButton();
	}

	/**
	 * Enables or disables the left button, so that it matches the current member variables.
	 */
	private void reflectMemberVariablesInLeftButton() {
		// Determine whether or not changes need to occur
		final boolean lastPageReached = (viewPager.getCurrentItem() + 1) == pages.size();
		final boolean buttonShouldBeInvisible = (lastPageReached && disableLeftButtonOnLastPage) ||
				leftButtonDisabled;
		final boolean buttonIsCurrentlyInvisible = leftButton.getVisibility() == View.INVISIBLE;
		final boolean shouldUpdateButton = buttonShouldBeInvisible != buttonIsCurrentlyInvisible;

		// Apply changes if necessary
		if (shouldUpdateButton) {
			final Animator buttonAnimator = buttonShouldBeInvisible ?
					buttonAnimatorFactory.newLeftButtonDisappearAnimator(leftButton) :
					buttonAnimatorFactory.newLeftButtonAppearAnimator(leftButton);

			if (buttonShouldBeInvisible) {
				disableButton(buttonAnimator, leftButton);
			} else {
				enableButton(buttonAnimator, leftButton);
			}
		}
	}

	/**
	 * Enables or disables the right button, so that it matches the current member variables.
	 */
	private void reflectMemberVariablesInRightButton() {
		// Determine whether or not changes need to occur
		final boolean lastPageReached = (viewPager.getCurrentItem() + 1) == pages.size();
		final boolean buttonShouldBeInvisible = lastPageReached || rightButtonDisabled;
		final boolean buttonIsCurrentlyInvisible = rightButton.getVisibility() == View.INVISIBLE;
		final boolean shouldUpdateButton = buttonShouldBeInvisible != buttonIsCurrentlyInvisible;

		// Apply changes if necessary
		if (shouldUpdateButton) {
			final Animator buttonAnimator = buttonShouldBeInvisible ?
					buttonAnimatorFactory.newRightButtonDisappearAnimator(rightButton) :
					buttonAnimatorFactory.newRightButtonAppearAnimator(rightButton);

			if (buttonShouldBeInvisible) {
				disableButton(buttonAnimator, rightButton);
			} else {
				enableButton(buttonAnimator, rightButton);
			}
		}
	}

	/**
	 * Enables or disables the final button, so that it matches the current member variables.
	 */
	private void reflectMemberVariablesInFinalButton() {
		// Determine whether or not changes need to occur
		final boolean lastPageReached = (viewPager.getCurrentItem() + 1) == pages.size();
		final boolean buttonShouldBeInvisible = !lastPageReached || finalButtonDisabled;
		final boolean buttonIsCurrentlyInvisible = finalButton.getVisibility() == View.INVISIBLE;
		final boolean shouldUpdateButton = buttonShouldBeInvisible != buttonIsCurrentlyInvisible;

		// Apply changes if necessary
		if (shouldUpdateButton) {
			final Animator buttonAnimator = buttonShouldBeInvisible ?
					buttonAnimatorFactory.newFinalButtonDisappearAnimator(finalButton) :
					buttonAnimatorFactory.newFinalButtonAppearAnimator(finalButton);

			if (buttonShouldBeInvisible) {
				disableButton(buttonAnimator, finalButton);
			} else {
				enableButton(buttonAnimator, finalButton);
			}
		}
	}

	/**
	 * Disables the supplied button by making it invisible and un-clickable. This method should only
	 * be called while the supplied button is enabled (i.e. visible and clickable). The supplied
	 * Animator will be used to transition the button.
	 *
	 * @param buttonAnimator
	 * 		the Animator to use when transitioning the button, null to perform no animation
	 * @param button
	 * 		the button to disable, not null
	 * @throws IllegalArgumentException
	 * 		if {@code button} is null
	 */
	private void disableButton(final Animator buttonAnimator, final IntroButton button) {
		if (button == null) {
			throw new IllegalArgumentException("button cannot be null");
		}

		// Any animations currently affecting the button must be cancelled before new ones start
		if (buttonAnimations.containsKey(button)) {
			buttonAnimations.get(button).cancel();
			buttonAnimations.remove(button);
		}

		if (buttonAnimator != null) {
			// Register animation so that it may be cancelled later if necessary
			buttonAnimations.put(button, buttonAnimator);

			// End and cancel conditions ensure that the UI is not left in a transient state when
			// animations finish for whatever reason
			buttonAnimator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationStart(Animator animation) {
					button.setVisibility(View.VISIBLE); // Make sure view is visible while animating
					button.setEnabled(false); // Click events should be ignored immediately
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					// If the animation doesn't properly hide the button, make sure it's invisible
					button.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onAnimationCancel(Animator animation) {
					// Restore button to enabled mode
					button.setVisibility(View.VISIBLE);
					button.setEnabled(true);
				}
			});

			buttonAnimator.setDuration(BUTTON_ANIMATION_DURATION_MS);
			buttonAnimator.start();
		} else {
			// If no animator was supplied, just apply the end conditions
			button.setVisibility(View.INVISIBLE);
			button.setEnabled(false);
		}
	}

	/**
	 * Enables the supplied button by making it visible and clickable. This method should only be
	 * called while the supplied button is disabled (i.e. invisible and un-clickable). The supplied
	 * Animator will be used to transition the button.
	 *
	 * @param buttonAnimator
	 * 		the Animator to use when transitioning the button, null to perform no animation
	 * @param button
	 * 		the button to enable, not null
	 * @throws IllegalArgumentException
	 * 		if {@code button} is null
	 */
	private void enableButton(final Animator buttonAnimator, final IntroButton button) {
		if (button == null) {
			throw new IllegalArgumentException("button cannot be null");
		}

		// Any animations currently affecting the button must be cancelled before new ones start
		if (buttonAnimations.containsKey(button)) {
			buttonAnimations.get(button).cancel();
			buttonAnimations.remove(button);
		}

		if (buttonAnimator != null) {
			buttonAnimations.put(button, buttonAnimator);

			// Give the disable animation (if any) time to finish before the enable animation starts
			buttonAnimator.setStartDelay(BUTTON_ANIMATION_DURATION_MS);

			// End and cancel conditions ensure that the UI is not left in a transient state when
			// animations finish for whatever reason
			buttonAnimator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationStart(Animator animation) {
					button.setVisibility(View.VISIBLE); // Make sure view is visible while animating
					button.setEnabled(true); // Click events should be listened for immediately
				}

				@Override
				public void onAnimationCancel(Animator animation) {
					// Restore button to disabled mode
					button.setVisibility(View.INVISIBLE);
					button.setEnabled(false);
				}
			});

			buttonAnimator.setDuration(BUTTON_ANIMATION_DURATION_MS);
			buttonAnimator.start();
		} else {
			// If no Animator was supplied, just apply the end conditions
			button.setVisibility(View.VISIBLE);
			button.setEnabled(true);
		}
	}

	/**
	 * Updates the progress indicator to reflect the current member variables.
	 */
	private void regenerateProgressIndicator() {
		progressIndicatorHelper.removeAllViews();

		if (progressIndicator != null) {
			progressIndicatorHelper.addView((View) progressIndicator);
			progressIndicator.setNumberOfItems(pages.size());
			if (pages.size() > 0) {
				progressIndicator.setSelectedItem(getIndexOfCurrentPage(), false);
			}
		}
	}


	// Abstract methods

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
	 * @return the collection of pages to display, not null
	 */
	protected abstract Collection<Fragment> generatePages(Bundle savedInstanceState);

	/**
	 * Called by {@link #onCreate(Bundle)} to generate the behaviour of the final button. This
	 * behaviour can be changed later using {@link #setFinalButtonBehaviour(Behaviour)}. The {@link
	 * IntroButton.ProgressToNextActivity} class is designed to simplify the implementation.
	 *
	 * @return the behaviour to use for the final button, not null
	 */
	protected abstract Behaviour generateFinalButtonBehaviour();


	/**
	 * Hides the status bar background but continues to display the icons. Views and ViewGroups
	 * which declare the {@code android:fitsSystemWindows="false"} attribute will draw to the top of
	 * the screen. The effect of this method varies depending on the current SDK version.
	 */
	public final void hideStatusBar() {
		StatusBarHelper.hideStatusBar(getWindow());
	}

	/**
	 * Shows the status bar background and prevents views from being drawn behind the status bar
	 * (including existing views). The primary dark color of the current theme will be used for the
	 * status bar color (on SDK version 21 and up). If the current theme does not specify a primary
	 * dark color, the status bar will be colored black.
	 */
	public final void showStatusBar() {
		final int statusBarColor = ColorHelper.getPrimaryDarkColor(this, Color.BLACK);
		StatusBarHelper.showStatusBar(getWindow(), statusBarColor);
	}


	// Methods relating to the root view

	/**
	 * @return a reference to the top most view of this activity
	 */
	public final RelativeLayout getRootView() {
		return rootView;
	}


	// Methods relating to the page transformer

	/**
	 * Sets the PageTransformer which will be used to the pages of this intro screen when scrolled.
	 *
	 * @param reverseDrawingOrder
	 * 		true if the supplied PageTransformer requires page views to be drawn from last to first
	 * 		instead of first to last
	 * @param transformer
	 * 		the PageTransformer that will modify each page's animation properties
	 */
	public final void setPageTransformer(final boolean reverseDrawingOrder, final ViewPager
			.PageTransformer transformer) {
		viewPager.setPageTransformer(reverseDrawingOrder, transformer);
	}


	// Methods relating to accessing pages and navigating between them

	/**
	 * Returns a reference to the pages of this activity, as an unmodifiable collection.
	 *
	 * @return the pages of this activity
	 */
	public final Collection<Fragment> getPages() {
		return Collections.unmodifiableCollection(pages);
	}

	/**
	 * Returns the page at the specified index.
	 *
	 * @param pageIndex
	 * 		the index of the page to return, counting from zero
	 * @return the page at {@code index}
	 * @throws IndexOutOfBoundsException
	 * 		if the index exceeds the size of the page dataset
	 */
	public final Fragment getPage(int pageIndex) {
		return pages.get(pageIndex);
	}

	/**
	 * @return the page currently being displayed
	 */
	public final Fragment getCurrentPage() {
		return pages.get(viewPager.getCurrentItem());
	}

	/**
	 * @return the first page of this activity
	 */
	public final Fragment getFirstPage() {
		return pages.get(0);
	}

	/**
	 * @return the last page of this activity
	 */
	public final Fragment getLastPage() {
		return pages.get(pages.size() - 1);
	}

	/**
	 * Returns the index of the specified page, or -1 if the page is not in this activity.
	 *
	 * @param page
	 * 		the page to get the index of
	 * @return the index of {@code page}, counting from zero
	 */
	public final int getIndexOfPage(Fragment page) {
		return pages.indexOf(page);
	}

	/**
	 * @return the index of the page currently being displayed, counting from zero
	 */
	public final int getIndexOfCurrentPage() {
		return viewPager.getCurrentItem();
	}

	/**
	 * Changes which page is currently displayed.
	 *
	 * @param pageIndex
	 * 		the index of the page to display, counting from zero
	 * @throws IndexOutOfBoundsException
	 * 		if the index exceeds the size of the page dataset
	 */
	public final void goToPage(final int pageIndex) {
		viewPager.setCurrentItem(pageIndex);
	}

	/**
	 * Displays the last page, if not already displayed.
	 */
	public final void goToLastPage() {
		viewPager.setCurrentItem(pages.size() - 1);
	}

	/**
	 * Displays the first page, if not already displayed.
	 */
	public final void goToFirstPage() {
		viewPager.setCurrentItem(0);
	}

	/**
	 * Displays the next page, if not already displayed.
	 */
	public final void goToNextPage() {
		boolean isLastPage = viewPager.getCurrentItem() == (pages.size() - 1);

		if (!isLastPage) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
		}
	}

	/**
	 * Displays the previous page, if not already displayed.
	 */
	public final void goToPreviousPage() {
		boolean isFirstPage = viewPager.getCurrentItem() == 0;

		if (!isFirstPage) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
		}
	}

	/**
	 * @return the current number of pages in this activity
	 */
	public final int numberOfPages() {
		return pages.size();
	}

	/**
	 * Sets the paging lock mode. This can be used to prevent the user from navigating to another
	 * page by swiping, pressing buttons, or both.
	 *
	 * @param lockMode
	 * 		the lock mode to use, not null
	 */
	public final void setPagingLockMode(final LockMode lockMode) {
		viewPager.setLockMode(lockMode);
	}

	/**
	 * @return the current lock mode for paging right
	 */
	public final LockMode getPagingLockMode() {
		return viewPager.getLockMode();
	}

	/**
	 * Sets the background manager to use when scrolling through pages. The {@link
	 * BackgroundManager#updateBackground(View, int, float)} method of the supplied manager will be
	 * invoked whenever the user scrolls. Note that the BackgroundManager draws behind the pages,
	 * therefore all pages should have a transparent background when using a BackgroundManager.
	 *
	 * @param backgroundManager
	 * 		the backgroundManager to use, null to use none
	 */
	public final void setBackgroundManager(BackgroundManager backgroundManager) {
		this.backgroundManager = backgroundManager;
	}

	/**
	 * @return the current BackgroundManager, may be null
	 */
	public final BackgroundManager getBackgroundManager() {
		return backgroundManager;
	}


	// Methods relating to the progress indicator

	/**
	 * Sets the selection indicator to show the user's progress through the activity. The provides
	 * selection indicator must be a subclass of {@link View}.
	 *
	 * @param selectionIndicator
	 * 		the selection indicator to use, null to clear any existing indicator
	 */
	public void setProgressIndicator(final SelectionIndicator selectionIndicator) {
		if (!(selectionIndicator instanceof View)) {
			throw new IllegalArgumentException(
					"selectionIndicator must be a subclass of android.view.View");
		}

		progressIndicator = selectionIndicator;
		regenerateProgressIndicator();
	}

	/**
	 * @return the selection indicator showing the user's progress through the activity, null if
	 * there is none
	 */
	public SelectionIndicator getProgressIndicator() {
		return progressIndicator;
	}

	/**
	 * Enables or disables progress indicator animations
	 *
	 * @param enableAnimations
	 * 		true to enable animations, false to disable them
	 */
	public void enableProgressIndicatorAnimations(final boolean enableAnimations) {
		progressIndicatorAnimationsEnabled = enableAnimations;
	}

	/**
	 * @return true if progress indicator animations are enabled, false otherwise
	 */
	public boolean progressIndicatorAnimationsAreEnabled() {
		return progressIndicatorAnimationsEnabled;
	}


	// Methods relating to the buttons

	/**
	 * Called when the activity is created to define the AnimatorFactory to use whenever a button is
	 * enabled or disabled. The default factory causes the buttons to fade in or fade out.
	 *
	 * @return an AnimatorFactory which supplies the animations to use when a button is enabled or
	 * disabled, not null
	 */
	public AnimatorFactory defineButtonAnimations() {
		return new FadeAnimatorFactory();
	}

	/**
	 * @return an IntroButtonModifier which can be used to modify the left button, not null
	 */
	public IntroButtonModifier modifyLeftButton() {
		return new IntroButtonModifier(leftButton);
	}

	/**
	 * @return an IntroButtonModifier which can be used to modify the right button, not null
	 */
	public IntroButtonModifier modifyRightButton() {
		return new IntroButtonModifier(rightButton);
	}

	/**
	 * @return an IntroButtonModifier which can be used to modify the final button, not null
	 */
	public IntroButtonModifier modifyFinalButton() {
		return new IntroButtonModifier(finalButton);
	}

	/**
	 * Disables the left button by making it invisible and un-clickable.
	 *
	 * @param disabled
	 * 		true to disable the button, false to enable it (i.e. make it visible and clickable)
	 */
	public final void disableLeftButton(final boolean disabled) {
		leftButtonDisabled = disabled;
		reflectMemberVariablesInLeftButton();
	}

	/**
	 * Returns whether or not the left button is currently disabled (i.e. invisible and
	 * un-clickable).
	 *
	 * @return true if the button is currently disabled, false otherwise
	 */
	public final boolean leftButtonIsDisabled() {
		return leftButtonDisabled;
	}

	/**
	 * Sets whether or not the left button should be automatically disabled on the last page and
	 * re-enabled when returning to a previous page. This feature is overridden if true was last
	 * passed to {@link #disableLeftButton(boolean)}.
	 *
	 * @param disableButton
	 * 		true to automatically disable the left button on the last page, false to prevent automatic
	 * 		disabling
	 */
	public final void disableLeftButtonOnLastPage(final boolean disableButton) {
		disableLeftButtonOnLastPage = disableButton;
		reflectMemberVariablesInLeftButton();
	}

	/**
	 * Returns whether or not the left button will be disabled when the last page is displayed. This
	 * method does not take into account whether or not the button is has been globally disabled
	 * using {@link #disableLeftButton(boolean)}.
	 *
	 * @return true if the left button will be disabled while the last page is displayed, false
	 * otherwise
	 */
	public final boolean leftButtonIsDisabledOnLastPage() {
		return disableLeftButtonOnLastPage;
	}

	/**
	 * Disables the right button by making it invisible and un-clickable.
	 *
	 * @param disabled
	 * 		true to disable the button, false to enable it (i.e. make it visible and clickable)
	 */
	public final void disableRightButton(final boolean disabled) {
		rightButtonDisabled = disabled;
		reflectMemberVariablesInRightButton();
	}

	/**
	 * Returns whether or not the right button is currently disabled (i.e. invisible and
	 * un-clickable).
	 *
	 * @return true if the button is currently disabled, false otherwise
	 */
	public final boolean rightButtonIsDisabled() {
		return rightButtonDisabled;
	}

	/**
	 * Disables the final button by making it invisible and un-clickable.
	 *
	 * @param disabled
	 * 		true to disable the button, false to enable it (i.e. make it visible and clickable)
	 */
	public final void disableFinalButton(final boolean disabled) {
		finalButtonDisabled = disabled;
		reflectMemberVariablesInFinalButton();
	}

	/**
	 * Returns whether or not the final button is currently disabled (i.e. invisible and
	 * un-clickable).
	 *
	 * @return true if the button is currently disabled, false otherwise
	 */
	public final boolean finalButtonIsDisabled() {
		return finalButtonDisabled;
	}
}