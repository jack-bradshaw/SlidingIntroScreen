# SlidingIntroScreen
A library to simplify the creation of introduction screens in Android apps. This library is simple to use, well documented and highly extensible. While default classes are provided for all components, the framework allows custom implementations in case the defaults don't satisfy. [This example](exampleapp/example.gif) (gif) was created by subclassing one activity and overriding a few methods. See the example code [here](exampleapp/src/main/java/com/matthewtamlin/exampleapp/ExampleActivity.java).

![Example of an intro](exampleapp/example.png)

## Installation
Releases are made available using gradle. Add `compile 'com.matthew-tamlin:sliding-intro-screen:2.1.0` to your gradle build file to use the latest version. Older version are available in the [maven repo](https://bintray.com/matthewtamlin/maven/SlidingIntroScreen/view).


## Quick start
The main class in this library is [IntroActivity](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/IntroActivity.java). To use `IntroActivity` you must subclass it and override `generatePages()` and `generateFinalButtonBehaviour()`. These methods are called by `onCreate()` to define the appearance and behaviour of your activity. In `generatePages()` you initialise your pages and return them in a Collection. In `generateFinalButtonBehaviour()` you return the behaviour you want to be executed when the user presses the done button. A behaviour is just a runnable with a reference to the activity. The `IntroButton.ProgressToNextActivity` class is designed to simplify moving to the next activity, but if you prefer you can implement the `IntroButton.Behaviour` interface and do whatever you want.

The javadoc of the IntroActivity class will give you a more in depth understanding of how to use the library. The other features of the library include:
- Hiding/showing the status bar.
- Easy customisation of the buttons, in terms of appearance and behaviour.
- Locking the page to touch and/or programmatic commands.
- Support for custom progress indicators.

## SelectionIndicator
The [DotIndicator](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/DotIndicator.java) class is provided for convenience and meets most needs, however the [SelectionIndicator](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/SelectionIndicator.java) interface makes it easy to implement your own custom progress indicator. If you only want to use the DotIndicator and don't care about the rest of the library, then add the following to your layout:

```java
<com.matthewtamlin.sliding_intro_screen_library.DotIndicator>
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:numberOfDots=YOUR_INT_HERE
            app:selectedDotIndex=YOUR_INT_HERE/>
```

The above code replicates the functionality of the dots in Google-made apps, however if you want to further customise it then the following attributes can be added:

- `app:unselectedDotDiameter` and `app:selectedDotDiameter` to set the diameters of the dots
- `app:unselectedDotColor` and `app:selectedDotColor `to set the colors of the dots
- `app:dotTransitionDuration` to set the time for animating the change between selected and unselected 
- `add:spacingBetweenDots` to change the distance between each dot

Alternatively, the indicator can be created programmatically with `DotIndicator i = new DotIndicator(context);`. Methods exist to modify the properties, similar to the aforementioned attributes. To update the page, just call method `i.setSelectedItem(int, true)` from inside `ViewPager.OnPageChangeListener.onPageSelected(int)`.


## Compatibility
This library is compatible with Android 11 and up. The 2.0.0 update breaks compatibility with pervious versions.
