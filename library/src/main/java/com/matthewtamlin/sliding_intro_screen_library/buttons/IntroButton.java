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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.sliding_intro_screen_library.R;
import com.matthewtamlin.sliding_intro_screen_library.core.IntroActivity;

import java.util.HashMap;

/**
 * An IntroButton is a button designed to manipulate an IntroActivity. Each button has two main
 * components: a {@link Behaviour} and an {@link Appearance}.
 * <p/>
 * The Behaviour is effectively a runnable which can manipulate an IntroActivity. When an
 * IntroButton is pressed, its current Behaviour is executed. By separating the on-click logic from
 * the Button, it is possible to write reusable actions to be used across multiple buttons.
 * IntroButtons also associate individual display elements (text and icons) with classes of
 * Behaviours, so that changing the Behaviour automatically updates the UI of the button. This
 * avoids needing to manually match the text and icon to the Behaviour, which reduces boilerplate
 * code and creates less opportunities for bugs to occur.
 * <p/>
 * The Appearance determines how the button is displayed to the user, with regards to the
 * arrangement of icons and text within the button. The Appearance can only be set to one of the
 * predefined Appearances. Using an Appearance to change the text/icon display arrangement has
 * advantages when compared to changing the text and icons manually. The text/image resources can be
 * loaded into the button once and stored in memory, then displayed when needed via a single method
 * call.
 */
public class IntroButton extends Button {
	/**
	 * Used to identify this class during debugging.
	 */
	@SuppressWarnings("unused")
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
	 * The current Behaviour.
	 */
	private Behaviour behaviour = DEFAULT_BEHAVIOUR;

	/**
	 * The current Appearance.
	 */
	private Appearance appearance = DEFAULT_APPEARANCE;

	/**
	 * The text labels to display in this IntroButton. Each text label is mapped to a class of
	 * Behaviour.
	 */
	private final HashMap<Class<? extends Behaviour>, CharSequence> labels = new HashMap<>();

	/**
	 * The icons to display in this IntroButton. Each icon is mapped to a class of Behaviour.
	 */
	private final HashMap<Class<? extends Behaviour>, Drawable> icons = new HashMap<>();

	/**
	 * The IntroActivity to manipulate with this button.
	 */
	private IntroActivity activity;

	/**
	 * The OnClickListener which has been registered to receive on-click events from this Button.
	 * On-click events are delivered to this listener after they are handled by the Behaviour.
	 */
	private OnClickListener externalOnClickListener;

	/**
	 * Receives on-click events from this Button and runs the Behaviour. After the Behaviour has
	 * been executed, the event is passed to the externalOnClickListener. Using a delegate hides the
	 * internal implementation from the public class signature.
	 */
	private final OnClickListener internalOnClickListenerDelegate = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (behaviour != null) {
				behaviour.setActivity(activity);
				behaviour.run();
			}

			if (externalOnClickListener != null) {
				externalOnClickListener.onClick(v);
			}
		}
	};

	/**
	 * Constructs a new IntroButton instance. The supplied Context is used as the Behaviour target
	 * if the Context is an instance of IntroActivity.
	 *
	 * @param context
	 * 		the context this IntroButton will be operating in, not null
	 */
	public IntroButton(final Context context) {
		super(context);
		init();
	}

	/**
	 * Constructs a new IntroButton instance. The supplied Context is used as the target for the
	 * Behaviour if the Context is an instance of IntroActivity.
	 *
	 * @param context
	 * 		the context this IntroButton will be operating in, not null
	 * @param attrs
	 * 		configuration attributes
	 */
	public IntroButton(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Constructs a new IntroButton instance. The supplied Context is used as the target for the
	 * Behaviour if the Context is an instance of IntroActivity.
	 *
	 * @param context
	 * 		the context this IntroButton will be operating in, not null
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
	 * Performs initialisation of this IntroButton.
	 */
	private void init() {
		// Let the internal delegate deal with on-click events
		super.setOnClickListener(internalOnClickListenerDelegate);

		initialiseLabelsToDefault();
		initialiseIconsToDefault();

		if (getContext() instanceof IntroActivity) {
			this.activity = (IntroActivity) getContext();
		}

		updateUI();
	}

	/**
	 * Initialises {@code labels} by loading default values stored in the string resources file.
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
	 * Initialises {@code icons} by loading the default images stored in the drawable resources
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
	 * Updates the UI of this IntroButton to match the current state.
	 */
	private void updateUI() {
		final AppearanceManipulator manipulator = appearance == null ?
				null :
				appearance.getManipulator();

		if (manipulator != null) {
			manipulator.setButton(this);
			manipulator.manipulateAppearance();
		}
	}

	/**
	 * Sets the Behaviour of this IntroButton. This class contains predefined Behaviours which meet
	 * most needs, but custom implementations of the Behaviour interface are also accepted. The
	 * {@link com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.BehaviourAdapter}
	 * class can be used to reduce boilerplate code when implementing the interface. This method
	 * does not accept null; to do nothing when the button is clicked, pass an instance of {@link
	 * IntroButton.DoNothing}.
	 * <p/>
	 * See {@link Behaviour}.
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
	 * @return the current Behaviour of this IntroButton, not null
	 */
	public Behaviour getBehaviour() {
		return behaviour;
	}

	/**
	 * Sets the Appearance of this IntroButton. The Appearance defines how the button is displayed.
	 * <p/>
	 * See {@link Appearance}.
	 *
	 * @param appearance
	 * 		the predefined Appearance to use, not null
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
	 * @return the current Appearance of this IntroButton, not null
	 */
	public Appearance getAppearance() {
		return appearance;
	}

	/**
	 * Sets the text label to be displayed by this IntroButton. The label will only be displayed
	 * when the Appearance is set to {@code Appearance.TEXT_ONLY}, {@code Appearance.ICON_TEXT_LEFT}
	 * or {@code Appearance.ICON_TEXT_RIGHT}. The label is linked to a Behaviour class, and will
	 * only be shown when this IntroButton is using an instance of that Behaviour class.
	 *
	 * @param label
	 * 		the text label to display
	 * @param behaviourClass
	 * 		the Behaviour class to associate the label with, null to use the current Behaviour
	 */
	public void setLabel(final CharSequence label, final Class<? extends Behaviour>
			behaviourClass) {
		// Use the current Behaviour class if null was supplied
		final Class<? extends Behaviour> behaviourClassToSet = (behaviourClass == null) ?
				behaviour.getClass() :
				behaviourClass;

		labels.put(behaviourClassToSet, label);
		updateUI();
	}

	/**
	 * Returns the text label displayed by this IntroButton for a particular Behaviour class. Note
	 * that the label may not currently be visible.
	 *
	 * @param behaviourClass
	 * 		the Behaviour class to get the associated text of, null to use the the current Behaviour
	 * @return the label for the Behaviour class, null if there is none
	 */
	public CharSequence getLabel(final Class<? extends Behaviour> behaviourClass) {
		// Use the current Behaviour class if null was supplied
		final Class behaviourClassToGet = (behaviourClass == null) ?
				behaviour.getClass() :
				behaviourClass;

		return labels.get(behaviourClassToGet);
	}

	/**
	 * Sets the icon to be displayed by this IntroButton. The icon will only be displayed when its
	 * Appearance is set to {@code Appearance.ICON_ONLY}, {@code Appearance.ICON_TEXT_LEFT} or
	 * {@code Appearance.ICON_TEXT_RIGHT}. The icon is linked to a Behaviour class, and will only be
	 * shown when this IntroButton is using an instance of that Behaviour class.
	 *
	 * @param icon
	 * 		the icon to display
	 * @param behaviourClass
	 * 		the Behaviour class to associate the icon with, null to use the current Behaviour
	 */
	public void setIcon(final Drawable icon, final Class<? extends Behaviour> behaviourClass) {
		// Use the current Behaviour class if null was supplied
		final Class<? extends Behaviour> behaviourClassToSet = (behaviourClass == null) ?
				behaviour.getClass() : behaviourClass;

		icons.put(behaviourClassToSet, icon);
		updateUI();
	}

	/**
	 * Returns the icon displayed by this IntroButton for a particular Behaviour class. Note that
	 * the icon may not currently be visible.
	 *
	 * @param behaviourClass
	 * 		the Behaviour class to get the associated icon of, null to use the the current Behaviour
	 * @return the icon for the Behaviour class, null if there is none
	 */
	public Drawable getIcon(final Class<? extends Behaviour> behaviourClass) {
		// Use the current Behaviour class if null was supplied
		final Class behaviourClassToSet = (behaviourClass == null) ?
				behaviour.getClass() :
				behaviourClass;

		return icons.get(behaviourClassToSet);
	}

	/**
	 * Sets the IntroActivity to be manipulated by this IntroButton. The supplied activity will
	 * replace any previously supplied activity. To clear the activity, supply null.
	 *
	 * @param activity
	 * 		the IntroActivity to manipulate, null allowed
	 */
	public void setActivity(final IntroActivity activity) {
		this.activity = activity;
	}

	/**
	 * @return the IntroActivity which is currently manipulated by this IntroButton, null if none
	 * has been set
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
	public void setOnClickListener(final OnClickListener l) {
		// Intercept the listener and save it as member variable. On-click events will be passed
		// to the external listener after they have been handled by the internal delegate.
		externalOnClickListener = l;
	}

	/**
	 * All the different appearances an IntroButton can take. The Appearance of an IntroButton
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
				final CharSequence text = button.getLabel(null); // Use the current Behaviour

				button.setText(text);
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
				final Drawable icon = button.getIcon(null); // Use the current Behaviour

				button.setText(null);
				button.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
			}
		}),

		/**
		 * Display an icon to the left of a text label.
		 */
		TEXT_WITH_LEFT_ICON(new AppearanceManipulator() {
			@Override
			public void manipulateAppearance() {
				final IntroButton button = getButton();
				final Drawable icon = button.getIcon(null); // Use the current Behaviour

				button.setText(button.getLabel(null));
				button.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
			}
		}),

		/**
		 * Display an icon to the right of a text label.
		 */
		TEXT_WITH_RIGHT_ICON(new AppearanceManipulator() {
			@Override
			public void manipulateAppearance() {
				final IntroButton button = getButton();

				// Use the current Behaviour
				final CharSequence text = button.getLabel(null);
				final Drawable icon = button.getIcon(null);

				button.setText(text);
				button.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
			}
		});

		/**
		 * An AppearanceManipulator which makes the target IntroButton reflect this Appearance.
		 */
		private final AppearanceManipulator manipulator;

		/**
		 * Constructs a new Appearance instance.
		 *
		 * @param manipulator
		 * 		an AppearanceManipulator which makes the target IntroButton reflect this
		 * 		Appearance,	not null
		 */
		Appearance(final AppearanceManipulator manipulator) {
			this.manipulator = manipulator;
		}

		/**
		 * Returns an AppearanceManipulator which makes the target IntroButton reflect this
		 * Appearance.
		 *
		 * @return the AppearanceManipulator, not null
		 */
		public AppearanceManipulator getManipulator() {
			return manipulator;
		}
	}

	/**
	 * A Runnable which can manipulate an IntroButton. Pass concrete instances of this interface to
	 * the {@link #setBehaviour(Behaviour)} method of an IntroButton to define its on-click
	 * behaviour.
	 */
	public interface Behaviour extends Runnable {
		/**
		 * Sets the activity to be manipulated by this Behaviour.
		 *
		 * @param activity
		 * 		the target activity, null to clear any existing target
		 */
		void setActivity(IntroActivity activity);

		/**
		 * @return the target activity of this Behaviour, null if none has been set
		 */
		IntroActivity getActivity();

		/**
		 * Starts executing this Behaviour. The IntroActivity supplied to {@link
		 * #setActivity(IntroActivity)} is manipulated in some way.
		 */
		@Override
		void run();
	}

	/**
	 * A partial implementation of the Behaviour interface, designed to eliminate boilerplate code
	 * in full implementations. This class features a simple getter/setter combination for the
	 * target IntroActivity, so that subclasses simply need to implement {@code run()} and use
	 * {@code getActivity()}. Subclasses should always perform a null check on the result of {@code
	 * getActivity()}.
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
	 * A Behaviour which displays the previous page of the target activity. No action is taken if
	 * the first page is currently displayed.
	 */
	public static final class GoToPreviousPage extends BehaviourAdapter {
		@Override
		public final void run() {
			if (getActivity() != null) {
				getActivity().goToPreviousPage();
			}
		}
	}

	/**
	 * A Behaviour which displays the next page of the target IntroActivity. No action is taken if
	 * the last page is currently displayed.
	 */
	public static final class GoToNextPage extends BehaviourAdapter {
		@Override
		public final void run() {
			if (getActivity() != null) {
				getActivity().goToNextPage();
			}
		}
	}

	/**
	 * A Behaviour which displays the first page of the target IntroActivity. No action is taken if
	 * the first page is currently displayed.
	 */
	public static final class GoToFirstPage extends BehaviourAdapter {
		@Override
		public final void run() {
			if (getActivity() != null) {
				getActivity().goToFirstPage();
			}
		}
	}

	/**
	 * A Behaviour which displays the last page of the target IntroActivity. No action is taken if
	 * the last page is currently displayed.
	 */
	public static final class GoToLastPage extends BehaviourAdapter {
		@Override
		public final void run() {
			if (getActivity() != null) {
				getActivity().goToLastPage();
			}
		}
	}

	/**
	 * A Behaviour designed to launch a new activity and record the completion of an IntroActivity.
	 * The default constructor accepts a {@link android.content.SharedPreferences.Editor}. Any
	 * pending changes in the editor are committed when the next activity is successfully launched,
	 * which allows a shared preferences flag to be set. By checking the status of this flag each
	 * time the IntroActivity is launched, the IntroActivity can be skipped if it was previously
	 * completed.
	 * <p/>
	 * To define validation conditions which must pass before the next activity is launched,
	 * subclass this class and override {@link #shouldLaunchActivity()}.
	 */
	public static class ProgressToNextActivity extends BehaviourAdapter {
		/**
		 * The intent to start the next activity.
		 */
		private final Intent startNextActivity;

		/**
		 * A shared preferences editor with pending changes. The changes are committed when the next
		 * activity is successfully launched.
		 */
		private final SharedPreferences.Editor editsToMake;

		/**
		 * Constructs a new ProgressToNextActivity instance. Any pending changes in {@code
		 * editsToMake} are committed when the next activity successfully launches.
		 *
		 * @param startNextActivity
		 * 		an intent which starts the next activity, not null
		 * @param editsToMake
		 * 		a shared preferences editor with pending changes, null allowed
		 * @throws IllegalArgumentException
		 * 		if {@code startNextActivity} is null
		 */
		public ProgressToNextActivity(final Intent startNextActivity, final SharedPreferences.Editor
				editsToMake) {
			if (startNextActivity == null) {
				throw new IllegalArgumentException("startNextActivity cannot be null");
			}

			this.startNextActivity = startNextActivity;
			this.editsToMake = editsToMake;
		}

		/**
		 * Contains validation logic which determines whether or not the next activity should
		 * launch. By default, this class always returns true. Override this method to define
		 * validation conditions which must pass before the next activity is launched.
		 *
		 * @return true if the next activity should launch, false otherwise
		 */
		public boolean shouldLaunchActivity() {
			return true;
		}

		@Override
		public final void run() {
			if (getActivity() != null && shouldLaunchActivity()) {
				if (editsToMake != null) {
					editsToMake.apply();
				}

				getActivity().startActivity(startNextActivity);
			}
		}
	}

	/**
	 * A Behaviour which does nothing.
	 */
	public static final class DoNothing extends BehaviourAdapter {
		@Override
		public final void run() {
			// Do nothing
		}
	}

	/**
	 * A Behaviour which closes the current app.
	 */
	@TargetApi(16)
	public static final class CloseApp extends BehaviourAdapter {
		@Override
		public final void run() {
			if (getActivity() != null) {
				getActivity().finishAffinity();
			}
		}
	}

	/**
	 * A Behaviour for requesting permissions. Pass the desired permissions to the constructor,
	 * along with a request code which can be used to receive the result. If this Behaviour is used,
	 * then {@link android.support.v7.app.AppCompatActivity#onRequestPermissionsResult(int,
	 * String[], int[])} should be overridden in the target activity so that the result of the
	 * request can be received.
	 */
	@TargetApi(23)
	public static final class RequestPermissions extends BehaviourAdapter {
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
		 * 		the permissions to request when this Behaviour is run, null allowed
		 * @param requestCode
		 * 		the request code for receiving the result of the permission request
		 */
		public RequestPermissions(final String[] permissions, final int requestCode) {
			this.permissions = permissions;
			this.requestCode = requestCode;
		}

		/**
		 * The operations to perform when the IntroButton is pressed. Implementations must account
		 * for cases where no activity has been set.
		 */
		@Override
		public final void run() {
			if (getActivity() != null) {
				getActivity().requestPermissions(permissions, requestCode);
			}
		}
	}
}

/**
 * Manipulates the appearance of an IntroButton. Subclasses need only implement the {@link
 * #manipulateAppearance()} method and call the {@link #getButton()} method to get a reference to
 * the IntroButton to manipulate.
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