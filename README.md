# Fitary - Fitness Diary

Fitary a Kotlin based native Android application meant to replace our friend group's shared Google Sheet for workout tracking.

## Technologies Used

- Android
- Kotlin
- Gradle - Building and handling dependencies
- [RainbowCake](https://github.com/rainbowcake/rainbowcake) - architecture & guidelines
- Google Firebase
    - User authentication
    - Real-time Database
- Dagger - dependency injection
- [MPAndroidCharts](https://github.com/PhilJay/MPAndroidChart)

## RainbowCake

<img src="./docs/rainbowcake-icon.png" alt="RainbowCake logo" width="150" height="150" />

I used the architecture recommended by [RainbowCake](https://github.com/rainbowcake/rainbowcake). It builds on top of Jetpack, both in terms of code and ideas.

For more information, see the official documentation on [rainbowcake.dev](https://rainbowcake.dev/).

## Dependency injection

DI is managed by Dagger. All Di related things are in the `app\src\main\java\hu\bme\aut\fitary\di` package.

All the view models are listed in `ViewModelModule.kt` file like so:

```kotlin
@Binds
@IntoMap
@ViewModelKey(MyViewModel::class)
abstract fun bindMyViewModel(myViewModel: MyViewModel): ViewModel
```

When a view model is instantiated Dagger provides all it's dependencies defined in the VM's constructor.
