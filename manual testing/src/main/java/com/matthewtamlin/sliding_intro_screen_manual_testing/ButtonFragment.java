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