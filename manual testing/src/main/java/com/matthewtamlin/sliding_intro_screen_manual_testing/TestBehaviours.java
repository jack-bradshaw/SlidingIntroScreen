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

package com.matthewtamlin.sliding_intro_screen_manual_testing;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.Behaviour;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton.ProgressToNextActivity;
import com.matthewtamlin.sliding_intro_screen_library.indicators.SelectionIndicator;

/**
 * Tests the Behaviours.
 */
public class TestBehaviours extends ThreePageTestBase {
	/**
	 * Used to identify this class during debugging.
	 */
	private static final String TAG = "[TestBehaviours]";

	/**
	 * The permissions to request from the user.
	 */
	private static final String[] PERMISSIONS =
			{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.PROCESS_OUTGOING_CALLS};

	/**
	 * Identifies the permissions request when the result is received.
	 */
	private static final int PERM_REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// The left button is used as the test trigger, so make sure it is always available
		disableLeftButtonOnLastPage(false);
		disableLeftButton(false);

		// Create a layout to display the control buttons over the ViewPager
		final LinearLayout controlButtonHolder = new LinearLayout(this);
		controlButtonHolder.setOrientation(LinearLayout.VERTICAL);
		getRootView().addView(controlButtonHolder);

		// Add the test buttons to the control layout
		controlButtonHolder.addView(createGoToPreviousPageButton());
		controlButtonHolder.addView(createGoToNextPageButton());
		controlButtonHolder.addView(createGoToFirstPageButton());
		controlButtonHolder.addView(createGoToLastPageButton());
		controlButtonHolder.addView(createProgressToNextActivityButton());
		controlButtonHolder.addView(createDoNothingButton());
		controlButtonHolder.addView(createCloseAppButton());
		controlButtonHolder.addView(createRequestPermissionButton());
		controlButtonHolder.addView(createCustomBehaviourButton());
	}

	/**
	 * @return a Button which sets the left button to use the GoToPreviousPage behaviour
	 */
	private Button createGoToPreviousPageButton() {
		final Button button = new Button(this);
		button.setText("Test go to previous page behaviour");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getLeftButtonAccessor().setBehaviour(new IntroButton.GoToPreviousPage());
				getLeftButtonAccessor().setText("Prev page", null);
			}
		});

		return button;
	}

	/**
	 * @return a Button which sets the left button to use the GoToNextPage behaviour
	 */
	private Button createGoToNextPageButton() {
		final Button button = new Button(this);
		button.setText("Test go to next page behaviour");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getLeftButtonAccessor().setBehaviour(new IntroButton.GoToNextPage());
				getLeftButtonAccessor().setText("Next page", null);
			}
		});

		return button;
	}

	/**
	 * @return a Button which sets the left button to use the GoToFirstPage behaviour
	 */
	private Button createGoToFirstPageButton() {
		final Button button = new Button(this);
		button.setText("Test go to first page behaviour");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getLeftButtonAccessor().setBehaviour(new IntroButton.GoToFirstPage());
				getLeftButtonAccessor().setText("First page", null);
			}
		});

		return button;
	}

	/**
	 * @return a Button which sets the left button to use the GoToLastPage behaviour
	 */
	private Button createGoToLastPageButton() {
		final Button button = new Button(this);
		button.setText("Test go to last page behaviour");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getLeftButtonAccessor().setBehaviour(new IntroButton.GoToLastPage());
				getLeftButtonAccessor().setText("Last page", null);
			}
		});

		return button;
	}

	/**
	 * @return a Button which sets the left button to use the ProgressToNextActivity behaviour
	 */
	private Button createProgressToNextActivityButton() {
		final Button button = new Button(this);
		button.setText("Test progress to next activity");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent nextActivity = new Intent(TestBehaviours.this, EndActivity.class);
				final Behaviour behaviour = new ProgressToNextActivity(nextActivity, null);
				getLeftButtonAccessor().setBehaviour(behaviour);
				getLeftButtonAccessor().setText("Next activity", null);
			}
		});

		return button;
	}

	/**
	 * @return a Button which sets the left button to use the DoNothing behaviour
	 */
	private Button createDoNothingButton() {
		final Button button = new Button(this);
		button.setText("Test do nothing");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getLeftButtonAccessor().setBehaviour(new IntroButton.DoNothing());
				getLeftButtonAccessor().setText("Do nothing", null);
			}
		});

		return button;
	}

	/**
	 * @return a Button which sets the left button to use the CloseApp behaviour
	 */
	private Button createCloseAppButton() {
		final Button button = new Button(this);
		button.setText("Test close app");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getLeftButtonAccessor().setBehaviour(new IntroButton.CloseApp());
				getLeftButtonAccessor().setText("Close app", null);
			}
		});

		return button;
	}

	/**
	 * @return a Button which sets the left button to use the RequestPermission behaviour
	 */
	private Button createRequestPermissionButton() {
		final Button button = new Button(this);
		button.setText("Test request permission behaviour");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getLeftButtonAccessor().setBehaviour(
						new IntroButton.RequestPermissions(PERMISSIONS, PERM_REQUEST_CODE));
				getLeftButtonAccessor().setText("Grant perms", null);
			}
		});

		return button;
	}

	/**
	 * @return a Button which sets the left button to use a custom behaviour which toggles the
	 * visibility of the SelectionIndicator
	 */
	private Button createCustomBehaviourButton() {
		final Button button = new Button(this);
		button.setText("Test custom behaviour");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getLeftButtonAccessor().setBehaviour(new IntroButton.BehaviourAdapter() {
					@Override
					public void run() {
						final SelectionIndicator indicator = getActivity().getProgressIndicator();
						indicator.setVisibility(!indicator.isVisible()); // Toggle current state
					}
				});

				getLeftButtonAccessor().setText("Toggle indicator", null);
			}
		});

		return button;
	}
}