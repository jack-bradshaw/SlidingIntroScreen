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
 * Test the activity when a parallax page transformer is set. The activity should show three
 * parallax pages, each with front and back images. When scrolled, the front image should scroll
 * faster than the other page elements.
 */
public class TestTransformer extends ThreePageTestBase {
	private final MultiViewParallaxTransformer transformer = new MultiViewParallaxTransformer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setPageTransformer(false, transformer);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		getRootView().addView(layout);

		Button addFrontImage = new Button(this);
		layout.addView(addFrontImage);
		addFrontImage.setText("Add front image parallax");
		addFrontImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				transformer.withParallaxView(R.id.page_fragment_imageHolderFront, 1.2f);
			}
		});

		Button addBackImage = new Button(this);
		layout.addView(addBackImage);
		addBackImage.setText("Add back image parallax");
		addBackImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				transformer.withParallaxView(R.id.page_fragment_imageHolderBack, 1f);
			}
		});

		Button removeFrontImage = new Button(this);
		layout.addView(removeFrontImage);
		removeFrontImage.setText("Remove front image parallax");
		removeFrontImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				transformer.withoutParallaxView(R.id.page_fragment_imageHolderFront);
			}
		});

		Button removeBackImage = new Button(this);
		layout.addView(removeBackImage);
		removeBackImage.setText("Remove back image parallax");
		removeBackImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				transformer.withoutParallaxView(R.id.page_fragment_imageHolderBack);
			}
		});
	}
}