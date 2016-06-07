# SlidingIntroScreen
A library designed to simplify the creation of introduction screens in Android apps. The design of the library is heavily interface driven which provides a great level of customisation. This is balanced against usability by providing at least one default implementation for each component. Even without creating custom components, it is possible to create beautiful and unique introductions for your app. The beauty and simplicity of the library is shown in [this](exampleapp/example.gif) example, which was created by subclassing just one Activity. The code for the example can be found [here](exampleapp/src/main/java/com/matthewtamlin/exampleapp/ExampleActivity.java).

![Example of an introduction screen](exampleapp/example.png)

## Installation
Releases are made available through jCentre. Add `compile 'com.matthew-tamlin:sliding-intro-screen:3.0.1'` to your gradle build file to use the latest version. Older versions are available in the [maven repo](https://bintray.com/matthewtamlin/maven/SlidingIntroScreen/view).

## Quick Start
[IntroActivity](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/core/IntroActivity.java) is the primary class of this library because it coordinates and displays all the other components. The UI features two main components: a series of Fragments (referred to as pages) hosted in a ViewPager, and a navigation bar. The pages display the content of the introduction screen, and the navigation bar displays the user's progress through the introduction. The navigation bar contains three configurable buttons known as the left button, right button and final button. The left and right buttons are shown on all but the last page, and the final button is shown on only the last page. 

IntroActivity is an abstract class, therefore to use it you must create a subclass and implement both `generatePages()` and `generateFinalButtonBehaviour()`. These methods are called by `onCreate()` to define the activity's appearance and behaviour. The pages to display are created in `generatePages()`, and the Behaviour of the final button is created in `generateFinalButtonBehaviour()`. A Behaviour is just a Runnable which holds a reference to an IntroActivity, which allows it to manipulate the activity when run. This is further explained in the IntroButton section below. 

The other methods of the IntroActivity class provide further customisation options, such as:
- Hiding/showing the status bar.
- Programatically changing the page.
- Locking the page to touch events and/or programmatic commands (such as button presses).
- Hiding/showing the horizontal divider atop the navigation bar.
- Changing the page transformer.
- Adding/removing page change listeners.
- Accessing the pages.
- Hiding/showing the navigation buttons.

The Javadoc of the IntroActivity class contains further information, and the [example app](exampleapp/src/main/java/com/matthewtamlin/exampleapp/ExampleActivity.java) demonstrates how the class is used in practice.

## Other Components
The other notable components of this library are:
- [IntroButton](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/buttons/IntroButton.java)
- [SelectionIndicator](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/indicators/SelectionIndicator.java)
- [BackgroundManager](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/background/BackgroundManager.java) 
- [AnimatorFactory](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/buttons/AnimatorFactory.java)

### IntroButton
The IntroButton class has two important properties: appearance and behaviour. These properties define how the button displays and how it behaves when pressed. Separating appearance and behaviour from the button itself allows buttons to be configured dynamically, and avoids boilerplate code. The appearance of a button is defined using the `IntroButton.Appearance` enum, and the behaviour of a button is defined using the `IntroButton.Behaviour` interface.

The appearance of an IntroButton defines how text and icons are displayed within the button. The appearance can be set to show just text, just an icon, or both. In the case of both, the appearance also defines whether the icon shows to the left or the right of the text. Using an Appearance object to configure how the button displays has advantages over manually changing the text/icon. The IntroButton class is designed to store text and icons internally, so that these resources do not need to be externally cached. When the Appearance changes to display an icon, the icon is loaded from inside the button. This reduces boilerplate code, eliminates the need to externally cache resources, and reduces occportunities for bugs to occur.

The behaviour of an IntroButton defines the actions to take when the button is clicked. The behaviour of an IntroButton is set by passing the button an implementation of the Behaviour interface. Using a separate object to define the behaviour decouples the on-click actions from the buttons, and makes code more reusable. The Behaviour interface extends the Runnable interface and has two extra methods: `IntroActivity getActivity()` and `void setActivity(IntroActivity)`. Using these extra methods, the `run()` method can obtain a reference to an IntroActivity to manipulate. Passing Behaviours to the navigation buttons of an IntroActivity is the simplest way to manipulate the IntroActivity when the buttons are pressed. The following concrete implementations of the Behaviour interface meet the most common needs:
- `IntroButton.GoToPreviousPage` makes the target IntroActivity display the previous page (unless the first page is currently displayed).
- `IntroButton.GoToNextPage` makes the target IntroActivity display the next page (unless the last page is currently displayed).
- `IntroButton.GoToFirstPage` makes the target IntroActivity return to the first page.
- `IntroButton.GoToLastPage` makes the target IntroActivity advance to the last page.
- `IntroButton.DoNothing` performs no operation. 
- `IntroButton.CloseApp` terminates the app affinity, effectively closing the app.
- `IntroButton.RequestPermissions` displays the standard permission request dialog to the user. The exact permissions to request are provided to the constructor, along with a request code to use when receiving the result.
- `IntroButton.ProgressToNextActivity` ends the introduction and progresses to the next Activity in the app.

The ProgressToNextActivity class is one of the most useful classes in the library. The default constructor accepts a `SharedPreferences.Editor` paramater which can be used to make sure the introduction is only shown once. Any pending changes in the editor are committed when the next activity is successfully launched, which allows a shared preferences flag to be automatically set when the introduction completes. By checking the status of this flag each time the IntroActivity is created, the introduction can be skipped if the user has previously completed it. The class also contains a method called `shouldLaunchActivity()` which defines validation logic. By default this method returns true, but subclasses can override it to define conditions which must pass before the next activity is launched.

If the above implementations are not sufficient, the interface can be directly implemented or the `IntroButton.BehaviourAdapter` class can be extended. The adapter class provides a simple getter/setter mechanism for `getActivity()` and `setActivity()`, and declares `run()` as abstract. This removes the need to write the same getter/setter boilerplate code for each Behaviour. The `run()` implementation simply needs to call `getActivity()` and perform the desired action on the retured activity (after doing a null check).

### SelectionIndicator
In the centre of the navigation bar within each IntroActivity is a SelectionIndicator. This is a visual element which displays the user's current progress through the introduction. The IntroActivity class provides methods for setting a new indicator, or modifying the existing one. SelectionIndicator itself is an interface, therfore custom indicators can be used. By default, a [DotIndicator](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/indicators/DotIndicator.java) is used, as shown in the above example image.

DotIndicator does not need to be used in an IntroActivity. To create a DotIndicator in any layout, just add the following XML:
```xml
<com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator>
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:numberOfDots=YOUR_INT_HERE
            app:selectedDotIndex=YOUR_INT_HERE/>
```

DotIndicator has been designed to replicate the appearance and functionality of similar indicators in Google-made apps. The following attributes can be added to the XML declaration to customise the indicator:
- `app:unselectedDotDiameter` and `app:selectedDotDiameter` to set the dot diameterss.
- `app:unselectedDotColor` and `app:selectedDotColor `to set the dot colors.
- `app:dotTransitionDuration` to set the animation time when transitioning dots between selected and unselected.
- `add:spacingBetweenDots` to change the distance between each dot.

Alternatively, the indicator can be created programmatically with `DotIndicator myIndicator = new DotIndicator(context);`. The class provides methods for modifying the properties, similar to the aforementioned attributes.

### BackgroundManager
Unless the individual pages define their own backgrounds, it is highly recommended that the background of the IntroActivity be changed (by default it is grey). The background of an IntroActivity can be changed in two ways: by manually changing the background color of the root View, or by supplying a BackgroundManager to the activity. The former approach is simpler and less error prone, and is ideal when a static background is all that is needed. The latter approach is ideal when a dynamic background is desired. 

The BackgroundManager interface defines a single method: `updateBackground(View background, int index, float offset)`. This method is called by IntroActivity each time the scroll position changes, which allows the background to be dependent on the user's progress through the introduction. The [ColorBlender](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/background/ColorBlender.java) class paints a variable color on the background, and blends colors together to create a continuous color scrolling effect as the user progresses. An example of this effect is shown [here](exampleapp/src/main/java/com/matthewtamlin/exampleapp/ExampleActivity.java).

### AnimatorFactory
The buttons of an IntroActivity must sometimes switch between enabled and disabled, such is the case when the last page is reached. By default, the left and right buttons are disabled on the last page and the final button is enabled. Rather than have a jarring transition between visible and invisible, the change is transitioned smoothly using Animators supplied by an a AnimatorFactory. The default AnimatorFactory causes the buttons to smoothly fade in and out, however custom implementations of the AnimatorFactory can be used by overriding `generateButtonAnimatorFactory()` in IntroActivity. To make sure the animations always display correctly, the AnimatorFactory cannot be changed after the activity is created.

## Licensing
This library is licenced under the Apache v2.0 licence. Have a look at [the license](LICENSE) for details.

## Compatibility
This library is compatible with Android 11 and up. The 3.0.0 update breaks compatibility with previous versions of the library.
