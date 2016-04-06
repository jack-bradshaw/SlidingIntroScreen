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

import com.matthewtamlin.sliding_intro_screen_library.ParallaxTransformer;

/**
 * Test the activity when a parallax page transformer is set. The activity should show three
 * parallax pages, each with front and back images. When scrolled, the front image should scroll
 * faster than the other page elements.
 */
public class TestTransformer extends ThreePageTestBase {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setPageTransformer(false, new ParallaxTransformer());
	}
}