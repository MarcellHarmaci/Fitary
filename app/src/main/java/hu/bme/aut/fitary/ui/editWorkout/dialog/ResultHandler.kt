package hu.bme.aut.fitary.ui.editWorkout.dialog

import hu.bme.aut.fitary.ui.editWorkout.EditWorkoutPresenter

interface ResultHandler {
    fun onAddDialogResult(exercise: EditWorkoutPresenter.Exercise)
    fun onEditDialogResult(exercise: EditWorkoutPresenter.Exercise, position: Int)
}