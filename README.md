_Read more about this project [here](https://medium.com/@arifnadeem7/using-coroutines-and-flow-with-mvvm-architecture-796142dbfc2f)_

### Description
This project demonstrates usage of Kotlin coroutines and Flow with MVVM architecture of Android applications.

### Libraries used

1. [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For offloading long running tasks to background

2. [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) - Works very well with coroutines, provides us with cold streams which can be transformed using well known reactive operators.

3. [Dagger](https://dagger.dev) - For dependency injection

4. [Room](https://developer.android.com/training/data-storage/room) - For storing our application data

5. [Jetpack Navigation](https://developer.android.com/guide/navigation/navigation-getting-started) - Used to effortlessly navigate between screens.

6. [Retrofit](https://github.com/square/retrofit) - For making API calls

7. [Material components for Android](https://material.io/develop/android/docs/getting-started/) - For material theming

### Acknowledgements:

* [Video from Android Dev Summit 2019](https://www.youtube.com/watch?v=B8ppnjGPAGE) presented by Jose Alcérreca, Yigit Boyar; which inspired me to develop this project
* Used [Deferred OkHttp initialization](https://www.zacsweers.dev/dagger-party-tricks-deferred-okhttp-init/) as described by Zac Sweers in his blog.
