package hu.bme.aut.fitary.di

import androidx.lifecycle.ViewModel
import co.zsmb.rainbowcake.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import hu.bme.aut.fitary.ui.charts.pieChart.PieChartViewModel
import hu.bme.aut.fitary.ui.editWorkout.EditWorkoutViewModel
import hu.bme.aut.fitary.ui.socialWorkouts.SocialWorkoutsViewModel
import hu.bme.aut.fitary.ui.userProfile.UserProfileViewModel
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
    @ViewModelKey(EditWorkoutViewModel::class)
    abstract fun bindCreateWorkoutViewModel(editWorkoutViewModel: EditWorkoutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PieChartViewModel::class)
    abstract fun bindPieChartViewModel(pieChartViewModel: PieChartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserProfileViewModel::class)
    abstract fun bindUserProfileViewModel(userProfileViewModel: UserProfileViewModel): ViewModel
}
