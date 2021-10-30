package hu.bme.aut.fitary.ui.viewWorkout

import android.os.Bundle
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.fitary.R

class ViewWorkoutFragment : RainbowCakeFragment<ViewWorkoutViewState, ViewWorkoutViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_view_workout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val workoutId = arguments?.getString("workout_id")
        viewModel.loadWorkout(workoutId)
    }

    override fun render(viewState: ViewWorkoutViewState) {
        when (viewState) {
            is Loading -> {

            }
            is LoadingFailed -> {

            }
            is WorkoutLoaded -> {

            }
        }.exhaustive
    }

}