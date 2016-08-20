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

package com.matthewtamlin.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.matthewtamlin.sliding_intro_screen_library.transformers.MultiViewParallaxTransformer;

/**
 * Tests to verify that parallax effects can be added to and removed from any View using the
 * MultiViewParallaxTransformer class.
 */
public class TestTransformer extends ThreePageTestBase {
	/**
	 * The transformer which applies the parallax effect.
	 */
	private MultiViewParallaxTransformer transformer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a parallax transformer and assign it as the IntroActivity transformer
		transformer = new MultiViewParallaxTransformer();
		setPageTransformer(false, transformer);

		// Create a layout to display the control buttons over the ViewPager
		final LinearLayout controlButtonHolder = new LinearLayout(this);
		controlButtonHolder.setOrientation(LinearLayout.VERTICAL);
		getRootView().addView(controlButtonHolder);

		// Add the test buttons to the control layout
		controlButtonHolder.addView(createAddFrontParallaxEffectButton());
		controlButtonHolder.addView(createAddBackParallaxEffectButton());
		controlButtonHolder.addView(createRemoveFrontParallaxEffectButton());
		controlButtonHolder.addView(createRemoveBackParallaxEffectButton());
	}

	/**
	 * @return a Button which sets a parallax effect on the front image
	 */
	private Button createAddFrontParallaxEffectButton() {
		final Button button = new Button(this);
		button.setText("Add front image parallax effect");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				transformer.withParallaxView(R.id.page_fragment_imageHolderFront, 1.2f);
			}
		});

		return button;
	}

	/**
	 * @return a Button which sets a parallax effect on the back image
	 */
	private Button createAddBackParallaxEffectButton() {
		final Button button = new Button(this);
		button.setText("Add back image parallax effect");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				transformer.withParallaxView(R.id.page_fragment_imageHolderBack, 1.5f);
			}
		});

		return button;
	}

	/**
	 * @return a Button which removes any parallax effect on the front image
	 */
	private Button createRemoveFrontParallaxEffectButton() {
		final Button button = new Button(this);
		button.setText("Remove front image parallax effect");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				transformer.withoutParallaxView(R.id.page_fragment_imageHolderFront);
			}
		});

		return button;
	}

	/**
	 * @return a Button which removes any parallax effect on the back image
	 */
	private Button createRemoveBackParallaxEffectButton() {
		final Button button = new Button(this);
		button.setText("Remove back image parallax effect");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				transformer.withoutParallaxView(R.id.page_fragment_imageHolderBack);
			}
		});

		return button;
	}
}