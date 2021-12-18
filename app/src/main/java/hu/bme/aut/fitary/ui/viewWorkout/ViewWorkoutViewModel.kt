package hu.bme.aut.fitary.ui.viewWorkout

import android.os.Bundle
import androidx.navigation.NavController
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.editWorkout.EditWorkoutFragment
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

    fun createCopy(navController: NavController) = execute {
        val bundle = Bundle().apply {
            putInt("purpose", EditWorkoutFragment.Purpose.COPY_WORKOUT)
            putString("workout_id", (viewState as WorkoutLoaded).workout.id)
        }
        navController.navigate(R.id.nav_edit_or_create_workout, bundle)
    }

}