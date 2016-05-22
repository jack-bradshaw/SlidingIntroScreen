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

public class LockableViewPager extends ViewPager {
	/**
	 * Used to identify this class during debugging.
	 */
	private static final String TAG = "[LockableViewPager]";

	/**
	 * Determines how the page can be changed.
	 */
	private LockMode lockMode = LockMode.UNLOCKED;

	/**
	 * Constructs a new LockableViewPager instance.
	 *
	 * @param context
	 * 		the Context in which this LockableViewPager is operating
	 */
	public LockableViewPager(Context context) {
		super(context);
	}

	/**
	 * Constructs a new LockableViewPager instance.
	 *
	 * @param context
	 * 		the Context in which this LockableViewPager is operating
	 * @param attrs
	 * 		an attribute in the current theme that contains a reference to a style resource that
	 * 		supplies defaults values for the StyledAttributes, or 0 to not look for defaults
	 */
	public LockableViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Sets the lock mode. Use this method to prevent the user from changing the page in different
	 * ways.
	 *
	 * @param lockMode
	 * 		the lock mode to use, not null
	 */
	public void setLockMode(final LockMode lockMode) {
		if (lockMode == null) {
			throw new IllegalArgumentException("lockMode cannot be null");
		}

		this.lockMode = lockMode;
	}

	/**
	 * @return the current lock mode
	 */
	public LockMode getLockMode() {
		return lockMode;
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent ev) {
		return !lockMode.allowsTouch() || super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent ev) {
		return !lockMode.allowsTouch() || super.onTouchEvent(ev);
	}

	@Override
	public void fakeDragBy(final float xOffset) {
		if (lockMode.allowsCommands()) {
			super.fakeDragBy(xOffset);
		}
	}

	@Override
	public void setCurrentItem(final int item) {
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
		if (lockMode.allowsCommands()) {
			super.setCurrentItem(item, smoothScroll);
		}
	}

	/**
	 * The ways in which a {@link LockableViewPager} can be locked.
	 */
	public enum LockMode {
		/**
		 * Ignore page change touch events.
		 */
		TOUCH_LOCKED(false, true),

		/**
		 * Ignore programmatic commands to change the page, including fake drag commands.
		 */
		COMMAND_LOCKED(true, false),

		/**
		 * Ignore touch events and programmatic commands to change the page.
		 */
		FULLY_LOCKED(false, false),

		/**
		 * Not locked.
		 */
		UNLOCKED(true, true);

		/**
		 * Indicates whether or not this LockMode allows page change touch events.
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
		 * 		whether or not this LockMode allows page change touch events
		 * @param allowsCommands
		 * 		whether or not this LockMode allows programmatic commands to change the page
		 */
		LockMode(final boolean allowsTouch, final boolean allowsCommands) {
			this.allowsTouch = allowsTouch;
			this.allowsCommands = allowsCommands;
		}

		/**
		 * @return true if this LockMode allows page change touch events, false otherwise
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