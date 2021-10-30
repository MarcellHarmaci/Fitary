package hu.bme.aut.fitary.ui.viewWorkout

sealed class ViewWorkoutViewState

object Loading : ViewWorkoutViewState()

object LoadingFailed : ViewWorkoutViewState()

data class WorkoutLoaded(
    val workout: ViewWorkoutPresenter.Workout
): ViewWorkoutViewState()