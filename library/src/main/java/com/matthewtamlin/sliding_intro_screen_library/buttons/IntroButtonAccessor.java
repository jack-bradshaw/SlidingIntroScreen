package com.matthewtamlin.sliding_intro_screen_library.buttons;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;


/**
 * Provides indirect access to an IntroButton. Each IntroButtonAccessor provides access to a single
 * IntroButton, and the same IntroButton will be accessed for the duration of the accessor's life.
 * This class is immutable but not necessarily thread-safe, as the methods may interact with
 * non-thread safe objects.
 * <p/>
 * See {@link IntroButton} for further details.
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
	 * 		the IntroButton to provide access to
	 */
	public IntroButtonAccessor(IntroButton button) {
		this.button = button;
	}

	/**
	 * Sets the Behaviour of the IntroButton accessed by this accessor. This is distinct from the
	 * on-click listener, which can be set using {@link #setOnClickListener(View.OnClickListener)}.
	 * The IntroButton class contains predefined Behaviours which meet most needs, but custom
	 * implementations of the Behaviour interface are also accepted. The {@link
	 * com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.BehaviourAdapter} class
	 * can be used to reduce boilerplate code when implementing the Behaviour interface.
	 * <p/>
	 * Null is not accepted by this method. To do nothing when the button is pressed, pass an
	 * instance of {@link com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.DoNothing}.
	 *
	 * @param behaviour
	 * 		the Behaviour to use when the accessed IntroButton is pressed, not null
	 * @throws IllegalArgumentException
	 * 		if {@code behaviour} is null
	 */
	public final void setBehaviour(final IntroButton.Behaviour behaviour) {
		button.setBehaviour(behaviour);
	}

	/**
	 * @return the current Behaviour of the IntroButton accessed by this accessor, not null
	 */
	public final IntroButton.Behaviour getBehaviour() {
		return button.getBehaviour();
	}

	/**
	 * Sets the Appearance of the IntroButton accessed by this accessor. The Appearance defines how
	 * the button is displayed. See {@link com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Appearance}.
	 *
	 * @param appearance
	 * 		the predefined Appearance to use, not null
	 * @throws IllegalArgumentException
	 * 		if {@code appearance} is null
	 */
	public final void setAppearance(final IntroButton.Appearance appearance) {
		button.setAppearance(appearance);
	}

	/**
	 * @return the current Appearance of the IntroButton accessed by this accessor, not null
	 */
	public final IntroButton.Appearance getAppearance() {
		return button.getAppearance();
	}

	/**
	 * Sets the text to display in the IntroButton accessed by this accessor. The text is linked to
	 * a Behaviour class, and will only be shown when the IntroButton is set to use a Behaviour of
	 * that class. The current Appearance of the button determines whether or not the text will
	 * actually be displayed.
	 *
	 * @param text
	 * 		the text to display in the button, not null
	 * @param behaviourClass
	 * 		the Behaviour class to associate the text with, null to use the class of the current
	 * 		Behaviour
	 */
	public final void setText(final CharSequence text, final Class<? extends IntroButton
			.Behaviour> behaviourClass) {
		button.setLabel(text, behaviourClass);
	}

	/**
	 * Returns the text to display in the IntroButton accessed by this accessor. The text which is
	 * associated with the supplied Behaviour is returned.
	 *
	 * @param behaviourClass
	 * 		the Behaviour class of which to get the associated text, null to use the class of the
	 * 		current Behaviour
	 * @return the text associated with {@code behaviourClass}, null if there is none
	 */
	public final CharSequence getText(final Class<? extends IntroButton.Behaviour> behaviourClass) {
		return button.getLabel(behaviourClass);
	}

	/**
	 * Sets the icon to display in the IntroButton accessed by this accessor. The icon is linked
	 * to a Behaviour class, and will only be shown when the IntroButton is set to use that
	 * Behaviour. The current appearance of the button determines whether or not the icon will
	 * actually be displayed.
	 *
	 * @param icon
	 * 		the icon to display in the button, no null
	 * @param behaviourClass
	 * 		the behaviour class to associate the text with, null to use the class of the current
	 * 		Behaviour
	 */
	public final void setIcon(final Drawable icon,
			final Class<? extends IntroButton.Behaviour> behaviourClass) {
		button.setIcon(icon, behaviourClass);
	}

	/**
	 * Returns the icon to display in the IntroButton accessed by this accessor. The icon which
	 * is associated with the supplied Behaviour is returned.
	 *
	 * @param behaviourClass
	 * 		the Behaviour class of which to get the associated icon, null to use the class of the
	 * 		current Behaviour
	 * @return the icon associated with {@code behaviourClass}, null if there is none
	 */
	public final Drawable getIcon(final Class<? extends IntroButton.Behaviour> behaviourClass) {
		return button.getIcon(behaviourClass);
	}

	/**
	 * Sets the color of the text in the IntroButton accessed by this accessor. The color is common
	 * to all Behaviours.
	 *
	 * @param color
	 * 		the color to use, as an ARGB hex code
	 */
	public final void setTextColor(final int color) {
		button.setTextColor(color);
	}

	/**
	 * Returns the color of the text in the IntroButton accessed by this accessor. The color is
	 * common to all Behaviours.
	 *
	 * @return the text color, as an ARGB hex code
	 */
	public final int getTextColor() {
		return button.getCurrentTextColor();
	}

	/**
	 * Sets the size of the text in the IntroButton accessed by this accessor. The size is common to
	 * all Behaviours.
	 *
	 * @param sizeSp
	 * 		the text size to use, measured in scaled-pixels
	 */
	public final void setTextSize(final float sizeSp) {
		button.setTextSize(sizeSp);
	}

	/**
	 * Returns the size of the text in the IntroButton accessed by this accessor. The size is common
	 * to all Behaviours.
	 *
	 * @return the current text size, measured in pixels
	 */
	public final float getTextSize() {
		return button.getTextSize();
	}

	/**
	 * Sets the typeface and style in which the text of the left button should be displayed.
	 *
	 * @param tf
	 * 		the typeface to use
	 * @param style
	 * 		style for the typeface
	 */
	public final void setTypeface(final Typeface tf, final int style) {
		button.setTypeface(tf, style);
	}

	/**
	 * Sets the typeface and style for the text of the IntroButton accessed by this accessor.
	 *
	 * @param tf
	 * 		the typeface to use
	 */
	public final void setTypeface(final Typeface tf) {
		button.setTypeface(tf);
	}

	/**
	 * Register a callback to be invoked when the IntroButton accessed by this accessor is clicked.
	 * Registering for on-click events does not prevent the IntroButton from performing its
	 * predefined Behaviour.
	 *
	 * @param l
	 * 		the callback that will run, null to clear the existing listener
	 */
	public final void setOnClickListener(final View.OnClickListener l) {
		button.setOnClickListener(l);
	}
}