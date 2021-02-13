package hu.bme.aut.fitary.ui.exerciseDialog

sealed class ExerciseDialogViewState

object Loading : ExerciseDialogViewState()

data class ExerciseDialogReady(
    val exerciseName: String = "",
    var reps: Int = 0
) : ExerciseDialogViewState()