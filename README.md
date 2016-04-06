# SlidingIntroScreen
This library simplifies the creation of introduction screens in Android apps. To give you an example of how easy it is to create an intro screen, [this activity](misc/example.gif) was created by subclassing one class and overriding two methods (see code [here](testapp/src/main/java/com/matthewtamlin/testapp/IntroTest.java)).


## Installation
To use this library, add `compile 'com.matthew-tamlin:sliding-intro-screen:1.1.0` to your gradle build file. Check the [maven repo](https://bintray.com/matthewtamlin/maven/SlidingIntroScreen/view) to make sure this is the latest version.


## Usage
There are only two classes you need to consider when using this library: [IntroActivity](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/IntroActivity.java) and [Page](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/Page.java). Both are abstract and must be subclassed to be used.

To use `IntroActivity` you must subclass it and override `generatePages()` and `progressToNextActivity()`. The former method is called by `onCreate(Bundle)`, and the later method is called when the user presses a "done" button. In `generatePages()` you initialise your pages and add them to the activity by calling `pages.add(Page)` or `pages.add(int, Page)`. In `progressToNextActivity()` you must release all resources and start the next activity. Although not necessary, you can add transition effects to the activity by overriding `onCreate()` and calling `viewPager.setPageTransformer(boolean, ViewPager.PageTransformer)`. An example of this class in use is shown [here](testapp/src/main/java/com/matthewtamlin/testapp/IntroTest.java). 

To customise the pages of your intro screen, create subclasses of `Page`. A basic subclass of `Page` is included in this library [here](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/ParallaxPage.java), however to maximise the functionality of your intro screen you should create your own subclasses. 


## SelectionIndicator
The [SelectionIndicator](library/src/main/java/com/matthewtamlin/sliding_intro_screen_library/SelectionIndicator.java) element can be used by itself if you wish to add a dot indicator to your app. To add a SelectionIndicator, add the following to your layout:

```java
<com.matthewtamlin.sliding_intro_screen_library.SelectionIndicator
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:numberOfDots=YOUR_INT_HERE
            app:activeItemIndex=YOUR_INT_HERE/>
```

The above code replicates the functionality of the dots on the Google Launcher homescreen, however if you want to further customise it then the following attributes can be added:

- app:inactiveDotDiameter and app:activeDotDiameter to set the diameters of the dots
- app:inactiveDotColor and app:selectedDotColor to set the colors of the dots
- app:dotTransitionDuration to set the time for animating the change from small to big (and back) 

Additionally, the view can be created programmatically by `SelectionIndicator si = new SelectionIndicator(context);`. Methods exist to modify the properties, similar to the aforementioned attributes. To update the page, just call method `si.setActiveItem(int, true)` from inside `ViewPager.OnPageChangeListener.onPageSelected(int)`.


## Compatibility
This library is compatible with Android 11 and up.
