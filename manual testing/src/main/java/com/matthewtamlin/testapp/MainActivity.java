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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}


	public void testButtonConfig(View v) {
		Intent i = new Intent(this, TestButtonConfig.class);
		startActivity(i);
	}

	public void testMultiplePageBehaviour(View v) {
		Intent i = new Intent(this, TestMultiplePageBehaviour.class);
		startActivity(i);
	}

	public void testSinglePageBehaviour(View v) {
		Intent i = new Intent(this, TestSinglePageBehaviour.class);
		startActivity(i);
	}

	public void testProgressIndicatorConfig(View v) {
		Intent i = new Intent(this, TestProgressIndicatorConfig.class);
		startActivity(i);
	}

	public void testProgrammaticallyChangePage(View v) {
		Intent i = new Intent(this, TestPageLock.class);
		startActivity(i);
	}

	public void testTransformer(View v) {
		Intent i = new Intent(this, TestTransformer.class);
		startActivity(i);
	}

	public void testHideStatusBar(View v) {
		Intent i = new Intent(this, TestHideStatusBar.class);
		startActivity(i);
	}

	public void testBehaviours(View v) {
		Intent i = new Intent(this, TestBehaviours.class);
		startActivity(i);
	}
}