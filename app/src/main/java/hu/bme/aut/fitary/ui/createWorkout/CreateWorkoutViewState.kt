package hu.bme.aut.fitary.ui.createWorkout

sealed class CreateWorkoutViewState

object Loading : CreateWorkoutViewState()

object SavingWorkout : CreateWorkoutViewState()

data class WorkoutCreationInProgress(
    val exercises: List<CreateWorkoutPresenter.Exercise>,
    val comment: String?
) : CreateWorkoutViewState()