package hu.bme.aut.fitary.ui.userWorkouts

sealed class UserWorkoutsViewState

object Loading : UserWorkoutsViewState()

data class UserWorkoutsLoaded(
    val workouts: List<UserWorkoutsPresenter.Workout>
) : UserWorkoutsViewState()