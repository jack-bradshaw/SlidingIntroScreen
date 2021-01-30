# SlidingIntroScreen
Your app should have a beginning which welcomes the user and makes them want to continue using your product. This library was designed to simplify the creation of introduction screens, so that any app can have a high quality introduction. The example below on the left demonstrates a real-life application of the library, and the example on the right demonstrates some of the features of the library such as parallax scrolling and automatic color blending.

<img src="https://raw.githubusercontent.com/MatthewTamlin/SlidingIntroScreen/master/artwork/mr-d-food-example.gif" width="425"/> <img src="https://raw.githubusercontent.com/MatthewTamlin/SlidingIntroScreen/master/artwork/dots-example.gif" width="425"/> 

## Dependency
To use the library, add the following to your gradle build file:
```groovy
repositories {
	jcenter()
}

dependencies {
	implementation 'com.matthew-tamlin:sliding-intro-screen:3.2.0'
}
```

Older versions are available in [the Maven repo](https://bintray.com/matthewtamlin/maven/SlidingIntroScreen).

**SUPPORT NOTICE: This library is now STABLE. It is no longer under active development, however pull requests from others are still being accepted.**

## Quick-start
[IntroActivity](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/core/IntroActivity.java) is the primary class of this library because it coordinates and displays all the other components. The activity coordinates two main components: a standard Android ViewPager and a custom navigation bar. The view pager hosts several pages which in turn display the content of the introduction screen. The navigation bar displays the user's progress through the introduction, and provides three buttons for navigation. The left and right buttons are shown on all but the last page, and the final button is shown on only the last page. 

IntroActivity is an abstract class, therefore to use it you must create a subclass and implement some abstract methods. Implementing `generatePages()` allows you to define the content of the introduction screen, and implementing `generateFinalButtonBehaviour()` allows you to define what happens when the final button is clicked.

The other methods of the IntroActivity class provide further customisation options, such as:
- Programatically changing the page.
- Locking the page to touch events and/or programmatic commands (such as button presses).
- Hiding/showing the horizontal divider of the navigation bar.
- Changing the page transformer (applies effects when scrolling).
- Changing the background manager (responsible for updating the background when scrolling).
- Adding/removing page change listeners.
- Getting references to the pages.
- Toggling the visibility of the buttons.

The Javadoc of the IntroActivity class contains further information, and the [example app](example%20app/src/main/java/com/matthewtamlin/sliding_intro_screen_example_app/DotsActivity.java) demonstrates how the class can be used in practice.

## Other Components
The other notable components of this library are:
- [IntroButton](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/buttons/IntroButton.java)
- [SelectionIndicator](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/indicators/SelectionIndicator.java)
- [BackgroundManager](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/background/BackgroundManager.java) 
- [AnimatorFactory](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/buttons/AnimatorFactory.java)

### IntroButton
The IntroButton class has two important properties: appearance and behaviour. These properties define how the button is displayed and how it behaves when pressed. Separating appearance and behaviour allows buttons to be configured dynamically and avoids boilerplate code. The appearance is limited to the options defined by the `IntroButton.Appearance` enum, however the behaviour has no such limitations.

The behaviour of an IntroButton defines the actions to take when the button is clicked, and is set by passing an implementation of the Behaviour interface to the button. The Behaviour interface extends Runnable and has two extra methods: `IntroActivity getActivity()` and `void setActivity(IntroActivity)`. Using these extra methods, the `run()` method can get a reference to an IntroActivity and call upon it as desired. The following implementations of the Behaviour interface meet the most common needs:
- `IntroButton.GoToPreviousPage`
- `IntroButton.GoToNextPage`
- `IntroButton.GoToFirstPage`
- `IntroButton.GoToLastPage`
- `IntroButton.DoNothing`
- `IntroButton.CloseApp`
- `IntroButton.RequestPermissions`
- `IntroButton.ProgressToNextActivity`

The last behaviour is worth further explanation as it is one of the most useful classes in the library. In addition to launching the next activity, the behaviour contains a mechanism for preventing the introduction from being shown again. The behaviour accepts a `SharedPreferences.Editor` at construction, and it commits any pending changes when the next activity is successfully launched. This mechanism can be used to set a shared preferences flag which indicates when the activity has completed. By checking for this flag each time the app is launched, the introduction can be skipped if already completed.

If the provided implementations are not sufficient, the interface can be directly implemented or the `IntroButton.BehaviourAdapter` class can be extended.

### SelectionIndicator
The navigation bar visually displays the user's progress through the introduction in the form of a [DotIndicator](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/indicators/DotIndicator.java), which is an implementation of the SelectionIndicator interface. DotIndicator has been designed to replicate the appearance and functionality of such indicators in Google-made apps, however can be customised in several ways to meet the need.

### BackgroundManager
Unless the individual pages define their own backgrounds, it is highly recommended that the background of the IntroActivity be changed (the default is an unattractive grey color). The background of an IntroActivity can be changed in two ways: by manually changing the background color of the root view, or by supplying a BackgroundManager to the activity. The former approach is simpler and less error prone, and is ideal when a static background is all that is needed. The latter approach is ideal when a dynamic background is desired. 

The BackgroundManager interface defines a single method: `updateBackground(View background, int index, float offset)`. This method is called by IntroActivity each time the scroll position changes, which allows the background to be dependent on the user's progress through the introduction. The [ColorBlender](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/background/ColorBlender.java) blends colors together to create a continuous color scrolling effect as the user progresses. An example of this background manager is shown in the above right image.

### AnimatorFactory
The buttons shown in the IntroActivity must sometimes be enabled and disabled, which requires their visibility to change. Rather than have a jarring instantaneous transition between visible and invisible, the change can be transitioned smoothly using Animators supplied by an a AnimatorFactory. The default AnimatorFactory causes the buttons to smoothly fade in and out, however custom implementations of the AnimatorFactory can be used by overriding `generateButtonAnimatorFactory()` in IntroActivity and returning a custom implementation. To make sure the animations are always displayed correctly, the AnimatorFactory cannot be changed after the activity is created.

## Licensing
This library is licensed under the Apache v2.0 licence. Have a look at [the license](LICENSE) for details.

## Attribution
This readme shows a screen from the [Mr D Food app](https://play.google.com/store/apps/details?id=com.mrd.food) which was created using this library. The content shown in the image is owned by the [Mr D Food](https://www.mrdfood.com) company, who has kindly allowed its use in this repository. As such, the associated files are strictly excluded from the terms of [the license](LICENSE).

## Compatibility
This library is compatible with Android 11 and up.
