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

package com.matthewtamlin.sliding_intro_screen_library.buttons;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Provides indirect access to an IntroButton, to allow limited modification and inspection. Each
 * accessor provides access to a single IntroButton only.
 * <p/>
 * See {@link IntroButton}.
 */
public final class IntroButtonAccessor {
	/**
	 * The IntroButton this accessor provides access to.
	 */
	private final IntroButton button;

	/**
	 * Constructs a new IntroButtonAccessor instance.
	 *
	 * @param button
	 * 		the IntroButton to provide access to, not null
	 * @throws IllegalArgumentException
	 * 		if {@code button} is null
	 */
	public IntroButtonAccessor(final IntroButton button) {
		if (button == null) {
			throw new IllegalArgumentException("button cannot be null");
		}

		this.button = button;
	}

	/**
	 * Sets the Behaviour of the accessed IntroButton. The IntroButton class contains predefined
	 * Behaviours which meet most needs, but custom implementations of the Behaviour interface are
	 * also accepted. The {@link IntroButton.BehaviourAdapter} class can be used to reduce
	 * boilerplate code when implementing the interface. This method does not accept null; to do
	 * nothing when the button is clicked, pass an instance of {@link IntroButton.DoNothing}.
	 * <p/>
	 * See {@link com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Behaviour}.
	 *
	 * @param behaviour
	 * 		the Behaviour to use, not null
	 * @throws IllegalArgumentException
	 * 		if {@code behaviour} is null
	 */
	public final void setBehaviour(final IntroButton.Behaviour behaviour) {
		button.setBehaviour(behaviour); // throws IllegalArgumentException if behaviour is null
	}

	/**
	 * Returns the current Behaviour of the accessed IntroButton.
	 * <p/>
	 * See {@link com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Behaviour}.
	 *
	 * @return the current Behaviour, not null
	 */
	public final IntroButton.Behaviour getBehaviour() {
		return button.getBehaviour();
	}

	/**
	 * Sets the Appearance of the accessed IntroButton. The Appearance defines how the button is
	 * displayed.
	 * <p/>
	 * See {@link com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Appearance}.
	 *
	 * @param appearance
	 * 		the predefined Appearance to use, not null
	 * @throws IllegalArgumentException
	 * 		if {@code appearance} is null
	 */
	public final void setAppearance(final IntroButton.Appearance appearance) {
		button.setAppearance(appearance); // throws IllegalArgumentException if appearance is null
	}

	/**
	 * Returns the current Appearance of the accessed IntroButton.
	 * <p/>
	 * See {@link com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Appearance}.
	 *
	 * @return the current Appearance, not null
	 */
	public final IntroButton.Appearance getAppearance() {
		return button.getAppearance();
	}

	/**
	 * Sets the text to be displayed by the accessed IntroButton. The text will only be displayed
	 * when the Appearance is set to {@code Appearance.TEXT_ONLY}, {@code Appearance.ICON_TEXT_LEFT}
	 * or {@code Appearance.ICON_TEXT_RIGHT}. The text is linked to a Behaviour class, and will only
	 * be shown when the button is using an instance of that Behaviour class.
	 *
	 * @param text
	 * 		the text to display
	 * @param behaviourClass
	 * 		the Behaviour class to associate the text with, null to use the current Behaviour
	 */
	public final void setText(final CharSequence text, final Class<? extends IntroButton.Behaviour>
			behaviourClass) {
		button.setLabel(text, behaviourClass);
	}

	/**
	 * Returns the text displayed by the accessed IntroButton for a particular Behaviour class. Note
	 * that the text may not currently be visible.
	 *
	 * @param behaviourClass
	 * 		the Behaviour class to get the associated text of, null to use the the current Behaviour
	 * @return the text for the Behaviour class, null if there is none
	 */
	public final CharSequence getText(final Class<? extends IntroButton.Behaviour> behaviourClass) {
		return button.getLabel(behaviourClass);
	}

	/**
	 * Sets the icon to be displayed by the accessed IntroButton. The icon will only be displayed
	 * when the Appearance is set to {@code Appearance.ICON_ONLY}, {@code Appearance.ICON_TEXT_LEFT}
	 * or {@code Appearance.ICON_TEXT_RIGHT}. The icon is linked to a Behaviour class, and will only
	 * be shown when the IntroButton is using an instance of that Behaviour class.
	 *
	 * @param icon
	 * 		the icon to display
	 * @param behaviourClass
	 * 		the Behaviour class to associate the icon with, null to use the current Behaviour
	 */
	public final void setIcon(final Drawable icon, final Class<? extends IntroButton.Behaviour>
			behaviourClass) {
		button.setIcon(icon, behaviourClass);
	}

	/**
	 * Returns the icon displayed by this IntroButton for a particular Behaviour class. Note that
	 * the icon may not currently be visible.
	 *
	 * @param behaviourClass
	 * 		the Behaviour class to get the associated icon of, null to use the the current Behaviour
	 * @return the icon for the Behaviour class, null if there is none
	 */
	public final Drawable getIcon(final Class<? extends IntroButton.Behaviour> behaviourClass) {
		return button.getIcon(behaviourClass);
	}

	/**
	 * Sets the text color of the accessed IntroButton.
	 *
	 * @param color
	 * 		the text color, as an ARGB hex code
	 */
	public final void setTextColor(final int color) {
		button.setTextColor(color);
	}

	/**
	 * @return the text color of the accessed button, as an ARGB hex code
	 */
	public final int getTextColor() {
		return button.getCurrentTextColor();
	}

	/**
	 * Sets the typeface of the accessed IntroButton.
	 *
	 * @param tf
	 * 		the typeface to use
	 */
	public final void setTypeface(final Typeface tf) {
		button.setTypeface(tf);
	}

	/**
	 * Sets the size of the text displayed in the accessed IntroButton.
	 *
	 * @param textSizeSp
	 * 		the size to use, measured in scaled-pixels
	 */
	public final void setTextSize(final float textSizeSp) {
		button.setTextSize(textSizeSp);
	}

	/**
	 * @return the size of the text currently displayed in the accessed IntroButton, measured in
	 * pixels
	 */
	public final float getTextSize() {
		return button.getTextSize();
	}

	/**
	 * Sets the on-click listener for the accessed IntroButton. This functionality is independent of
	 * the Behaviour.
	 *
	 * @param l
	 * 		the listener to receive the callbacks, null to clear any existing listener
	 */
	public final void setOnClickListener(final View.OnClickListener l) {
		button.setOnClickListener(l);
	}
}