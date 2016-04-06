package com.matthewtamlin.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.matthewtamlin.sliding_intro_screen_library.IntroButton;

/**
 * Tests the ability to set behaviours.
 */
public class TestBehaviours extends ThreePageTestBase {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		getRootView().addView(layout);

		Button testCustomBehaviour = new Button(this);
		layout.addView(testCustomBehaviour);
		testCustomBehaviour.setText("Test custom behaviour");
		testCustomBehaviour.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setLeftButtonBehaviour(new IntroButton.BehaviourAdapter() {
					@Override
					public void run() {
						getActivity().goToPage(3);
					}
				});

				setLeftButtonText("Go back to 0 ", null);
				hideLeftButtonOnLastPage(false);
			}
		});
	}
}
