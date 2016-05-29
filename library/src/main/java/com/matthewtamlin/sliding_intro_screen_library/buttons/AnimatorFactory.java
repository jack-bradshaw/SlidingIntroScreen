package com.matthewtamlin.sliding_intro_screen_library.buttons;

import android.animation.Animator;
import android.view.View;

/**
 * Creates Animations for making IntroButtons appear and disappear. The animations must adhere to a
 * set of conditions, but are otherwise unconstrained. The design of this interface allows the
 * caller to explicitly specify which button the Animator is for, and whether the Animation should
 * make the button appear or disappear
 */
public interface AnimatorFactory {
	/**
	 * Creates a new Animator which can be used to make the left button of a IntroActivity appear.
	 * The Animator must adhere to the following conditions: <ul> <li>At the start of the animation,
	 * the button is invisible.</li> <li>At the end of the animation, the button is visible.</li>
	 * <li>The animation displays properly when the duration is set to 150 milliseconds.</li> </ul>
	 *
	 * @param leftButton
	 * 		the button to animate, not null
	 * @return an animator which can be run to make the supplied button appear, not null
	 */
	Animator newLeftButtonAppearAnimator(View leftButton);

	/**
	 * Creates a new Animator which can be used to make the left button of a IntroActivity
	 * disappear. The Animator must adhere to the following conditions: <ul> <li>At the start of the
	 * animation, the button is visible.</li> <li>At the end of the animation, the button is
	 * invisible.</li> <li>The animation displays properly when the duration is set to 150
	 * milliseconds.</li> </ul>
	 *
	 * @param leftButton
	 * 		the button to animate, not null
	 * @return an animator which can be run to make the supplied button disappear, not null
	 */
	Animator newLeftButtonDisappearAnimator(View leftButton);

	/**
	 * Creates a new Animator which can be used to make the right button of a IntroActivity appear.
	 * The Animator must adhere to the following conditions: <ul> <li>At the start of the animation,
	 * the button is invisible.</li> <li>At the end of the animation, the button is visible.</li>
	 * <li>The animation displays properly when the duration is set to 150 milliseconds.</li> </ul>
	 *
	 * @param rightButton
	 * 		the button to animate, not null
	 * @return an animator which can be run to make the supplied button appear, not null
	 */
	Animator newRightButtonAppearAnimator(View rightButton);

	/**
	 * Creates a new Animator which can be used to make the right button of a IntroActivity
	 * disappear. The Animator must adhere to the following conditions: <ul> <li>At the start of the
	 * animation, the button is visible.</li> <li>At the end of the animation, the button is
	 * invisible.</li> <li>The animation displays properly when the duration is set to 150
	 * milliseconds.</li> </ul>
	 *
	 * @param rightButton
	 * 		the button to animate, not null
	 * @return an animator which can be run to make the supplied button disappear, not null
	 */
	Animator newRightButtonDisappearAnimator(View rightButton);

	/**
	 * Creates a new Animator which can be used to make the final button of a IntroActivity appear.
	 * The Animator must adhere to the following conditions: <ul> <li>At the start of the animation,
	 * the button is invisible.</li> <li>At the end of the animation, the button is visible.</li>
	 * <li>The animation displays properly when the duration is set to 150 milliseconds.</li> </ul>
	 *
	 * @param finalButton
	 * 		the button to animate, not null
	 * @return an animator which can be run to make the supplied button appear, not null
	 */
	Animator newFinalButtonAppearAnimator(View finalButton);

	/**
	 * Creates a new Animator which can be used to make the final button of a IntroActivity
	 * disappear. The Animator must adhere to the following conditions: <ul> <li>At the start of the
	 * animation, the button is visible.</li> <li>At the end of the animation, the button is
	 * invisible.</li> <li>The animation displays properly when the duration is set to 150
	 * milliseconds.</li> </ul>
	 *
	 * @param finalButton
	 * 		the button to animate, not null
	 * @return an animator which can be run to make the supplied button disappear, not null
	 */
	Animator newFinalButtonDisappearAnimator(View finalButton);
}