package hu.bme.aut.fitary.ui.userWorkouts

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.editWorkout.EditWorkoutFragment
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class UserWorkoutsViewModel @Inject constructor(
    private val presenter: UserWorkoutsPresenter
) : RainbowCakeViewModel<UserWorkoutsViewState>(Loading) {

    init {
        executeNonBlocking {
            presenter.workouts.collect {
                viewState = UserWorkoutsLoaded(it)
            }
        }
    }

    fun onPopupItemSelected(item: MenuItem, navController: NavController) = execute {
        val position = item.intent.getIntExtra("position", 0)

        if (position == RecyclerView.NO_POSITION)
            return@execute

        val workout = (viewState as UserWorkoutsLoaded).workouts[position]

        when (item.itemId) {
            R.id.item_edit_workout -> {
                val bundle = Bundle().apply {
                    putInt("purpose", EditWorkoutFragment.Purpose.EDIT_WORKOUT)
                    putString("workout_id", workout.id)
                }
                navController.navigate(R.id.nav_edit_or_create_workout, bundle)
            }
            R.id.item_delete_workout -> {
                workout.id?.let { presenter.deleteWorkout(workoutId = it) }
            }
            R.id.item_copy_workout -> {
                val bundle = Bundle().apply {
                    putInt("purpose", EditWorkoutFragment.Purpose.COPY_WORKOUT)
                    putString("workout_id", workout.id)
                }
                navController.navigate(R.id.nav_edit_or_create_workout, bundle)
            }
        }
    }

}