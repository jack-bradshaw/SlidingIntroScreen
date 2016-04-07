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

package com.matthewtamlin.sliding_intro_screen_library;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An {@link Page} with three elements: a front image, a back image and text. The front and back
 * images are centred at the top the Page, such that the front image is drawn on top of the back
 * image. The text is drawn over both images. This class can be used in an {@link IntroActivity}
 * with a {@link ParallaxTransformer} to create a parallax scrolling effect, where the images
 * transition at different speeds. To allow the use of custom transformers, the resource ids of the
 * views can be accessed using the {@link #getFrontImageHolderResId()}, {@link
 * #getBackImageHolderResId()} and {@link #getTextHolderResId()} methods.
 */
public class ParallaxPage extends Page {
	/**
	 * Used to identify this class during debugging.
	 */
	private static final String TAG = "[ParallaxPage]";

	/**
	 * The root view of this Fragment.
	 */
	protected FrameLayout rootView;

	/**
	 * The View which displays the front image.
	 */
	protected ImageView frontImageHolder;

	/**
	 * The View which displays the back image.
	 */
	protected ImageView backImageHolder;

	/**
	 * The View which displays the text.
	 */
	protected TextView textHolder;

	/**
	 * The current front image.
	 */
	protected Bitmap frontImage = null;

	/**
	 * The current back image.
	 */
	protected Bitmap backImage = null;

	/**
	 * The current text.
	 */
	protected CharSequence text = null;

	/**
	 * Required default empty constructor.
	 */
	public ParallaxPage() {
		super();
	}

	/**
	 * @return a new instance of ParallaxPage.
	 */
	public static ParallaxPage newInstance() {
		return new ParallaxPage();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {

		rootView =
				(FrameLayout) inflater.inflate(R.layout.fragment_parallax_page, container, false);
		frontImageHolder = (ImageView) rootView.findViewById(R.id.page_fragment_imageHolderFront);
		backImageHolder = (ImageView) rootView.findViewById(R.id.page_fragment_imageHolderBack);
		textHolder = (TextView) rootView.findViewById(R.id.page_fragment_textHolder);

		notifyFrontImageChanged();
		notifyBackImageChanged();
		notifyTextChanged();

		return rootView;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	/**
	 * Sets the front image of this ParallaxPage.
	 *
	 * @param frontImage
	 * 		the image to display
	 */
	public void setFrontImage(final Bitmap frontImage) {
		this.frontImage = frontImage;
		notifyFrontImageChanged();
	}

	/**
	 * @return the current front image, null if there is none
	 */
	public Bitmap getFrontImage() {
		return frontImage;
	}

	/**
	 * @return the resource id of the view which holds the front image
	 */
	public int getFrontImageHolderResId() {
		return frontImageHolder.getId();
	}

	/**
	 * Sets the back image of this ParallaxPage.
	 *
	 * @param backImage
	 * 		the image to display
	 */
	public void setBackImage(final Bitmap backImage) {
		this.backImage = backImage;
		notifyBackImageChanged();
	}

	/**
	 * @return the current back image, null if there is none
	 */
	public Bitmap getBackImage() {
		return backImage;
	}

	/**
	 * @return the resource id of the view which holds the back image
	 */
	public int getBackImageHolderResId() {
		return backImageHolder.getId();
	}

	/**
	 * Sets and displays the text of this ParallaxPage.
	 *
	 * @param text
	 * 		the text to display
	 */
	public void setText(final CharSequence text) {
		this.text = text;
		notifyTextChanged();
	}

	/**
	 * @return the current text, null if there is none
	 */
	public CharSequence getText() {
		return text;
	}

	/**
	 * @return the resource id of the view which holds the text
	 */
	public int getTextHolderResId() {
		return textHolder.getId();
	}

	/**
	 * Updates the UI of this ParallaxPage to reflect the image supplied to {@link
	 * #setFrontImage(Bitmap)}. There is no need to explicitly call this method after calling {@link
	 * #setFrontImage(Bitmap)}.
	 */
	public void notifyFrontImageChanged() {
		if (frontImageHolder != null) {
			frontImageHolder.setImageBitmap(null); // Forces reset
			frontImageHolder.setImageBitmap(frontImage);
		}
	}

	/**
	 * Updates the UI of this ParallaxPage to reflect the image supplied to {@link #setBackImage
	 * (Bitmap)}. There is no need to explicitly call this method after calling {@link
	 * #setBackImage(Bitmap)}.
	 */
	public void notifyBackImageChanged() {
		if (backImageHolder != null) {
			backImageHolder.setImageBitmap(null); // Forces reset
			backImageHolder.setImageBitmap(backImage);
		}
	}

	/**
	 * Updates the UI of this ParallaxPage to reflect the text supplied to {@link
	 * #setText(CharSequence)}. There is no need to explicitly call this method after calling {@link
	 * #setText(CharSequence)}.
	 */
	public void notifyTextChanged() {
		if (textHolder != null) {
			textHolder.setText(null); // Forces reset
			textHolder.setText(text);
		}
	}
}