package com.matthewtamlin.testapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A Fragment which displays a single button in its centre.
 */
public class ButtonFragment extends Fragment {
	/**
	 * Constructs a new ButtonFragment.
	 */
	public ButtonFragment() {}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		// Create the button and set its layout params
		final Button button = new Button(getContext());
		button.setText("Button inside fragment");
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				button.setText("Button click successful...");
			}
		});
		final LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup
				.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		buttonLayoutParams.gravity = Gravity.CENTER;
		button.setLayoutParams(buttonLayoutParams);

		// Create the root view and set its layout params
		final LinearLayout layout = new LinearLayout(getContext());
		layout.addView(button);
		final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup
				.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(layoutParams);

		return layout;
	}
}
