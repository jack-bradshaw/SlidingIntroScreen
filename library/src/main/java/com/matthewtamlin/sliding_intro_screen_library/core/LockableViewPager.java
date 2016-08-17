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

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * A ViewPager which can be locked to prevent navigation. Multiple locking modes are supported.
 */
public class LockableViewPager extends ViewPager {
	/**
	 * Used to identify this class during debugging.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "[LockableViewPager]";

	/**
	 * Specifies which actions are currently prevented from changing the page.
	 */
	private LockMode lockMode = LockMode.UNLOCKED;

	/**
	 * Constructs a new LockableViewPager instance.
	 *
	 * @param context
	 * 		the Context in which this LockableViewPager is operating, not null
	 */
	public LockableViewPager(final Context context) {
		super(context);
	}

	/**
	 * Constructs a new LockableViewPager instance.
	 *
	 * @param context
	 * 		the Context in which this LockableViewPager is operating, not null
	 * @param attrs
	 * 		an attribute in the current theme that contains a reference to a style resource that
	 * 		supplies defaults values for the StyledAttributes, or 0 to not look for defaults
	 */
	public LockableViewPager(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Sets the lock mode to allow/disallow different methods of changing the page.
	 *
	 * @param lockMode
	 * 		the lock mode to use, not null
	 * @throws IllegalArgumentException
	 * 		if {@code lockMode} is null
	 */
	public void setLockMode(final LockMode lockMode) {
		if (lockMode == null) {
			throw new IllegalArgumentException("lockMode cannot be null");
		}

		this.lockMode = lockMode;
	}

	/**
	 * @return the current lock mode, not null
	 */
	public LockMode getLockMode() {
		return lockMode;
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent ev) {
		if (lockMode.allowsTouch()) {
			super.onInterceptTouchEvent(ev);
		} else {
			return false;
		}

		// Uses the current lock mode to prevent touch events if necessary
		return !lockMode.allowsTouch() || super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (lockMode.allowsTouch()) {
			return super.onTouchEvent(ev);
		} else {
			return false;
		}
	}

	@Override
	public void fakeDragBy(final float xOffset) {
		// Uses the lock mode to prevent programmatic commands from changing the page if necessary
		if (lockMode.allowsCommands()) {
			super.fakeDragBy(xOffset);
		}
	}

	@Override
	public void setCurrentItem(final int item) {
		// Uses the lock mode to prevent programmatic commands from changing the page if necessary
		if (lockMode.allowsCommands()) {
			super.setCurrentItem(item);
		}
	}

	/**
	 * Set the currently selected page.
	 *
	 * @param item
	 * 		Item index to select
	 * @param smoothScroll
	 * 		True to smoothly scroll to the new item, false to transition immediately
	 */
	@Override
	public void setCurrentItem(final int item, final boolean smoothScroll) {
		// Uses the lock mode to prevent programmatic commands from changing the page if necessary
		if (lockMode.allowsCommands()) {
			super.setCurrentItem(item, smoothScroll);
		}
	}

	/**
	 * The ways in which a {@link LockableViewPager} can be locked.
	 */
	public enum LockMode {
		/**
		 * Prevent touch events from changing the page.
		 */
		TOUCH_LOCKED(false, true),

		/**
		 * Prevent programmatic commands from changing the page, including fake drag events.
		 */
		COMMAND_LOCKED(true, false),

		/**
		 * Prevent both touch events and programmatic commands from changing the page.
		 */
		FULLY_LOCKED(false, false),

		/**
		 * Do not prevent the page from changing.
		 */
		UNLOCKED(true, true);

		/**
		 * Indicates whether or not this LockMode allows touch events to change the page.
		 */
		private final boolean allowsTouch;

		/**
		 * Indicates whether or not this LockMode allows programmatic commands to change the page.
		 */
		private final boolean allowsCommands;

		/**
		 * Constructs a new LockMode instance.
		 *
		 * @param allowsTouch
		 * 		whether or not this LockMode allows touch events to change the page
		 * @param allowsCommands
		 * 		whether or not this LockMode allows programmatic commands to change the page
		 */
		LockMode(final boolean allowsTouch, final boolean allowsCommands) {
			this.allowsTouch = allowsTouch;
			this.allowsCommands = allowsCommands;
		}

		/**
		 * @return true if this LockMode allows touch events to change the page, false otherwise
		 */
		public final boolean allowsTouch() {
			return allowsTouch;
		}

		/**
		 * @return true if this LockMode allows programmatic commands to change the page, false
		 * otherwise
		 */
		public final boolean allowsCommands() {
			return allowsCommands;
		}
	}
}