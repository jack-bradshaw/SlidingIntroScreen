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
import android.view.View;

/**
 * Creates Animations for making IntroButtons appear and disappear. The animations must adhere to a
 * set of conditions, but are otherwise unconstrained. The design of this interface allows the
 * caller to explicitly specify which button the Animator is for, and whether the Animation should
 * make the button appear or disappear.
 */
public interface AnimatorFactory {
	/**
	 * Creates a new Animator which can be used to make the left button of a IntroActivity appear.
	 * The Animator adheres to the following conditions: <ul> <li>At the start of the animation, the
	 * button is invisible to the user.</li> <li>At the end of the animation, the button is visible
	 * to the user.</li> <li>The animation displays properly when the duration is set to 150
	 * milliseconds.</li> </ul>
	 *
	 * @param leftButton
	 * 		the button to animate, not null
	 * @return an Animator which can be run to make the supplied button appear, not null
	 */
	Animator newLeftButtonAppearAnimator(View leftButton);

	/**
	 * Creates a new Animator which can be used to make the left button of a IntroActivity
	 * disappear. The Animator adheres to the following conditions: <ul> <li>At the start of the
	 * animation, the button is visible to the user.</li> <li>At the end of the animation, the
	 * button is invisible to the user.</li> <li>The animation displays properly when the duration
	 * is set to 150 milliseconds.</li> </ul>
	 *
	 * @param leftButton
	 * 		the button to animate, not null
	 * @return an Animator which can be run to make the supplied button disappear, not null
	 */
	Animator newLeftButtonDisappearAnimator(View leftButton);

	/**
	 * Creates a new Animator which can be used to make the right button of a IntroActivity appear.
	 * The Animator adheres to the following conditions: <ul> <li>At the start of the animation, the
	 * button is invisible to the user.</li> <li>At the end of the animation, the button is visible
	 * to the user.</li> <li>The animation displays properly when the duration is set to 150
	 * milliseconds.</li> </ul>
	 *
	 * @param rightButton
	 * 		the button to animate, not null
	 * @return an Animator which can be run to make the supplied button appear, not null
	 */
	Animator newRightButtonAppearAnimator(View rightButton);

	/**
	 * Creates a new Animator which can be used to make the right button of a IntroActivity
	 * disappear. The Animator adheres to the following conditions: <ul> <li>At the start of the
	 * animation, the button is visible to the user.</li> <li>At the end of the animation, the
	 * button is invisible to the user.</li> <li>The animation displays properly when the duration
	 * is set to 150 milliseconds.</li> </ul>
	 *
	 * @param rightButton
	 * 		the button to animate, not null
	 * @return an Animator which can be run to make the supplied button disappear, not null
	 */
	Animator newRightButtonDisappearAnimator(View rightButton);

	/**
	 * Creates a new Animator which can be used to make the final button of a IntroActivity appear.
	 * The Animator adheres to the following conditions: <ul> <li>At the start of the animation, the
	 * button is invisible to the user.</li> <li>At the end of the animation, the button is visible
	 * to the user.</li> <li>The animation displays properly when the duration is set to 150
	 * milliseconds.</li> </ul>
	 *
	 * @param finalButton
	 * 		the button to animate, not null
	 * @return an Animator which can be run to make the supplied button appear, not null
	 */
	Animator newFinalButtonAppearAnimator(View finalButton);

	/**
	 * Creates a new Animator which can be used to make the final button of a IntroActivity
	 * disappear. The Animator adheres to the following conditions: <ul> <li>At the start of the
	 * animation, the button is visible to the user.</li> <li>At the end of the animation, the
	 * button is invisible to the user.</li> <li>The animation displays properly when the duration
	 * is set to 150 milliseconds.</li> </ul>
	 *
	 * @param finalButton
	 * 		the button to animate, not null
	 * @return an Animator which can be run to make the supplied button disappear, not null
	 */
	Animator newFinalButtonDisappearAnimator(View finalButton);
}