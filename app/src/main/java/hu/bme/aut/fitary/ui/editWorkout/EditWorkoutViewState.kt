package hu.bme.aut.fitary.ui.editWorkout

sealed class EditWorkoutViewState

object Loading : EditWorkoutViewState()

object Saving : EditWorkoutViewState()

data class Editing(
    val exercises: List<EditWorkoutPresenter.Exercise>,
    val score: Double = 0.0,
    val title: String? = null
) : EditWorkoutViewState()