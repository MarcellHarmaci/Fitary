package hu.bme.aut.fitary.di

import androidx.lifecycle.ViewModel
import co.zsmb.rainbowcake.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import hu.bme.aut.fitary.ui.barChart.BarChartViewModel
import hu.bme.aut.fitary.ui.exerciseDialog.ExerciseDialogViewModel
import hu.bme.aut.fitary.ui.socialWorkouts.SocialWorkoutsViewModel

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BarChartViewModel::class)
    abstract fun bindBarChartViewModel(barChartViewModel: BarChartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExerciseDialogViewModel::class)
    abstract fun bindExerciseDialogViewModel(exerciseDialogViewModel: ExerciseDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SocialWorkoutsViewModel::class)
    abstract fun bindSocialWorkoutsViewModel(socialWorkoutsViewModel: SocialWorkoutsViewModel): ViewModel
}
