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
		final Button button = createButton();
		final LinearLayout layout = createLayout();
		layout.addView(button);
		return layout;
	}

	/**
	 * @return a Button which is centred in its parent and provides visual feedback when clicked
	 */
	private Button createButton() {
		final Button button = new Button(getContext());
		button.setText("Button inside fragment");
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				button.setText("Button click successful...");
			}
		});

		final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup
				.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		button.setLayoutParams(layoutParams);

		return button;
	}

	/**
	 * @return a LinearLayout which fills its parent
	 */
	private LinearLayout createLayout() {
		final LinearLayout layout = new LinearLayout(getContext());
		layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		return layout;
	}
}