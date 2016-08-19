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
import android.support.v7.app.AppCompatActivity;

import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton;

/**
 * This Activity should be displayed after each tests which invokes the {@link
 * IntroButton.ProgressToNextActivity} behaviour. The Activity displays a visual cue to indicate
 * that the Behaviour worked
 */
public class EndActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end);
	}
}