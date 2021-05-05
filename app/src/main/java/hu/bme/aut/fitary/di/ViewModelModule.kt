package hu.bme.aut.fitary.di

import androidx.lifecycle.ViewModel
import co.zsmb.rainbowcake.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import hu.bme.aut.fitary.ui.charts.pieChart.PieChartViewModel
import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutViewModel
import hu.bme.aut.fitary.ui.socialWorkouts.SocialWorkoutsViewModel
import hu.bme.aut.fitary.ui.userWorkouts.UserWorkoutsViewModel

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SocialWorkoutsViewModel::class)
    abstract fun bindSocialWorkoutsViewModel(socialWorkoutsViewModel: SocialWorkoutsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserWorkoutsViewModel::class)
    abstract fun bindUserWorkoutsViewModel(userWorkoutsViewModel: UserWorkoutsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateWorkoutViewModel::class)
    abstract fun bindCreateWorkoutViewModel(createWorkoutViewModel: CreateWorkoutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PieChartViewModel::class)
    abstract fun bindPieChartViewModel(pieChartViewModel: PieChartViewModel): ViewModel
}
