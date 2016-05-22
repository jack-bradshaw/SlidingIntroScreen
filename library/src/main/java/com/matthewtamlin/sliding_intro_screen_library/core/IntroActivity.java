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

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.matthewtamlin.android_utilities_library.collections.ArrayListWithCallbacks;
import com.matthewtamlin.android_utilities_library.helpers.ColorHelper;
import com.matthewtamlin.android_utilities_library.helpers.StatusBarHelper;
import com.matthewtamlin.sliding_intro_screen_library.core.IntroButton.Appearance;
import com.matthewtamlin.sliding_intro_screen_library.core.IntroButton.Behaviour;
import com.matthewtamlin.sliding_intro_screen_library.core.LockableViewPager.LockMode;
import com.matthewtamlin.sliding_intro_screen_library.R;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;
import com.matthewtamlin.sliding_intro_screen_library.indicators.SelectionIndicator;
import com.matthewtamlin.sliding_intro_screen_library.pages.Page;

import java.util.Collection;
import java.util.Collections;

/**
 * Displays an introduction screen to the user, consisting of a series of pages and a navigation
 * bar. The pages display the content of the introduction, and the navigation bar displays the
 * user's progress through the activity. The navigation bar also provides buttons for moving between
 * pages and advancing to the next activity. To use this class, subclass it and implement {@link
 * #generatePages(Bundle)} and {@link #generateFinalButtonBehaviour()}. It is recommended that the
 * manifest entry declare {@code android:noHistory="true"} to prevent the user from navigating back
 * to this activity once finished. <p> {@link #generatePages(Bundle)} is called by {@link
 * #onCreate(Bundle)} to generate the {@link Page} elements to display in the activity. The method
 * must return a collection, it cannot return null. Pages cannot be added or removed from the
 * activity after this method returns. {@link #generateFinalButtonBehaviour()} is called by {@link
 * #onCreate(Bundle)} to generate the behaviour to assign to the final button. The method must
 * return a Behaviour, it cannot return null. The behaviour of the button defines what happens when
 * the button is pressed. The {@link IntroButton
 * .ProgressToNextActivity} abstract class is designed to facilitate validation conditions to check
 * that the activity should finish, and it provides a mechanism for setting a shared preferences
 * flag to prevent the user from being shown the intro screen again. <p> The navigation bar contains
 * three buttons: a left button, a right button and a final button. The left button is present on
 * all pages, but by default it is not displayed on the last page. The button can be shown on the
 * last page by calling {@link #hideLeftButtonOnLastPage (boolean)}. The right button is present on
 * all pages but the last, and this cannot be changed as the final button replaces the right button
 * on the last page. By default, the left button skips ahead to the last page and the right button
 * moves to the next page. The behaviour of the final button is generated in {@link
 * #generateFinalButtonBehaviour()}. The behaviour of each button can be changed using the
 * respective 'set behaviour' method. The appearance of each button can also be changed using the
 * respective 'set appearance' method. These methods make it easy to display text, an icon, or both
 * in each button. The other methods of this activity allow finer control over the appearance of
 * each button. <p> The methods of this activity also provide the following customisation options:
 * <li>Hiding/showing the status bar.</li> <li>Programmatically changing the page.</li> <li>Locking
 * the page to prevent touch events and/or commands (e.g. button presses) from changing the
 * page.</li> <li>Modifying/replacing the progress indicator.</li>
 */
public abstract class IntroActivity extends AppCompatActivity
		implements ViewPager.OnPageChangeListener, ArrayListWithCallbacks.OnListChangedListener {
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
	private static final int DEFAULT_CURRENT_PAGE_INDEX = 0;

	/**
	 * The Behaviour to use for the left button until it is explicitly set.
	 */
	private final Behaviour DEFAULT_LEFT_BUTTON_BEHAVIOUR = new IntroButton.GoToLastPage();

	/**
	 * The Behaviour to use for the right button until it is explicitly set.
	 */
	private final Behaviour DEFAULT_RIGHT_BUTTON_BEHAVIOUR = new IntroButton.GoToNextPage();

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
	 * The root view of this activity.
	 */
	private RelativeLayout rootView;

	/**
	 * Displays the elements of {@code pages} to the user.
	 */
	private LockableViewPager viewPager;

	/**
	 * Holds the selection indicator.
	 */
	private FrameLayout progressIndicatorHelper;

	/**
	 * An IntroButton which is displayed at the bottom left of the navigation bar. This button is
	 * hidden on the last page by default, however this can be changed using {@link
	 * #hideLeftButtonOnLastPage(boolean)}.
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
	 * Displays the user's progress through the intro screen.
	 */
	private SelectionIndicator progressIndicator;


	/**
	 * The pages to display in {@code viewPager}.
	 */
	private final ArrayListWithCallbacks<Page> pages = new ArrayListWithCallbacks<>();

	/**
	 * Adapts the elements of {@code pages} to {@code viewPager}.
	 */
	private final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), pages);


	/**
	 * Whether or not changes in the progress indicator should be animated.
	 */
	private boolean progressIndicatorAnimationsEnabled = true;

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
	 * Whether or not {@code leftButton} should be hidden when the last page is being displayed.
	 */
	private boolean hideLeftButtonOnLastPage = true;


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
		registerListeners();

		for (final Page p : generatePages(savedInstanceState)) {
			pages.add(p);
		}

		initialiseNavigationButtons();
		initialiseViewPager(savedInstanceState);
		updateButtonVisibilities();
		progressIndicator = new DotIndicator(this);
		regenerateProgressIndicator();
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

	@Override
	public void onPageScrolled(final int position, final float positionOffset,
			final int positionOffsetPixels) {
		//TODO
	}

	@Override
	public void onPageSelected(final int position) {
		updateButtonVisibilities();
		if (progressIndicator != null) {
			progressIndicator.setSelectedItem(position, progressIndicatorAnimationsEnabled);
		}
	}

	@Override
	public void onPageScrollStateChanged(final int state) {
		// Forced to implement this method with onPageSelected(int)
	}

	@Override
	public void onItemAdded(final ArrayListWithCallbacks list, final Object itemAdded,
			final int index) {
		if (list == pages) {
			updateButtonVisibilities();
			regenerateProgressIndicator();
		}
	}

	@Override
	public void onItemRemoved(final ArrayListWithCallbacks list, final Object itemRemoved,
			final int index) {
		if (list == pages) {
			updateButtonVisibilities();
			regenerateProgressIndicator();
		}
	}

	@Override
	public void onListCleared(final ArrayListWithCallbacks list) {
		if (list == pages) {
			updateButtonVisibilities();
			regenerateProgressIndicator();
		}
	}

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
	 * Registers event listeners for {@code viewPager}, {@code pages}.
	 */
	private void registerListeners() {
		viewPager.addOnPageChangeListener(this);
		pages.addOnListChangedListener(this);
	}

	/**
	 * Initialises UI elements related to displaying the current page. If this activity is being
	 * restored then the previously open page is restored.
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
		updateBackground(0, 1);
	}

	/**
	 * Sets the behaviour and appearance of the left and right buttons, and sets {@code viewPager}
	 * as their action target.
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
	 * Updates the visibility of the buttons, and makes them un-clickable if hidden.
	 */
	private void updateButtonVisibilities() {
		final boolean reachedLastPage = (viewPager.getCurrentItem() + 1) == pages.size();
		final boolean disableLeftButton =
				(hideLeftButtonOnLastPage && reachedLastPage) || leftButtonDisabled;
		final boolean disableRightButton = reachedLastPage || rightButtonDisabled;
		final boolean disableFinalButton = !reachedLastPage || finalButtonDisabled;

		// Update left button
		if (disableLeftButton) {
			leftButton.setVisibility(View.INVISIBLE);
			leftButton.setEnabled(false);
		} else {
			leftButton.setVisibility(View.VISIBLE);
			leftButton.setEnabled(true);
		}

		// Update right button
		if (disableRightButton) {
			rightButton.setVisibility(View.INVISIBLE);
			rightButton.setEnabled(false);
		} else {
			rightButton.setVisibility(View.VISIBLE);
			rightButton.setEnabled(true);
		}

		// Update final button
		if (disableFinalButton) {
			finalButton.setVisibility(View.INVISIBLE);
			finalButton.setEnabled(false);
		} else {
			finalButton.setVisibility(View.VISIBLE);
			finalButton.setEnabled(true);
		}
	}


	/**
	 * Removes {@code progressIndicator} from the view and reattaches it. The active item is
	 * updated.
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
	protected abstract Collection<Page> generatePages(Bundle savedInstanceState);

	/**
	 * Called by {@link #onCreate(Bundle)} to generate the behaviour of the final button. This
	 * behaviour can be changed later using {@link #setFinalButtonBehaviour(Behaviour)}. The {@link
	 * IntroButton.ProgressToNextActivity} class is
	 * designed to simplify the implementation.
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


	/**
	 * @return a reference to the top most view of this activity
	 */
	public final RelativeLayout getRootView() {
		return rootView;
	}


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

	/**
	 * Returns a reference to the pages of this activity, as an unmodifiable collection.
	 *
	 * @return the pages of this activity
	 */
	public final Collection<Page> getPages() {
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
	public final Page getPage(int pageIndex) {
		return pages.get(pageIndex);
	}

	/**
	 * @return the Page currently being displayed
	 */
	public final Page getCurrentPage() {
		return pages.get(viewPager.getCurrentItem());
	}

	/**
	 * @return the first Page of this activity
	 */
	public final Page getFirstPage() {
		return pages.get(0);
	}

	/**
	 * @return the last Page of this activity
	 */
	public final Page getLastPage() {
		return pages.get(pages.size() - 1);
	}

	/**
	 * Returns the index of the specified page, or -1 if the page is not in this activity.
	 *
	 * @param page
	 * 		the Page to get the index of
	 * @return the index of {@code page}, counting from zero
	 */
	public final int getIndexOfPage(Page page) {
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


	/**
	 * Sets the behaviour of the left button. This is distinct from the on-click behaviour, which
	 * can be set using {@link #setLeftButtonOnClickListener(OnClickListener)}. Predefined
	 * behaviours are provided in the {@link IntroButton} class, however custom behaviours are
	 * accepted. To use a custom behaviour, implement {@link Behaviour} and pass an instance of the
	 * implementation to this method. Alternatively, subclassing {@link
	 * IntroButton.BehaviourAdapter} simplifies the
	 * implementation and eliminates boilerplate code.
	 *
	 * @param behaviour
	 * 		the behaviour to use when left button is pressed, not null
	 */
	public final void setLeftButtonBehaviour(final Behaviour behaviour) {
		leftButton.setBehaviour(behaviour);
	}

	/**
	 * @return the current behaviour of the left button
	 */
	public final Behaviour getLeftButtonBehaviour() {
		return leftButton.getBehaviour();
	}

	/**
	 * Sets the appearance of the left button.
	 *
	 * @param appearance
	 * 		the predefined appearance to use, not null
	 */
	public final void setLeftButtonAppearance(final Appearance appearance) {
		leftButton.setAppearance(appearance);
	}

	/**
	 * @return the current Appearance of the left button
	 */
	public final Appearance getLeftButtonAppearance() {
		return leftButton.getAppearance();
	}

	/**
	 * Sets the text to display in the left button for a single behaviour class. The current
	 * appearance of the button determines whether or not the text will actually be displayed.
	 * Although null is accepted, changing the button appearance using {@link
	 * #setLeftButtonAppearance(Appearance)} is the preferred method of showing no text.
	 *
	 * @param text
	 * 		the text label to display
	 * @param behaviourClass
	 * 		the behaviour class to set the label for, null to use the class of the current behaviour
	 */
	public final void setLeftButtonText(final CharSequence text,
			final Class<? extends Behaviour> behaviourClass) {
		leftButton.setLabel(text, behaviourClass);
	}

	/**
	 * Returns the text to display in the left button for a particular behaviour class.
	 *
	 * @param behaviourClass
	 * 		the behaviour class to query the text against, null to use the class of the current
	 * 		behaviour
	 * @return the text for {@code behaviourClass}, null if there is none
	 */
	public final CharSequence getLeftButtonText(final Class<? extends Behaviour> behaviourClass) {
		return leftButton.getLabel(behaviourClass);
	}

	/**
	 * Sets the icon to display in the left button for a single behaviour class. The current
	 * appearance of the button determines whether or not the icon will actually be displayed, and
	 * where it will be located. Although null is accepted, changing the button appearance using
	 * {@link #setLeftButtonAppearance(Appearance)} is the preferred method of showing no icon.
	 *
	 * @param icon
	 * 		the icon to display
	 * @param behaviourClass
	 * 		the behaviour class to set the icon for, null to use the class of the current appearance
	 */
	public final void setLeftButtonIcon(final Drawable icon,
			final Class<? extends Behaviour> behaviourClass) {
		leftButton.setIcon(icon, behaviourClass);
	}

	/**
	 * Returns the icon to display in the left button for a particular behaviour.
	 *
	 * @param behaviourClass
	 * 		the behaviour class to query the icon against, null to use the class of the current
	 * 		behaviour
	 * @return the icon for {@code behaviourClass}, null if there is none
	 */
	public final Drawable getLeftButtonIcon(final Class<? extends Behaviour> behaviourClass) {
		return leftButton.getIcon(behaviourClass);
	}

	/**
	 * Sets the color of the text in the left button. The color is common to all behaviours.
	 *
	 * @param color
	 * 		the color to use, as an ARGB hex code
	 */
	public final void setLeftButtonTextColor(final int color) {
		leftButton.setTextColor(color);
	}

	/**
	 * Returns the color of the text in the left button. The color is common to all behaviours.
	 *
	 * @return the text color, as an ARGB hex code
	 */
	public final int getLeftButtonTextColor() {
		return leftButton.getCurrentTextColor();
	}

	/**
	 * Sets the size of the text in the left button. The size is common to all behaviours.
	 *
	 * @param sizeSp
	 * 		the size to use, measured in scaled-pixels
	 */
	public final void setLeftButtonTextSize(final float sizeSp) {
		leftButton.setTextSize(sizeSp);
	}

	/**
	 * Returns the size of the text in the left button. The size is common to all behaviours.
	 *
	 * @return the text size, measured in pixels
	 */
	public final float getLeftButtonTextSize() {
		return leftButton.getTextSize();
	}

	/**
	 * Sets the typeface and style in which the text of the left button should be displayed. Turns
	 * on the fake bold and italic bits in the Paint if the Typeface that you provided does not have
	 * all the bits in the style that you specified.
	 *
	 * @param tf
	 * 		the typeface to use
	 * @param style
	 * 		style for the typeface
	 */
	public final void setLeftButtonTypeface(final Typeface tf, final int style) {
		leftButton.setTypeface(tf, style);
	}

	/**
	 * Sets the typeface and style in which the text of the left button should be displayed. Turns
	 * on the fake bold and italic bits in the Paint if the Typeface that you provided does not have
	 * all the bits in the style that you specified.
	 *
	 * @param tf
	 * 		the typeface to use
	 */
	public final void setLeftButtonTypeface(final Typeface tf) {
		leftButton.setTypeface(tf);
	}

	/**
	 * Register a callback to be invoked when the left button is clicked. If the button is not
	 * clickable, it becomes clickable. Registering for on-click events does not prevent the left
	 * button from performing its predefined behaviour.
	 *
	 * @param l
	 * 		the callback that will run, null to clear the existing listener
	 */
	public final void setLeftButtonOnClickListener(final OnClickListener l) {
		leftButton.setOnClickListener(l);
	}

	/**
	 * Hides and disables the left button. This overrides any
	 *
	 * @param disabled
	 * 		whether or not the button should be hidden/disabled
	 */
	public final void disableLeftButton(final boolean disabled) {
		leftButtonDisabled = disabled;
		updateButtonVisibilities();
	}

	/**
	 * Returns whether or not the left button is currently disabled and hidden.
	 *
	 * @return true if disabled and hidden, false otherwise
	 */
	public final boolean leftButtonIsDisabled() {
		return leftButtonDisabled;
	}

	/**
	 * Sets whether or not the left button should be hidden when the last page is displayed. If
	 * {@link #disableFinalButton(boolean)} has been used to disable the left button then calling
	 * this method will not display the left button. The setting will still be recorded so that if
	 * the button is later enabled then the last page behaviour is retained.
	 *
	 * @param hideButton
	 * 		true to hide the left button on the last page, false to show it
	 */
	public final void hideLeftButtonOnLastPage(final boolean hideButton) {
		hideLeftButtonOnLastPage = hideButton;
		updateButtonVisibilities();
	}

	/**
	 * Returns whether or not the left button will be hidden when the last page is displayed. This
	 * method ignores whether or not the button is disabled/enabled.
	 *
	 * @return true if the left button will be hidden when the last page is displayed, false
	 * otherwise
	 */
	public final boolean leftButtonIsHiddenOnLastPage() {
		return hideLeftButtonOnLastPage;
	}

	/**
	 * Sets the behaviour of the right button when clicked. This is distinct from the on-click
	 * behaviour, which can be set using {@link #setRightButtonOnClickListener(OnClickListener)}.
	 * Predefined behaviours are provided in the {@link IntroButton} class, however custom
	 * behaviours are accepted. To use a custom behaviour, implement {@link Behaviour} and pass an
	 * instance of the implementation to this method. Alternatively, subclassing {@link
	 * IntroButton.BehaviourAdapter} simplifies the
	 * implementation and eliminates boilerplate code.
	 *
	 * @param behaviour
	 * 		the behaviour to use when the right button is pressed, not null
	 */
	public final void setRightButtonBehaviour(final Behaviour behaviour) {
		rightButton.setBehaviour(behaviour);
	}

	/**
	 * @return the current Behaviour of the right button
	 */
	public final Behaviour getRightButtonBehaviour() {
		return rightButton.getBehaviour();
	}

	/**
	 * Sets the appearance of the right button.
	 *
	 * @param appearance
	 * 		the predefined appearance to use, not null
	 */
	public final void setRightButtonAppearance(final Appearance appearance) {
		rightButton.setAppearance(appearance);
	}

	/**
	 * @return the current Appearance of the right button
	 */
	public final Appearance getRightButtonAppearance() {
		return rightButton.getAppearance();
	}

	/**
	 * Sets the text to display in the right button for a single behaviour class. The current
	 * appearance of the button determines whether or not the text will actually be displayed.
	 * Although null is accepted, changing the button appearance using {@link
	 * #setLeftButtonAppearance(Appearance)} is the preferred method of showing no text.
	 *
	 * @param text
	 * 		the text label to display
	 * @param behaviourClass
	 * 		the behaviour class to set the label for, null to use the class of the current behaviour
	 */
	public final void setRightButtonText(final CharSequence text,
			final Class<? extends Behaviour> behaviourClass) {
		rightButton.setLabel(text, behaviourClass);
	}

	/**
	 * Returns the text to display in the right button for a particular behaviour class.
	 *
	 * @param behaviourClass
	 * 		the behaviour class to query the text against, null to use the class of the current
	 * 		behaviour
	 * @return the text for {@code behaviourClass}, null if there is none
	 */
	public final CharSequence getRightButtonText(
			final Class<? extends IntroButton.Behaviour> behaviourClass) {
		return rightButton.getLabel(behaviourClass);
	}

	/**
	 * Sets the icon to display in the right button for a single behaviour class. The current
	 * appearance of the button determines whether or not the icon will actually be displayed, and
	 * where it will be located. Although null is accepted, changing the button appearance using
	 * {@link #setRightButtonAppearance(IntroButton.Appearance)} is the preferred method of showing
	 * no icon.
	 *
	 * @param icon
	 * 		the icon to display
	 * @param behaviourClass
	 * 		the behaviour class to set the icon for, null to use the class of the current appearance
	 */
	public final void setRightButtonIcon(final Drawable icon,
			final Class<? extends IntroButton.Behaviour> behaviourClass) {
		rightButton.setIcon(icon, behaviourClass);
	}

	/**
	 * Returns the icon to display in the right button for a particular behaviour.
	 *
	 * @param behaviourClass
	 * 		the behaviour class to query the icon against, null to use the class of the current
	 * 		behaviour
	 * @return the icon for {@code behaviourClass}, null if there is none
	 */
	public final Drawable getRightButtonIcon(
			final Class<? extends IntroButton.Behaviour> behaviourClass) {
		return rightButton.getIcon(behaviourClass);
	}

	/**
	 * Sets the text color of the right button. The color is common to all behaviours.
	 *
	 * @param color
	 * 		the color to use, as an ARGB hex code
	 */
	public final void setRightButtonTextColor(final int color) {
		rightButton.setTextColor(color);
	}

	/**
	 * Returns the color of the text in the right button. The color is common to all behaviours.
	 *
	 * @return the text color, as an ARGB hex code
	 */
	public final int getRightButtonTextColor() {
		return rightButton.getCurrentTextColor();
	}

	/**
	 * Sets the size of the text in the right button. The size is common to all behaviours.
	 *
	 * @param sizeSp
	 * 		the size to use, measured in scaled-pixels
	 */
	public final void setRightButtonTextSize(float sizeSp) {
		rightButton.setTextSize(sizeSp);
	}

	/**
	 * Returns the size of the text in the right button. The size is common to all behaviours.
	 *
	 * @return the text size, measured in pixels
	 */
	public final float getRightButtonTextSize() {
		return rightButton.getTextSize();
	}

	/**
	 * Sets the typeface and style in which the text of the right button should be displayed. Turns
	 * on the fake bold and italic bits in the Paint if the Typeface that you provided does not have
	 * all the bits in the style that you specified.
	 *
	 * @param tf
	 * 		the typeface to use
	 * @param style
	 * 		style for the typeface
	 */
	public final void setRightButtonTypeface(final Typeface tf, final int style) {
		rightButton.setTypeface(tf, style);
	}

	/**
	 * Sets the typeface and style in which the text of the right button should be displayed. Turns
	 * on the fake bold and italic bits in the Paint if the Typeface that you provided does not have
	 * all the bits in the style that you specified.
	 *
	 * @param tf
	 * 		the typeface to use
	 */
	public final void setRightButtonTypeface(final Typeface tf) {
		rightButton.setTypeface(tf);
	}

	/**
	 * Register a callback to be invoked when the right button is clicked. If the button is not
	 * clickable, it becomes clickable. Registering for on-click events does not prevent the right
	 * button from performing its predefined behaviour.
	 *
	 * @param l
	 * 		the callback that will run, null to clear the existing listener
	 */
	public final void setRightButtonOnClickListener(final OnClickListener l) {
		rightButton.setOnClickListener(l);
	}

	/**
	 * Hides and disables the right button.
	 *
	 * @param disabled
	 * 		whether or not the button should be hidden/disabled
	 */
	public final void disableRightButton(final boolean disabled) {
		rightButtonDisabled = disabled;
		updateButtonVisibilities();
	}

	/**
	 * Returns whether or not the right button is currently disabled and hidden.
	 *
	 * @return true if disabled and hidden, false otherwise
	 */
	public final boolean rightButtonIsDisabled() {
		return rightButtonDisabled;
	}


	/**
	 * Sets the behaviour of the final button. This is distinct from the on-click behaviour, which
	 * can be set using {@link #setLeftButtonOnClickListener(OnClickListener)}. Predefined
	 * behaviours are provided in the {@link IntroButton} class, however custom behaviours are
	 * accepted. To use a custom behaviour, implement {@link Behaviour} and pass an instance of the
	 * implementation to this method. Alternatively, subclassing {@link
	 * IntroButton.BehaviourAdapter} simplifies the
	 * implementation and eliminates boilerplate code.
	 *
	 * @param behaviour
	 * 		the behaviour to use when left button is pressed, not null
	 */
	public final void setFinalButtonBehaviour(final Behaviour behaviour) {
		finalButton.setBehaviour(behaviour);
	}

	/**
	 * @return the current behaviour of the final button
	 */
	public final Behaviour getFinalButtonBehaviour() {
		return finalButton.getBehaviour();
	}

	/**
	 * Sets the appearance of the final button.
	 *
	 * @param appearance
	 * 		the predefined appearance to use, not null
	 */
	public final void setFinalButtonAppearance(final IntroButton.Appearance appearance) {
		finalButton.setAppearance(appearance);
	}

	/**
	 * @return the current Appearance of the final button
	 */
	public final IntroButton.Appearance getFinalButtonAppearance() {
		return finalButton.getAppearance();
	}

	/**
	 * Sets the text to display in the final button for a single behaviour class. The current
	 * appearance of the button determines whether or not the text will actually be displayed.
	 * Although null is accepted, changing the button appearance using {@link
	 * #setLeftButtonAppearance(Appearance)} is the preferred method of showing no text.
	 *
	 * @param text
	 * 		the text label to display
	 * @param behaviourClass
	 * 		the behaviour class to set the label for, null to use the class of the current behaviour
	 */
	public final void setFinalButtonText(final CharSequence text,
			final Class<? extends Behaviour> behaviourClass) {
		finalButton.setLabel(text, behaviourClass);
	}

	/**
	 * Returns the text to display in the final button for a particular behaviour class.
	 *
	 * @param behaviourClass
	 * 		the behaviour class to query the text against, null to use the class of the current
	 * 		behaviour
	 * @return the text for {@code behaviourClass}, null if there is none
	 */
	public final CharSequence getFinalButtonText(
			final Class<? extends IntroButton.Behaviour> behaviourClass) {
		return finalButton.getLabel(behaviourClass);
	}

	/**
	 * Sets the icon to display in the final button for a single behaviour class. The current
	 * appearance of the button determines whether or not the icon will actually be displayed, and
	 * where it will be located. Although null is accepted, changing the button appearance using
	 * {@link #setFinalButtonAppearance(IntroButton.Appearance)} is the preferred method of showing
	 * no icon.
	 *
	 * @param icon
	 * 		the icon to display
	 * @param behaviourClass
	 * 		the behaviour class to set the icon for, null to use the class of the current appearance
	 */
	public final void setFinalButtonIcon(final Drawable icon,
			final Class<? extends IntroButton.Behaviour> behaviourClass) {
		finalButton.setIcon(icon, behaviourClass);
	}

	/**
	 * Returns the icon to display in the final button for a particular behaviour.
	 *
	 * @param behaviourClass
	 * 		the behaviour class to query the icon against, null to use the class of the current
	 * 		behaviour
	 * @return the icon for {@code behaviourClass}, null if there is none
	 */
	public final Drawable getFinalButtonIcon(
			final Class<? extends IntroButton.Behaviour> behaviourClass) {
		return finalButton.getIcon(behaviourClass);
	}

	/**
	 * Sets the color of the text in the final button. The color is common to all behaviours.
	 *
	 * @param color
	 * 		the color to use, as an ARGB hex code
	 */
	public final void setFinalButtonTextColor(final int color) {
		finalButton.setTextColor(color);
	}

	/**
	 * Returns the color of the text in the final button. The color is common to all behaviours.
	 *
	 * @return the text color, as an ARGB hex code
	 */
	public final int getFinalButtonTextColor() {
		return finalButton.getCurrentTextColor();
	}

	/**
	 * Sets the size of the text in the final button. The size is common to all behaviours.
	 *
	 * @param sizeSp
	 * 		the size to use, measured in scaled-pixels
	 */
	public final void setFinalButtonTextSize(final float sizeSp) {
		finalButton.setTextSize(sizeSp);
	}

	/**
	 * Returns the size of the text in the final button. The size is common to all behaviours.
	 *
	 * @return the text size, measured in pixels
	 */
	public final float getFinalButtonTextSize() {
		return finalButton.getTextSize();
	}

	/**
	 * Sets the typeface and style in which the text of the final button should be displayed. Turns
	 * on the fake bold and italic bits in the Paint if the Typeface that you provided does not have
	 * all the bits in the style that you specified.
	 *
	 * @param tf
	 * 		the typeface to use
	 * @param style
	 * 		style for the typeface
	 */
	public final void setFinalButtonTypeface(final Typeface tf, final int style) {
		finalButton.setTypeface(tf, style);
	}

	/**
	 * Sets the typeface and style in which the text of the final button should be displayed. Turns
	 * on the fake bold and italic bits in the Paint if the Typeface that you provided does not have
	 * all the bits in the style that you specified.
	 *
	 * @param tf
	 * 		the typeface to use
	 */
	public final void setFinalButtonTypeface(final Typeface tf) {
		finalButton.setTypeface(tf);
	}

	/**
	 * Register a callback to be invoked when the final button is clicked. If the button is not
	 * clickable, it becomes clickable. Registering for on-click events does not prevent the final
	 * button from performing its predefined behaviour.
	 *
	 * @param l
	 * 		the callback that will run, null to clear the existing listener
	 */
	public final void setFinalButtonOnClickListener(final View.OnClickListener l) {
		finalButton.setOnClickListener(l);
	}

	/**
	 * Hides and disables the final button.
	 *
	 * @param disabled
	 * 		whether or not the button should be hidden/disabled
	 */
	public final void disableFinalButton(final boolean disabled) {
		finalButtonDisabled = disabled;
		updateButtonVisibilities();
	}

	/**
	 * Returns whether or not the left button is currently disabled and hidden.
	 *
	 * @return true if disabled and hidden, false otherwise
	 */
	public final boolean finalButtonIsDisabled() {
		return finalButtonDisabled;
	}
}