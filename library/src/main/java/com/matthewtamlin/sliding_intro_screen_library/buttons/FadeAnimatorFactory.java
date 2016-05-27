package com.matthewtamlin.sliding_intro_screen_library.buttons;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;

public class FadeAnimatorFactory implements IntroButtonAnimationFactory {
	@Override
	public Animator newLeftButtonAppearAnimator(View leftButton) {
		return createFade(leftButton, 0, 1);
	}

	@Override
	public Animator newLeftButtonDisappearAnimator(View leftButton) {
		return createFade(leftButton, 1, 0);
	}

	@Override
	public Animator newRightButtonAppearAnimator(View rightButton) {
		return createFade(rightButton, 0, 1);
	}

	@Override
	public Animator newRightButtonDisappearAnimator(View rightButton) {
		return createFade(rightButton, 1, 0);
	}

	@Override
	public Animator newFinalButtonAppearAnimator(View finalButton) {
		return createFade(finalButton, 0, 1);
	}

	@Override
	public Animator newFinalButtonDisappearAnimator(View finalButton) {
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
	private Animator createFade(final View button, float startAlpha, float endAlpha) {
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
