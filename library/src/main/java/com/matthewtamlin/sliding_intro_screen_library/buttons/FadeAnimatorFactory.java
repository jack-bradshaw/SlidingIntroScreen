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

package com.matthewtamlin.sliding_intro_screen_library.buttons;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Creates fade in effects for appearing buttons and fade out effects for disappearing buttons.
 */
public class FadeAnimatorFactory implements AnimatorFactory {
	@Override
	public Animator newLeftButtonAppearAnimator(final View leftButton) {
		return createFade(leftButton, 0, 1);
	}

	@Override
	public Animator newLeftButtonDisappearAnimator(final View leftButton) {
		return createFade(leftButton, 1, 0);
	}

	@Override
	public Animator newRightButtonAppearAnimator(final View rightButton) {
		return createFade(rightButton, 0, 1);
	}

	@Override
	public Animator newRightButtonDisappearAnimator(final View rightButton) {
		return createFade(rightButton, 1, 0);
	}

	@Override
	public Animator newFinalButtonAppearAnimator(final View finalButton) {
		return createFade(finalButton, 0, 1);
	}

	@Override
	public Animator newFinalButtonDisappearAnimator(final View finalButton) {
		return createFade(finalButton, 1, 0);
	}

	/**
	 * Creates an animation which fades a button by gradually changing its alpha level. The duration
	 * of the animation is not set.
	 *
	 * @param button
	 * 		the button to animate
	 * @param startAlpha
	 * 		the alpha to use at the start of the animation
	 * @param endAlpha
	 * 		the alpha to use at the end of the animation
	 * @return the fade animation, not null
	 */
	private Animator createFade(final View button, final float startAlpha, final float endAlpha) {
		final ValueAnimator fadeAnimator = ValueAnimator.ofFloat(startAlpha, endAlpha);

		fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				final float value = (Float) animation.getAnimatedValue();
				button.setAlpha(value);
			}
		});

		return fadeAnimator;
	}
}