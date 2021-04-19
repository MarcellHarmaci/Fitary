package hu.bme.aut.fitary.ui.createWorkout

sealed class CreateWorkoutViewState

object Loading : CreateWorkoutViewState()

object SavingWorkout : CreateWorkoutViewState()

data class CreateWorkoutLoaded(
    val exercises: List<CreateWorkoutPresenter.Exercise>
) : CreateWorkoutViewState()