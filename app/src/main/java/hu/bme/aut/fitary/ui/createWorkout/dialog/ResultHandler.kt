package hu.bme.aut.fitary.ui.createWorkout.dialog

import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutPresenter

interface ResultHandler {
    fun onAddDialogResult(exercise: CreateWorkoutPresenter.Exercise)
    fun onEditDialogResult(exercise: CreateWorkoutPresenter.Exercise, position: Int)
}