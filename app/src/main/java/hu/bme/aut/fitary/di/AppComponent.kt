package hu.bme.aut.fitary.di

import co.zsmb.rainbowcake.dagger.RainbowCakeComponent
import co.zsmb.rainbowcake.dagger.RainbowCakeModule
import hu.bme.aut.fitary.di.ApplicationModule
import hu.bme.aut.fitary.di.ViewModelModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        RainbowCakeModule::class,
        ApplicationModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : RainbowCakeComponent
