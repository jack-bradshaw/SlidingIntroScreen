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

package com.matthewtamlin.sliding_intro_screen_library.pages;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtamlin.sliding_intro_screen_library.R;
import com.matthewtamlin.sliding_intro_screen_library.core.IntroActivity;
import com.matthewtamlin.sliding_intro_screen_library.transformers.MultiViewParallaxTransformer;

/**
 * An Fragment with three visual elements: a front image, a back image and text. The Views are drawn
 * such that the back image is drawn behind the front image, and the text is drawn on top of both.
 * The images are positioned in the centre-top of the layout, and the text is positioned at the
 * exact centre. This class can be used in a {@link IntroActivity} with a {@link
 * MultiViewParallaxTransformer} to create a parallax scrolling effect between the images.
 */
public class ParallaxPage extends Fragment {
	/**
	 * Used to identify this class during debugging.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "[ParallaxPage]";

	/**
	 * The root view of this Fragment.
	 */
	private FrameLayout rootView;

	/**
	 * The View which displays the front image.
	 */
	private ImageView frontImageHolder;

	/**
	 * The View which displays the back image.
	 */
	private ImageView backImageHolder;

	/**
	 * The View which displays the text.
	 */
	private TextView textHolder;

	/**
	 * The current front image.
	 */
	private Bitmap frontImage = null;

	/**
	 * The current back image.
	 */
	private Bitmap backImage = null;

	/**
	 * The current text.
	 */
	private CharSequence text = null;

	/**
	 * @return a new ParallaxPage instance
	 */
	public static ParallaxPage newInstance() {
		return new ParallaxPage();
	}

	/**
	 * Constructs a new ParallaxPage instance. This is an empty public constructor, as required by
	 * the Android fragment framework.
	 */
	public ParallaxPage() {
		super();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {
		rootView =
				(FrameLayout) inflater.inflate(R.layout.fragment_parallax_page, container, false);
		frontImageHolder = (ImageView) rootView.findViewById(R.id.page_fragment_imageHolderFront);
		backImageHolder = (ImageView) rootView.findViewById(R.id.page_fragment_imageHolderBack);
		textHolder = (TextView) rootView.findViewById(R.id.page_fragment_textHolder);

		reflectParametersInView();

		return rootView;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	/**
	 * Updates the UI of this ParallaxPage to reflect the current member variables
	 */
	private void reflectParametersInView() {
		if (frontImageHolder != null) {
			// The image will not update unless it is first reset by supplying null
			frontImageHolder.setImageBitmap(null);
			frontImageHolder.setImageBitmap(frontImage);
		}

		if (backImageHolder != null) {
			// The image will not update unless it is first reset by supplying null
			backImageHolder.setImageBitmap(null);
			backImageHolder.setImageBitmap(backImage);
		}

		if (textHolder != null) {
			// The text will not update unless it is first reset by supplying null
			textHolder.setText(null);
			textHolder.setText(text);
		}
	}

	/**
	 * Sets the front image of this ParallaxPage.
	 *
	 * @param frontImage
	 * 		the image to display, null to display none
	 */
	public void setFrontImage(final Bitmap frontImage) {
		this.frontImage = frontImage;
		reflectParametersInView();
	}

	/**
	 * @return the current front image, null if there is none
	 */
	public Bitmap getFrontImage() {
		return frontImage;
	}

	/**
	 * @return the View which holds the front image, null if the View has not yet been created
	 */
	public View getFrontImageHolder() {
		return frontImageHolder;
	}

	/**
	 * Sets the back image of this ParallaxPage.
	 *
	 * @param backImage
	 * 		the image to display, null to display none
	 */
	public void setBackImage(final Bitmap backImage) {
		this.backImage = backImage;
		reflectParametersInView();
	}

	/**
	 * @return the current back image, null if there is none
	 */
	public Bitmap getBackImage() {
		return backImage;
	}

	/**
	 * @return the View which holds the back image, null if the View has not yet been created
	 */
	public View getBackImageHolder() {
		return backImageHolder;
	}

	/**
	 * Sets the text of this ParallaxPage.
	 *
	 * @param text
	 * 		the text to display, null to display none
	 */
	public void setText(final CharSequence text) {
		this.text = text;
		reflectParametersInView();
	}

	/**
	 * @return the current text, null if there is none
	 */
	public CharSequence getText() {
		return text;
	}

	/**
	 * @return the View which holds the text, null if the View has not yet been created
	 */
	public View getTextHolder() {
		return textHolder;
	}
}