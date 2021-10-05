package hu.bme.aut.fitary.ui.createWorkout

sealed class CreateWorkoutViewState

object Loading : CreateWorkoutViewState()

object SavingWorkout : CreateWorkoutViewState()

data class WorkoutCreationInProgress(
    val exercises: List<CreateWorkoutPresenter.Exercise>,
    val score: Double = 0.0,
    val comment: String?
) : CreateWorkoutViewState()