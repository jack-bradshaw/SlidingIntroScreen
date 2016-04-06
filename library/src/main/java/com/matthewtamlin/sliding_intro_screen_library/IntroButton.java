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

package com.matthewtamlin.sliding_intro_screen_library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.matthewtamlin.android_utilities_library.helpers.PermissionsHelper;

import java.util.HashMap;

/**
 * An IntroButton is a Button designed to manipulate an IntroActivity. Each button has a behaviour
 * and an appearance. The behaviour of each button is an implementation of the {@link
 * com.matthewtamlin.sliding_intro_screen_library.IntroButton.Behaviour} interface, and is used to
 * perform some action when the button is pressed. Each button may also have an OnClickListener
 * independent of its behaviour. The appearance determines how the button is displayed, and can be
 * set to any of the predefined appearances in {@link com.matthewtamlin.sliding_intro_screen_library
 * .IntroButton.Appearance}. Each Behaviour <b>class</b> can have different text label and icon
 * resources, and changing the behaviour will automatically load the corresponding resources. The
 * context supplied to the constructor is automatically provided to the Behaviour if the context is
 * an IntroActivity.
 */
public class IntroButton extends Button implements OnClickListener {
	/**
	 * Used to identify this class during debugging.
	 */
	private static final String TAG = "[IntroButton]";

	/**
	 * The Behaviour to use before being explicitly set.
	 */
	private final Behaviour DEFAULT_BEHAVIOUR = new GoToPreviousPage();

	/**
	 * The Appearance to use before being explicitly set.
	 */
	private static final Appearance DEFAULT_APPEARANCE = Appearance.TEXT_ONLY;

	/**
	 * The current Behaviour to execute when clicked.
	 */
	private Behaviour behaviour = DEFAULT_BEHAVIOUR;

	/**
	 * The current Appearance of this IntroButton.
	 */
	private Appearance appearance = DEFAULT_APPEARANCE;

	/**
	 * The text labels to display in this IntroButton when the appearance is set to {@code
	 * Appearance.TEXT_ONLY}, {@code Appearance.ICON_TEXT_LEFT} or {@code Appearance
	 * .ICON_TEXT_RIGHT}. Each Behaviour may have a different label set.
	 */
	private final HashMap<Class<? extends Behaviour>, CharSequence> labels = new HashMap<>();

	/**
	 * The icons to display in this IntroButton when the appearance is set to {@code
	 * Appearance.ICON_ONLY}, {@code Appearance.ICON_TEXT_LEFT} or {@code Appearance
	 * .ICON_TEXT_RIGHT}.
	 */
	private final HashMap<Class<? extends Behaviour>, Drawable> icons = new HashMap<>();

	/**
	 * The IntroActivity to manipulate with this button.
	 */
	private IntroActivity activity;

	/**
	 * External on-click listener to receive on-click events after the event has been handled by the
	 * behaviour.
	 */
	private OnClickListener externalOnClickListener;


	/**
	 * Constructs a new IntroButton instance.
	 *
	 * @param context
	 * 		the context this IntroButton will be operating in
	 */
	public IntroButton(final Context context) {
		super(context);
		init();
	}

	/**
	 * Constructs a new IntroButton instance.
	 *
	 * @param context
	 * 		the context this IntroButton will be operating in
	 * @param attrs
	 * 		configuration attributes
	 */
	public IntroButton(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Constructs a new IntroButton instance.
	 *
	 * @param context
	 * 		the context this IntroButton will be operating in
	 * @param attrs
	 * 		configuration attributes
	 * @param defStyleAttr
	 * 		an attribute in the current theme that contains a reference to a style resource that
	 * 		supplies defaults values for the StyledAttributes, or 0 to not look for defaults
	 */
	public IntroButton(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}


	/**
	 * Initialise this IntroButton independent of the constructor used.
	 */
	private void init() {
		// Deal with on click events internally then pass to the external delegate
		super.setOnClickListener(this);

		initialiseLabelsToDefault();
		initialiseIconsToDefault();

		if (getContext() instanceof IntroActivity) {
			this.activity = (IntroActivity) getContext();
		}

		updateUI();
	}

	/**
	 * Initialises {@code labels} to use the default values stored in the string resources file.
	 */
	private void initialiseLabelsToDefault() {
		labels.put(GoToPreviousPage.class,
				getContext().getString(R.string.introActivity_defaultBackButtonText));

		labels.put(GoToNextPage.class,
				getContext().getString(R.string.introActivity_defaultNextButtonText));

		labels.put(GoToFirstPage.class,
				getContext().getString(R.string.introActivity_defaultFirstButtonText));

		labels.put(GoToLastPage.class,
				getContext().getString(R.string.introActivity_defaultLastButtonText));

		labels.put(ProgressToNextActivity.class,
				getContext().getString(R.string.introActivity_defaultFinalButtonText));
	}

	/**
	 * Initialises {@code icons} to use the default images stored in the drawables resources
	 * folder.
	 */
	private void initialiseIconsToDefault() {
		icons.put(GoToPreviousPage.class, ContextCompat.getDrawable(getContext(), R.drawable
				.introbutton_behaviour_previous));

		icons.put(GoToNextPage.class,
				ContextCompat.getDrawable(getContext(), R.drawable.introbutton_behaviour_next));

		icons.put(GoToFirstPage.class,
				ContextCompat.getDrawable(getContext(), R.drawable.introbutton_behaviour_first));

		icons.put(GoToLastPage.class,
				ContextCompat.getDrawable(getContext(), R.drawable.introbutton_behaviour_last));

		icons.put(ProgressToNextActivity.class,
				ContextCompat.getDrawable(getContext(), R.drawable.introbutton_behaviour_last));
	}

	/**
	 * Updates the UI of this IntroButton to match the current {@code appearance}, {@code label} and
	 * {@code icon} member variables.
	 */
	private void updateUI() {
		final AppearanceManipulator manipulator =
				appearance == null ? null : appearance.getManipulator();

		if (manipulator != null) {
			manipulator.setButton(this);
			manipulator.manipulateAppearance();
		}
	}


	/**
	 * Sets the behaviour of this IntroButton. The behaviour will be executed when the button is
	 * clicked. See {@link com.matthewtamlin .sliding_intro_screen_library.IntroButton.Behaviour}.
	 * This method does not accept null; instead consider passing an instance of {@link
	 * com.matthewtamlin.sliding_intro_screen_library.IntroButton.DoNothing}.
	 *
	 * @param behaviour
	 * 		the Behaviour to use, not null
	 * @throws IllegalArgumentException
	 * 		if {@code behaviour} is null
	 */
	public void setBehaviour(final Behaviour behaviour) {
		if (behaviour == null) {
			throw new IllegalArgumentException("behaviour cannot be null");
		}

		this.behaviour = behaviour;
		updateUI();
	}

	/**
	 * @return the current on-click behaviour of this IntroButton
	 */
	public Behaviour getBehaviour() {
		return behaviour;
	}

	/**
	 * Sets the appearance of this IntroButton. See {@link com.matthewtamlin
	 * .sliding_intro_screen_library.IntroButton.Appearance}.
	 *
	 * @param appearance
	 * 		the predefined appearance to use, not null
	 * @throws IllegalArgumentException
	 * 		if {@code appearance} is null
	 */
	public void setAppearance(final Appearance appearance) {
		if (appearance == null) {
			throw new IllegalArgumentException("appearance cannot be null");
		}

		this.appearance = appearance;
		updateUI();
	}

	/**
	 * @return the current appearance of this IntroButton
	 */
	public Appearance getAppearance() {
		return appearance;
	}

	/**
	 * Sets the text label to be displayed by this IntroButton when its appearance is set to {@code
	 * Appearance.TEXT_ONLY}, {@code Appearance.ICON_TEXT_LEFT} or {@code
	 * Appearance.ICON_TEXT_RIGHT}. A different label can be set for each Behaviour class.
	 *
	 * @param label
	 * 		the text label to display when the current behaviour is an instance of {@code
	 * 		behaviourClass}
	 * @param behaviourClass
	 * 		the Behaviour class to set the label for, or null to use the class of the current
	 * 		Behaviour
	 */
	public void setLabel(final CharSequence label, final Class<? extends Behaviour>
			behaviourClass) {
		final Class<? extends Behaviour> behaviourClassToSet =
				(behaviourClass == null) ? this.behaviour.getClass() :
						behaviourClass;
		labels.put(behaviourClassToSet, label);
		updateUI();
	}

	/**
	 * Returns the text label displayed by this IntroButton for a particular Behaviour class. Note
	 * that the label may not be currently visible.
	 *
	 * @param behaviourClass
	 * 		the Behaviour class to query the label against, or null to use the class of the current
	 * 		behaviour
	 * @return the label displayed when the behaviour is set to an instance of {@code
	 * behaviourClass}
	 */
	public CharSequence getLabel(final Class<? extends Behaviour> behaviourClass) {
		final Class behaviourClassToGet = (behaviourClass == null) ? this.behaviour.getClass() :
				behaviourClass;
		return labels.get(behaviourClassToGet);
	}

	/**
	 * Sets the icon to be displayed by this IntroButton when its appearance is set to {@code
	 * Appearance.ICON_ONLY}, {@code Appearance.TEXT_WITH_LEFT_ICON} or {@code
	 * Appearance.TEXT_WITH_RIGHT_ICON}. A different icon can be set for each Behaviour class.
	 *
	 * @param icon
	 * 		the icon to display when the current behaviour is an instance of {@code behaviourClass}
	 * @param behaviourClass
	 * 		the Behaviour class to set the icon for, or null to use the class of the current behaviour
	 */
	public void setIcon(final Drawable icon, final Class<? extends Behaviour> behaviourClass) {
		final Class<? extends Behaviour> behaviourClassToSet =
				(behaviourClass == null) ? this.behaviour.getClass() :
						behaviourClass;
		icons.put(behaviourClassToSet, icon);
		updateUI();
	}

	/**
	 * Returns the icon displayed on this IntroButton for a particular Behaviour. Note that the icon
	 * may not be currently visible.
	 *
	 * @param behaviourClass
	 * 		the Behaviour to query the icon against, or null to use the class of the current behaviour
	 * @return the icon displayed when the behaviour is set to an instance of {@code behaviourClass}
	 */
	public Drawable getIcon(final Class<? extends Behaviour> behaviourClass) {
		final Class behaviourClassToSet =
				(behaviourClass == null) ? this.behaviour.getClass() : behaviourClass;
		return icons.get(behaviourClassToSet);
	}

	/**
	 * Sets the IntroActivity to be manipulated by this IntroButton.
	 *
	 * @param activity
	 * 		the IntroActivity to manipulate, null to clear any existing activity
	 */
	public void setActivity(final IntroActivity activity) {
		this.activity = activity;
	}

	/**
	 * @return the IntroActivity which is manipulated by this IntroButton, null if none has been set
	 */
	public IntroActivity getActivity() {
		return activity;
	}


	@Override
	public void setTextColor(final int color) {
		super.setTextColor(color);
		updateUI();
	}

	@Override
	public void setTypeface(final Typeface tf, final int style) {
		super.setTypeface(tf, style);
		updateUI();
	}

	@Override
	public void setTypeface(final Typeface tf) {
		super.setTypeface(tf);
		updateUI();
	}

	@Override
	public void onClick(final View v) {
		if (behaviour != null) {
			behaviour.setActivity(activity);
			behaviour.run();
		}

		if (externalOnClickListener != null) {
			externalOnClickListener.onClick(v);
		}
	}

	@Override
	public void setOnClickListener(final OnClickListener l) {
		externalOnClickListener = l;
	}


	/**
	 * The different appearances an IntroButton can have. The appearance of an IntroButton
	 * determines whether or not it should display a label, an icon, or both. In the case of both,
	 * it also determines whether the icon appears to the left or the right of the label.
	 */
	public enum Appearance {
		/**
		 * Display only a text label.
		 */
		TEXT_ONLY(new AppearanceManipulator() {
			@Override
			public void manipulateAppearance() {
				final IntroButton button = getButton();
				button.setText(button.getLabel(null));
				button.setCompoundDrawables(null, null, null, null);
			}
		}),

		/**
		 * Display only an icon.
		 */
		ICON_ONLY(new AppearanceManipulator() {
			@Override
			public void manipulateAppearance() {
				final IntroButton button = getButton();
				button.setText(null);
				button.setCompoundDrawablesWithIntrinsicBounds(button.getIcon(null), null, null,
						null);
			}
		}),

		/**
		 * Display an icon to the left of a text label.
		 */
		TEXT_WITH_LEFT_ICON(new AppearanceManipulator() {
			@Override
			public void manipulateAppearance() {
				final IntroButton button = getButton();
				button.setText(button.getLabel(null));
				button.setCompoundDrawablesWithIntrinsicBounds(button.getIcon(null), null, null,
						null);
			}
		}),

		/**
		 * Display an icon to the right of a text label.
		 */
		TEXT_WITH_RIGHT_ICON(new AppearanceManipulator() {
			@Override
			public void manipulateAppearance() {
				final IntroButton button = getButton();
				button.setText(button.getLabel(null));
				button.setCompoundDrawablesWithIntrinsicBounds(null, null, button.getIcon(null),
						null);
			}
		});

		/**
		 * An AppearanceManipulator which can manipulate an IntroButton as required.
		 */
		private final AppearanceManipulator manipulator;

		/**
		 * Constructs a new Appearance instance.
		 *
		 * @param manipulator
		 * 		an AppearanceManipulator which can manipulate the IntroButton as required
		 */
		Appearance(AppearanceManipulator manipulator) {
			this.manipulator = manipulator;
		}

		/**
		 * Returns the Appearance corresponding to the supplied ordinal.
		 *
		 * @param ordinal
		 * 		the ordinal to use
		 * @return the Appearance corresponding to {@code ordinal}
		 */
		public static Appearance fromOrdinal(int ordinal) {
			return Appearance.values()[ordinal];
		}

		/**
		 * @return an AppearanceManipulator which can manipulate an IntroButton as required
		 */
		public AppearanceManipulator getManipulator() {
			return manipulator;
		}
	}


	/**
	 * Pass concrete instances of this interface to the {@link #setBehaviour(Behaviour)} method of
	 * an IntroButton to define its on click behaviour. This interface has methods for setting and
	 * retrieving an IntroActivity, to allow the {@link #run()} method to manipulate the
	 * IntroActivity hosting the button.
	 */
	public interface Behaviour extends Runnable {
		/**
		 * Sets an activity to be manipulated by {@link #run()}.
		 *
		 * @param activity
		 * 		the activity to be manipulated, null to clear any activity which has been set
		 */
		void setActivity(IntroActivity activity);

		/**
		 * @return the activity to be manipulated, null if none has been set
		 */
		IntroActivity getActivity();

		/**
		 * The operations to perform when the IntroButton is pressed. Implementations must account
		 * for cases where no activity has been set.
		 */
		@Override
		void run();
	}

	/**
	 * A partial implementation of the Behaviour interface, designed to eliminate boilerplate code
	 * in full implementations. This class features a simple getter/setter combo for the target
	 * IntroActivity, so that subclasses simply need to implement {@code run()} and use {@code
	 * getActivity()} to obtain the activity to manipulate.
	 */
	public abstract static class BehaviourAdapter implements Behaviour {
		/**
		 * The activity to manipulate.
		 */
		private IntroActivity activity;

		@Override
		public void setActivity(final IntroActivity activity) {
			this.activity = activity;
		}

		@Override
		public IntroActivity getActivity() {
			return activity;
		}
	}

	/**
	 * A Behaviour which manipulates the provided activity to display the previous page.
	 */
	public static final class GoToPreviousPage extends BehaviourAdapter {
		/**
		 * Returns to the previous page of the supplied IntroActivity, unless the first page is
		 * currently displayed.
		 */
		@Override
		public void run() {
			if (getActivity() != null) {
				getActivity().goToPreviousPage();
			}
		}
	}

	/**
	 * A Behaviour which manipulates the provided activity to display the next page.
	 */
	public static final class GoToNextPage extends BehaviourAdapter {
		/**
		 * Advances to the next page of the supplied activity, unless the last page is currently
		 * displayed.
		 */
		@Override
		public void run() {
			if (getActivity() != null) {
				getActivity().goToNextPage();
			}
		}
	}

	/**
	 * A Behaviour which manipulates the provided activity to display the first page.
	 */
	public static final class GoToFirstPage extends BehaviourAdapter {
		/**
		 * Returns to the first page of the supplied activity, unless the first page is currently
		 * displayed.
		 */
		@Override
		public void run() {
			if (getActivity() != null) {
				getActivity().goToFirstPage();
			}
		}
	}

	/**
	 * A Behaviour which manipulates the provided activity to display the last page.
	 */
	public static final class GoToLastPage extends BehaviourAdapter {
		/**
		 * Advances to the last page of the supplied activity, unless the last page is currently
		 * displayed.
		 */
		@Override
		public void run() {
			if (getActivity() != null) {
				getActivity().goToLastPage();
			}
		}
	}

	/**
	 * A Behaviour designed to launch a new activity. The default constructor has two arguments: an
	 * {@link Intent} and a {@link android.content.SharedPreferences.Editor} shared preThe new
	 * activity is defined by an intent passed to the default constructor. The {@link
	 * android.content.SharedPreferences.Editor}. Any pending changes in this editor will be
	 * committed when the next activity successfully launches. This can be used to set a shared
	 * preferences flag which stops the activity from being displayed to the user again. Subclass
	 * this class and implement {@link #shouldLaunchActivity() } to define validation conditions
	 * which must pass before the activity is launched.
	 */
	public static abstract class ProgressToNextActivity extends BehaviourAdapter {
		/**
		 * An intent which starts the next activity.
		 */
		private final Intent startNextActivity;

		/**
		 * A shared preferences editor with changes pending. The changes are committed when the
		 * activity successfully launches.
		 */
		private final SharedPreferences.Editor editsToMake;

		/**
		 * Constructs a new ProgressToNextActivity instance. Any pending changes in {@code
		 * editsToMake} are committed when the next activity successfully launches.
		 *
		 * @param startNextActivity
		 * 		an intent which starts the next activity
		 * @param editsToMake
		 * 		a shared preferences editor with pending changes, null to ignore this feature
		 */
		public ProgressToNextActivity(Intent startNextActivity, SharedPreferences.Editor
				editsToMake) {
			this.startNextActivity = startNextActivity;
			this.editsToMake = editsToMake;
		}

		/**
		 * Determines whether the next activity should launch. Implement this method to define
		 * validation conditions which must pass before the next activity is launched.
		 *
		 * @return true if the next activity should launch, false otherwise
		 */
		public abstract boolean shouldLaunchActivity();

		@Override
		public final void run() {
			if (getActivity() != null && shouldLaunchActivity()) {
				if (startNextActivity != null) {
					if (editsToMake != null) {
						editsToMake.apply();
					}

					getActivity().startActivity(startNextActivity);
				}
			}
		}
	}

	/**
	 * A Behaviour which does nothing.
	 */
	public static final class DoNothing extends BehaviourAdapter {
		@Override
		public void run() {
			// Do nothing
		}
	}

	/**
	 * A Behaviour which closes the current app.
	 */
	@TargetApi(16)
	public static class CloseApp extends BehaviourAdapter {
		@Override
		public void run() {
			if (getActivity() != null) {
				getActivity().finishAffinity();
			}
		}
	}

	/**
	 * A Behaviour for requesting permissions. Pass the permissions to request to the constructor,
	 * along with a request code which can be used to receive the result. If this behaviour is used,
	 * {@link android.support.v7.app.AppCompatActivity#onRequestPermissionsResult(int, String[],
	 * int[])} should be overridden in the activity passed to {@link #setActivity(IntroActivity)} so
	 * that the request result can be received.
	 */
	@TargetApi(23)
	public static abstract class RequestPermissions extends BehaviourAdapter {
		/**
		 * The permissions to request when this Behaviour is run.
		 */
		private final String[] permissions;

		/**
		 * The request code to use when requesting the permissions.
		 */
		private final int requestCode;

		/**
		 * Constructs a new RequestPermissions instance.
		 *
		 * @param permissions
		 * 		the permissions to request when this Behaviour is run
		 * @param requestCode
		 * 		the request code to use when requesting the permissions
		 */
		public RequestPermissions(String[] permissions, int requestCode) {
			this.permissions = permissions;
			this.requestCode = requestCode;
		}

		/**
		 * The operations to perform when the IntroButton is pressed. Implementations must account
		 * for cases where no activity has been set.
		 */
		@Override
		public void run() {
			if (getActivity() != null) {
				if (!PermissionsHelper.permissionsAreGranted(getActivity(), permissions)) {
					getActivity().requestPermissions(permissions, requestCode);
				}
			}
		}
	}
}

/**
 * Manipulates the appearance of an IntroButton. Subclasses need only implement the {@link
 * #manipulateAppearance()} method and call {@link #getButton()} to get a reference to the button to
 * manipulate.
 */
abstract class AppearanceManipulator {
	/**
	 * The IntroButton to manipulate when {@code run()} is called.
	 */
	private IntroButton button;

	/**
	 * Sets the IntroButton to manipulate, null to clear any button which has been set
	 *
	 * @param button
	 * 		the IntroButton to manipulate
	 */
	public final void setButton(final IntroButton button) {
		this.button = button;
	}

	/**
	 * @return the IntroButton to manipulate, null if none has been set
	 */
	public final IntroButton getButton() {
		return button;
	}

	/**
	 * Manipulates the appearance of the IntroButton supplied to {@link #setButton(IntroButton)}.
	 * Implementations must account for cases where no button has been set.
	 */
	public abstract void manipulateAppearance();
}