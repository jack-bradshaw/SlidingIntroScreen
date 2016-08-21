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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.matthewtamlin.android_utilities_library.helpers.StatusBarHelper;
import com.matthewtamlin.android_utilities_library.helpers.ThemeColorHelper;
import com.matthewtamlin.sliding_intro_screen_library.R;
import com.matthewtamlin.sliding_intro_screen_library.background.BackgroundManager;
import com.matthewtamlin.sliding_intro_screen_library.buttons.AnimatorFactory;
import com.matthewtamlin.sliding_intro_screen_library.buttons.FadeAnimatorFactory;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Appearance;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Behaviour;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButtonAccessor;
import com.matthewtamlin.sliding_intro_screen_library.core.LockableViewPager.LockMode;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;
import com.matthewtamlin.sliding_intro_screen_library.indicators.SelectionIndicator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Displays an introduction screen to the user, consisting of a series of pages and a navigation
 * bar. The pages display the content of the introduction screen, and the navigation bar displays
 * the user's progress through the activity. The navigation bar also provides three configurable
 * buttons for moving through the activity and performing other functions. It is recommended that
 * the manifest entry declare {@code android:noHistory="true"} to prevent the user from navigating
 * back to this activity once it is complete.
 * <p/>
 * To use this class, subclass it and implement {@link #generatePages(Bundle)} and {@link
 * #generateFinalButtonBehaviour()}. The former method is called by onCreate method generates the
 * pages (i.e. Fragments) displayed in the introduction. Pages cannot be added or removed after this
 * method returns, however they may still be modified. The latter method is called by onCreate to
 * generate the Behaviour to assign to the button shown on the last page (see {@link IntroButton}).
 * It is recommended that an instance of the {@link IntroButton.ProgressToNextActivity} class be
 * used.
 * <p/>
 * The navigation bar contains three buttons: a left button, a right button and a final button. By
 * default the left and right buttons are present on all pages but the last, and the final button is
 * displayed only on the last page. The left button can be displayed on the last page by calling
 * {@link #disableLeftButtonOnLastPage(boolean)}. By default, the left button skips ahead to the
 * last page and the right button moves to the next page. The behaviour of the final button is
 * generated in {@link #generateFinalButtonBehaviour()}. The behaviour and appearance of each button
 * can be changed using the methods of this Activity. Whenever buttons appear or disappear, they are
 * animated using the {@link AnimatorFactory} supplied by {@link #generateButtonAnimatorFactory}.
 * The default animations cause the buttons to fade in and out, however this behaviour can be
 * changed by overriding {@code generateButtonAnimatorFactory()} and returning a custom
 * AnimatorFactory.
 * <p/>
 * Unless the individual page Fragments define their own backgrounds, it is highly recommended that
 * the background of the IntroActivity be changed. The background of an IntroActivity can be changed
 * in two ways: by manually changing the root View (via {@link #getRootView()}), or by supplying a
 * BackgroundManager to {@link #setBackgroundManager(BackgroundManager)}. The former approach is
 * simpler and less error prone, and is ideal when a static background is all that is needed. The
 * latter approach is ideal when a dynamic background is desired. The {@link
 * com.matthewtamlin.sliding_intro_screen_library.background.ColorBlender} class is provided to
 * simplify implementation of a dynamic background.
 * <p/>
 * The methods of this activity provide the following additional customisation options:
 * <ul><li>Hiding/showing the status bar.</li> <li>Programmatically changing the page.</li>
 * <li>Locking the page.</li> <li>Modifying/replacing the progress indicator.</li> <li>Setting a
 * page transformer.</li> <li>Obtaining references to the individual pages.</li></ul>
 */
public abstract class IntroActivity extends AppCompatActivity {
	// Constants

	/**
	 * Used to identify this class during debugging.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "[IntroActivity]";

	/**
	 * Key for saving and restoring the current page on configuration changes.
	 */
	private static final String STATE_KEY_CURRENT_PAGE_INDEX = "current page index";

	/**
	 * The page index to use when there is no state to restore.
	 */
	private static final int DEFAULT_CURRENT_PAGE_INDEX = 0;

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
	 * The length of time to use for button appear/disappear animations, measured in milliseconds.
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
	 * The root view of the View hierarchy.
	 */
	private RelativeLayout rootView;

	/**
	 * Displays the pages to the user.
	 */
	private LockableViewPager viewPager;

	/**
	 * The thin horizontal divider separating the navigation elements from the rest of the UI.
	 */
	private View horizontalDivider;


	// Progress indicator variables

	/**
	 * Wrapper for the selection indicator.
	 */
	private FrameLayout progressIndicatorWrapper;

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
	 * The IntroButton displayed in the left end of the navigation bar. This button is hidden on the
	 * last page by default but can be shown using {@link #disableLeftButtonOnLastPage(boolean)}.
	 */
	private IntroButton leftButton;

	/**
	 * The IntroButton displayed in the right end of the navigation bar. This button is not shown on
	 * the last page.
	 */
	private IntroButton rightButton;

	/**
	 * The IntroButton displayed in the right end of the navigation bar when the last page is
	 * shown.
	 */
	private IntroButton finalButton;

	/**
	 * Whether or not the left button should be disabled entirely.
	 */
	private boolean leftButtonDisabled = false;

	/**
	 * Whether or not the right button should be disabled entirely.
	 */
	private boolean rightButtonDisabled = false;

	/**
	 * Whether or not the final button should be disabled entirely.
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
	 * Maps each button to the animator currently affecting it. This allows animators to be
	 * cancelled if a newer animator is created. If a button is not currently being animated, then
	 * it should not exist in the keyset.
	 */
	private final HashMap<IntroButton, Animator> buttonAnimations = new HashMap<>();


	// Dataset related variables

	/**
	 * The pages to display.
	 */
	private final ArrayList<Fragment> pages = new ArrayList<>();

	/**
	 * Adapts the pages so that they can be displayed in the UI.
	 */
	private final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), pages);


	// Background manager related variables

	/**
	 * Updates the background of the Activity as the pages scroll.
	 */
	private BackgroundManager backgroundManager = null;


	// Listener delegates

	/**
	 * Page change events from {@code viewPager} are delegated to this receiver. Using a delegate as
	 * the receiver is hides the internal implementation from the class signature.
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
			// The active page has changes, so the UI needs to be updated
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
	 * Initialises the UI and behaviour of this activity. This method's execution calls the abstract
	 * methods.
	 *
	 * @param savedInstanceState
	 * 		if this activity is being re-initialized after previously being shut down, then this Bundle
	 * 		contains the data this activity most recently saved in {@link
	 * 		#onSaveInstanceState(Bundle)}, otherwise null
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialise the UI and get references to the View elements
		setContentView(R.layout.activity_intro);
		bindViews();

		// Initialise the buttons
		initialiseNavigationButtons();
		buttonAnimatorFactory = generateButtonAnimatorFactory();

		// Generate the pages and create a copy to avoid external changes to the dataset
		pages.addAll(generatePages(savedInstanceState));

		// Initialise the view pager
		viewPager.addOnPageChangeListener(pageChangeListenerDelegate);
		initialiseViewPager(savedInstanceState);

		// Initialise the progress indicator
		progressIndicator = new DotIndicator(this);
		regenerateProgressIndicator();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		// Will apply animations to the left and right buttons as usual
		reflectMemberVariablesInAllButtons();

		// When the activity is displayed (and the final page isn't shown) the final button
		// disappear animation needs to occur so that appear animation displays properly later
		final boolean lastPage = getIndexOfCurrentPage() + 1 == pages.size();

		if (hasFocus && !lastPage) {
			final Animator finalButtonAnimator =
					buttonAnimatorFactory.newFinalButtonDisappearAnimator(finalButton);
			finalButtonAnimator.start();
		}
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_KEY_CURRENT_PAGE_INDEX, viewPager.getCurrentItem());
	}

	/**
	 * Called when the back button is pressed. If this activity is currently displaying the first
	 * page or the lock mode prevents commands, the default back behaviour occurs. Otherwise, the
	 * previous page is displayed.
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
		horizontalDivider = findViewById(R.id.intro_activity_horizontalDivider);
		viewPager = (LockableViewPager) findViewById(R.id.intro_activity_viewPager);
		progressIndicatorWrapper =
				(FrameLayout) findViewById(R.id.intro_activity_progressIndicatorHolder);
		leftButton = (IntroButton) findViewById(R.id.intro_activity_leftButton);
		rightButton = (IntroButton) findViewById(R.id.intro_activity_rightButton);
		finalButton = (IntroButton) findViewById(R.id.intro_activity_finalButton);
	}

	/**
	 * Initialises the UI elements for displaying the current page. If this activity is being
	 * restored, then the page which was previously displayed will be redisplayed.
	 *
	 * @param savedInstanceState
	 * 		if this activity is being re-initialized after previously being shut down, then this Bundle
	 * 		contains the data this activity most recently saved in {@link
	 * 		#onSaveInstanceState(Bundle)}, otherwise null
	 */
	private void initialiseViewPager(final Bundle savedInstanceState) {
		// Restore the page index from the saved instance state if possible
		final int pageIndex = (savedInstanceState == null) ?
				DEFAULT_CURRENT_PAGE_INDEX :
				savedInstanceState.getInt(STATE_KEY_CURRENT_PAGE_INDEX, DEFAULT_CURRENT_PAGE_INDEX);

		// Initialise the dataset of the view pager and display the desired page
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(pageIndex);

		// Make sure the background for the current page is displayed
		if (backgroundManager != null) {
			backgroundManager.updateBackground(rootView, pageIndex, 0);
		}
	}

	/**
	 * Sets the Behaviour and Appearance of the buttons. This Activity is set to the Behaviour
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

			// End/cancel conditions ensure that the UI is not left in a transient state when
			// animations finish
			buttonAnimator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationStart(final Animator animation) {
					button.setVisibility(View.VISIBLE); // Make sure View is visible while animating
					button.setEnabled(false); // Click events should be ignored immediately
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					// If the animation doesn't properly hide the button, make sure it's invisible
					button.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onAnimationCancel(Animator animation) {
					// Restore the button to enabled mode
					button.setVisibility(View.VISIBLE);
					button.setEnabled(true);
				}
			});

			buttonAnimator.setDuration(BUTTON_ANIMATION_DURATION_MS);
			buttonAnimator.start();
		} else {
			// If no animator was supplied, just apply the disabled conditions
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

			// Give any disable animations time to finish before the enable animation starts
			buttonAnimator.setStartDelay(BUTTON_ANIMATION_DURATION_MS);

			// End/cancel conditions ensure that the UI is not left in a transient state when
			// animations finish
			buttonAnimator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationStart(final Animator animation) {
					button.setVisibility(View.VISIBLE); // Make sure View is visible while animating
					button.setEnabled(true); // Click events should be accepted immediately
				}

				@Override
				public void onAnimationCancel(final Animator animation) {
					// Restore button to disabled mode
					button.setVisibility(View.INVISIBLE);
					button.setEnabled(false);
				}
			});

			buttonAnimator.setDuration(BUTTON_ANIMATION_DURATION_MS);
			buttonAnimator.start();
		} else {
			// If no Animator was supplied, just apply the enabled conditions
			button.setVisibility(View.VISIBLE);
			button.setEnabled(true);
		}
	}

	/**
	 * Updates the progress indicator to reflect the current member variables.
	 */
	private void regenerateProgressIndicator() {
		// Refresh the View by entirely removing the progress indicator so that it can be re-added
		progressIndicatorWrapper.removeAllViews();

		// Only re-add the indicator if one currently exists
		if (progressIndicator != null) {
			progressIndicatorWrapper.addView((View) progressIndicator);

			// Make sure the number of pages and the displayed page is correct
			progressIndicator.setNumberOfItems(pages.size());
			progressIndicator.setSelectedItem(getIndexOfCurrentPage(), false);
		}
	}


	// Abstract methods

	/**
	 * Called by {@link #onCreate(Bundle)} to generate the pages displayed in this activity. The
	 * returned Collection is copied, so further changes to the collection will have no effect after
	 * this method returns. The total ordering of the returned collection is maintained in the
	 * display of the pages.
	 *
	 * @param savedInstanceState
	 * 		if this activity is being re-initialized after previously being shut down, then this Bundle
	 * 		contains the data this activity most recently saved in {@link
	 * 		#onSaveInstanceState(Bundle)}, otherwise null
	 * @return the pages to display in the Activity, not null
	 */
	protected abstract Collection<? extends Fragment> generatePages(Bundle savedInstanceState);

	/**
	 * Called by {@link #onCreate(Bundle)} to generate the Behaviour of the final button. The {@link
	 * IntroButton} class contains Behaviours which suit most needs. The Behaviour of the final
	 * button can be changed later using {@link #getFinalButtonAccessor()}.
	 *
	 * @return the Behaviour to use for the final button, not null
	 */
	protected abstract Behaviour generateFinalButtonBehaviour();


	// Miscellaneous public methods

	/**
	 * Hides the status bar background but continues to display the system icons. Views and
	 * ViewGroups which declare the {@code android:fitsSystemWindows="false"} attribute will draw to
	 * the top of the screen. The effect of this method varies depending on the current SDK
	 * version.
	 *
	 * @deprecated use the AndroidUtilities library directly (com.matthewtamlin:android-utilities)
	 */
	@Deprecated
	public final void hideStatusBar() {
		StatusBarHelper.hideStatusBar(getWindow());
	}

	/**
	 * Shows the status bar background and prevents Views from being drawn behind the status bar.
	 * The primary dark color of the current theme will be used for the status bar color (on SDK
	 * version 21 and higher). If the current theme does not specify a primary dark color, the
	 * status bar will be colored black.
	 *
	 * @deprecated use the AndroidUtilities library directly (com.matthewtamlin:android-utilities)
	 */
	@Deprecated
	public final void showStatusBar() {
		final int statusBarColor = ThemeColorHelper.getPrimaryDarkColor(this, Color.BLACK);
		StatusBarHelper.showStatusBar(getWindow(), statusBarColor);
	}

	/**
	 * Returns the root View in the View hierarchy of this Activity. If this method is called before
	 * {@code onCreate(Bundle)}, then null is returned.
	 *
	 * @return the root View of the View hierarchy
	 */
	public final RelativeLayout getRootView() {
		return rootView;
	}

	/**
	 * Allows the visibility of the horizontal divider at the top of the navigation bar to be
	 * changed.
	 *
	 * @param show
	 * 		true to show the divider, false to hide it
	 */
	public final void changeHorizontalDividerVisibility(final boolean show) {
		horizontalDivider.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
	}

	/**
	 * Sets the PageTransformer to use when scrolling.
	 *
	 * @param reverseDrawingOrder
	 * 		true if the supplied PageTransformer requires page Views to be drawn from last to first
	 * 		instead of first to last
	 * @param transformer
	 * 		the transformer to use, null allowed
	 */
	public final void setPageTransformer(final boolean reverseDrawingOrder, final ViewPager
			.PageTransformer transformer) {
		viewPager.setPageTransformer(reverseDrawingOrder, transformer);
	}


	// Methods for listing to page changes

	/**
	 * Registers a listener to receive a callback whenever the page changes.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	public final void addPageChangeListener(final OnPageChangeListener listener) {
		viewPager.addOnPageChangeListener(listener);
	}

	/**
	 * Unregisters a listener to prevent it from receiving further page change callbacks.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	public final void removePageChangeListener(final OnPageChangeListener listener) {
		viewPager.removeOnPageChangeListener(listener);
	}


	// Methods relating to the pages and navigation

	/**
	 * Returns an unmodifiable Collection containing the pages.
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
	public final Fragment getPage(final int pageIndex) {
		return pages.get(pageIndex);
	}

	/**
	 * @return the page currently being displayed
	 */
	public final Fragment getCurrentPage() {
		return pages.get(viewPager.getCurrentItem());
	}

	/**
	 * @return the first page of this Activity
	 */
	public final Fragment getFirstPage() {
		return pages.get(0);
	}

	/**
	 * @return the last page of this Activity
	 */
	public final Fragment getLastPage() {
		return pages.get(pages.size() - 1);
	}

	/**
	 * Returns the index of the specified page, or -1 if the page does not exist in the page
	 * Collection.
	 *
	 * @param page
	 * 		the page to get the index of
	 * @return the index of {@code page}, counting from zero
	 */
	public final int getIndexOfPage(final Fragment page) {
		return pages.indexOf(page);
	}

	/**
	 * @return the index of the page currently being displayed, counting from zero
	 */
	public final int getIndexOfCurrentPage() {
		return viewPager.getCurrentItem();
	}

	/**
	 * Navigates to the page at the supplied index.
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
	 * Navigates to the last page (if not already there).
	 */
	public final void goToLastPage() {
		viewPager.setCurrentItem(pages.size() - 1);
	}

	/**
	 * Navigates to the first page (if not already there).
	 */
	public final void goToFirstPage() {
		viewPager.setCurrentItem(0);
	}

	/**
	 * Navigates to the next page (if not already there).
	 */
	public final void goToNextPage() {
		final boolean isLastPage = viewPager.getCurrentItem() == (pages.size() - 1);

		if (!isLastPage) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
		}
	}

	/**
	 * Navigates to the previous page (if not already there).
	 */
	public final void goToPreviousPage() {
		final boolean isFirstPage = viewPager.getCurrentItem() == 0;

		if (!isFirstPage) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
		}
	}

	/**
	 * @return the current number of pages
	 */
	public final int numberOfPages() {
		return pages.size();
	}

	/**
	 * Sets the paging lock mode. This can be used to prevent the user from navigating to another
	 * page by swiping, pressing buttons, or both. The lock mode can also disable programmatic
	 * commends.
	 *
	 * @param lockMode
	 * 		the lock mode to use, not null
	 * @throws IllegalArgumentException
	 * 		if {@code lockMode} is null
	 */
	public final void setPagingLockMode(final LockMode lockMode) {
		viewPager.setLockMode(lockMode); // throws exception is lockMode is null
	}

	/**
	 * @return the current lock mode
	 */
	public final LockMode getPagingLockMode() {
		return viewPager.getLockMode();
	}


	// Methods relating to the background manager

	/**
	 * Sets the background manager to use when scrolling through pages. The {@link
	 * BackgroundManager#updateBackground(View, int, float)} method of the supplied manager will be
	 * invoked whenever the user scrolls. Note that the BackgroundManager draws behind the pages,
	 * therefore the background will be obscured if the pages do not have transparent backgrounds.
	 *
	 * @param backgroundManager
	 * 		the backgroundManager to use, null to clear any existing manager
	 */
	public final void setBackgroundManager(final BackgroundManager backgroundManager) {
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
	 * Sets the selection indicator to show the user's progress through the Activity. The provided
	 * selection indicator must be a subclass of {@link View}.
	 *
	 * @param selectionIndicator
	 * 		the selection indicator to use, null to clear any existing indicator
	 * @throws IllegalArgumentException
	 * 		if {@code selectionIndicator} is not a View subclass
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
	 * @return the current selection indicator, may be null
	 */
	public SelectionIndicator getProgressIndicator() {
		return progressIndicator;
	}

	/**
	 * Enables/disables progress indicator page change animations.
	 *
	 * @param enableAnimations
	 * 		true to enable animations, false to disable them
	 */
	public void enableProgressIndicatorAnimations(final boolean enableAnimations) {
		progressIndicatorAnimationsEnabled = enableAnimations;
	}

	/**
	 * @return true if progress indicator page change animations are enabled, false otherwise
	 */
	public boolean progressIndicatorAnimationsAreEnabled() {
		return progressIndicatorAnimationsEnabled;
	}


	// Methods relating to the buttons

	/**
	 * Called in {@code onCreate(Bundle)} to generate an AnimatorFactory for the buttons. The
	 * factory will be used to animate the change whenever a button is enabled or disabled. The
	 * default factory causes the buttons to fade in when enabled and fade out when disabled.
	 *
	 * @return an AnimatorFactory to use when buttons are enabled/disabled, not null
	 */
	protected AnimatorFactory generateButtonAnimatorFactory() {
		return new FadeAnimatorFactory();
	}

	/**
	 * Returns an an IntroButtonAccessor which can be used to modify and inspect the left button.
	 *
	 * @return the left button accessor, not null
	 */
	public IntroButtonAccessor getLeftButtonAccessor() {
		return new IntroButtonAccessor(leftButton);
	}

	/**
	 * Returns an an IntroButtonAccessor which can be used to modify and inspect the right button.
	 *
	 * @return the right button accessor, not null
	 */
	public IntroButtonAccessor getRightButtonAccessor() {
		return new IntroButtonAccessor(rightButton);
	}

	/**
	 * Returns an an IntroButtonAccessor which can be used to modify and inspect the final button.
	 *
	 * @return the final button accessor, not null
	 */
	public IntroButtonAccessor getFinalButtonAccessor() {
		return new IntroButtonAccessor(finalButton);
	}

	/**
	 * Disables the left button on all pages by making it invisible and un-clickable. This method
	 * takes precedence over {@link #disableLeftButtonOnLastPage(boolean)}.
	 *
	 * @param disabled
	 * 		true to disable the button, false to enable it
	 */
	public final void disableLeftButton(final boolean disabled) {
		leftButtonDisabled = disabled;
		reflectMemberVariablesInLeftButton();
	}

	/**
	 * Sets whether or not the left button should be automatically disabled on the last page and
	 * re-enabled when returning to a previous page. The {@link #disableLeftButton(boolean)} method
	 * takes precedence over this method when disabling, however values passed to this method are
	 * still stored. This means that if the left button is enabled using the other method after
	 * false was passed to this method, then the left button will still be disabled on the last
	 * page.
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
	 * Returns whether or not the left button is currently disabled on all pages. This method does
	 * not take into account {@link #disableLeftButtonOnLastPage(boolean)} in any way.
	 *
	 * @return true if the button is currently entirely, false otherwise
	 */
	public final boolean leftButtonIsEntirelyDisabled() {
		return leftButtonDisabled;
	}

	/**
	 * Returns whether or not the left button will be disabled when the last page is displayed. This
	 * method does not take into account whether or not the button is has been entirely disabled
	 * using {@link #disableLeftButton(boolean)}.
	 *
	 * @return true if the left button will be disabled while the last page is displayed, false
	 * otherwise
	 */
	public final boolean leftButtonIsDisabledOnLastPage() {
		return disableLeftButtonOnLastPage;
	}

	/**
	 * Disables the right button on all pages by making it invisible and un-clickable.
	 *
	 * @param disabled
	 * 		true to disable the button, false to enable it
	 */
	public final void disableRightButton(final boolean disabled) {
		rightButtonDisabled = disabled;
		reflectMemberVariablesInRightButton();
	}

	/**
	 * Returns whether or not the right button is currently disabled on all pages.
	 *
	 * @return true if the button is currently disabled, false otherwise
	 */
	public final boolean rightButtonIsDisabled() {
		return rightButtonDisabled;
	}

	/**
	 * Disables the final button on all pages by making it invisible and un-clickable.
	 *
	 * @param disabled
	 * 		true to disable the button, false to enable it
	 */
	public final void disableFinalButton(final boolean disabled) {
		finalButtonDisabled = disabled;
		reflectMemberVariablesInFinalButton();
	}

	/**
	 * Returns whether or not the final button is currently disabled on all pages.
	 *
	 * @return true if the button is currently disabled, false otherwise
	 */
	public final boolean finalButtonIsDisabled() {
		return finalButtonDisabled;
	}
}