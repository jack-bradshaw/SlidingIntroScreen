# SlidingIntroScreen
A library to simplify the creation of introduction screens in Android apps. This library is simple to use, well documented and highly extensible. While default classes are provided for all components, the framework allows custom implementations in case the defaults don't satisfy. [This example](exampleapp/example.gif) (gif) was created by subclassing one activity and overriding a few methods. See the example code [here](exampleapp/src/main/java/com/matthewtamlin/exampleapp/ExampleActivity.java).

![Example of an intro](exampleapp/example.png)

## Installation
Add `compile 'com.matthew-tamlin:sliding-intro-screen:2.1.1'` to your gradle build file to use the latest version. Older version are available in the [maven repo](https://bintray.com/matthewtamlin/maven/SlidingIntroScreen/view).

## Quick start
The main class in this library is [IntroActivity](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/IntroActivity.java). To use `IntroActivity` you must subclass it and override `generatePages()` and `generateFinalButtonBehaviour()`. These methods are called by `onCreate()` to define the appearance and behaviour of your activity. In `generatePages()` you initialise your pages and return them in a Collection. In `generateFinalButtonBehaviour()` you return the behaviour you want to be executed when the user presses the done button. A behaviour is just a runnable with a reference to the activity. The `IntroButton.ProgressToNextActivity` class is designed to simplify moving to the next activity, but if you prefer you can implement the `IntroButton.Behaviour` interface and do whatever you want.

The javadoc of the IntroActivity class will give you a more in depth understanding of how to use the library. The other features of the library include:
- Hiding/showing the status bar.
- Easy customisation of the buttons, in terms of appearance and behaviour.
- Locking the page to touch and/or programmatic commands.
- Support for custom progress indicators.

## Other classes
The other components of this library you should be aware of are:
- [Page](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/Page.java)
- [IntroButton](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/IntroButton.java)
- [DotIndicator](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/DotIndicator.java) and [SelectionIndicator](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/SelectionIndicator.java)

### Page
The `Page` class extends `Fragment` and is used to display the content of your intro screen. Each page contains a color it would like to have drawn behind it, and the hosting `IntroActivity` blends these colors together when scrolling to create a continuous color effect. The `ParallaxPage` class is provided to simplify building an interface with parallax effects. Should you desire custom page layouts and behaviours, just subclass `Page` and use your subclass in `IntroActivity`.

### IntroButton
The `IntroButton` class differs from the Android framework button in a few ways. An `IntroButton` has two properties called Appearance and Behaviour which define how the button looks and how it functions when it is pressed.

The Appearance of an `IntroButton` defines how the text in the button is positioned relative to any icon in it. The appearance can be set to show only text, show only an icon, show both with the icon to the left of the text, or show both with the icon to the right of the text. Using the appearance property to change the icon/text relationship is better than manually setting the text and icon each time the apperarance needs to change, because it allows the `IntroButton` to manage the data internally and you don't have to worry about loading drawables each time you change the button. 

The Behaviour of an `IntroButton` defines the actions to take when the button is clicked. `IntroButton` still supports the `OnClickListener` interface, however using behaviours has important advantages. `Behaviour` is actually just an interface with three method signatures: `IntroActivity getActivity()`, `void setActivity(IntroActivity)` and `void run()`. Unlike regular `Runnable` implementations, the `run()` method can get a reference to an `IntroActivity` so that it may perform some action on it. This decouples the actions to take from any individual View or Activity, so that reusable operations can be written. The following concrete implementations are defined in the `IntroButton` class and can be passed to any `IntroButton`:
- `GoToPreviousPage` makes the target activity move back a page (unless the first page is currently displayed).
- `GoToNextPage` makes the target activiity move forward a page (unless the last page is currently displayed).
- `GoToFirstPage` makes the target activity move to the first page.
- `GoToLastPage` makes the target activity advance to the last page.
- `DoNothing` performs no operation. 
- `CloseApp` terminates the app affinity.
- `RequestPermissions` displays the standard permission request dialog to the user. The target app should override `onRequestPermissionsResult(int, String[], int[])`

In addition to these concrete classes, the most useful class is the `ProgressToNextActivity` abstract class. Implementations of this class must implent the `shouldLaunchActivity()` method.  The next activity will only be launched if the validation criteria defined in the method returns true. The class' default constructor accepts a `SharedPreferences.Editor` object. When the next activity is successfully launched, any pending changes in the editor are applied. This allows a shared perferences flag to be automatically set, so that the intro screen won't be displayed a second time if the user successfully completes it.

If the above implementations are not sufficient for your purposes, you can either implement the `Behaviour` interface or subclass the `IntroButton.BehaviourAdapter` class. The adapter contains a simple getter/setter combo for `getActivity()` and `setActivity()`, and declares `run()` as abstract. This removes the need to write the same getter/setter boilerplate code for each `Behaviour` you define.

### DotIndicator and SelectionIndicator
At the bottom of `IntroActivity`, a `SelectionIndicator` displays the user's current progress through the activity. This library provides one concrete implementation of the `SelectionIndicator` interface called `DotIndicator`. By default, all `IntroActivity` instances have a `DotIndicator`. The methods of the `IntroActivity` provide a mechanism for setting a new indicator and modifying the existing one. Have a look into the Javadoc of `DotIndicator` to see the ways in which it can be customised. 

The `DotIndicator` class can be used in other contexts as well and isn't limited to this library. To create a `DotIndicator` in your custom layout, add:

```java
<com.matthewtamlin.sliding_intro_screen_library.DotIndicator>
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:numberOfDots=YOUR_INT_HERE
            app:selectedDotIndex=YOUR_INT_HERE/>
```

The default `DotIndicator` replicates the functionality of similar indicators in Google-made apps, however if you want to further customise it then the following attributes can be added:

- `app:unselectedDotDiameter` and `app:selectedDotDiameter` to set the diameters of the dots
- `app:unselectedDotColor` and `app:selectedDotColor `to set the colors of the dots
- `app:dotTransitionDuration` to set the time for animating the change between selected and unselected 
- `add:spacingBetweenDots` to change the distance between each dot

Alternatively, the indicator can be created programmatically with `DotIndicator i = new DotIndicator(context);`. Methods exist to modify the properties, similar to the aforementioned attributes.

## Licensing
This library is licenced under the Apache v2.0 licence. Have a look at [the license](LICENSE) for details.

## Compatibility
This library is compatible with Android 11 and up. The 2.0.0 update breaks compatibility with previous versions of the library.
