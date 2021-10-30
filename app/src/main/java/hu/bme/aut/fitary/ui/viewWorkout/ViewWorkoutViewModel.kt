package hu.bme.aut.fitary.ui.viewWorkout

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class ViewWorkoutViewModel @Inject constructor(
    private val presenter: ViewWorkoutPresenter
) : RainbowCakeViewModel<ViewWorkoutViewState>(Loading) {

    fun loadWorkout(workoutId: String?) = execute {
        if (workoutId == null) {
            viewState = LoadingFailed
            return@execute
        }

        val workout = presenter.loadWorkout(workoutId)

        workout?.let {
            viewState = WorkoutLoaded(it)
        } ?: run {
            viewState = LoadingFailed
        }
    }

}