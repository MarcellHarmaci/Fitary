package hu.bme.aut.fitary.ui.createWorkout

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.fitary.ui.createWorkout.dialog.ExerciseDialog
import hu.bme.aut.fitary.ui.createWorkout.dialog.ResultHandler
import javax.inject.Inject

class CreateWorkoutViewModel @Inject constructor(
    private val createWorkoutPresenter: CreateWorkoutPresenter
) : RainbowCakeViewModel<CreateWorkoutViewState>(Loading), ResultHandler {

    var comment: String? = null
    val exercises = mutableListOf<CreateWorkoutPresenter.Exercise>()

    fun load() = execute {
        TODO("Not yet implemented")
    }

    fun saveWorkout() = execute {
        viewState = SavingWorkout

        TODO("Not yet implemented")
    }

    // TODO Show dialog to create a new exercise and
    fun addExercise() = execute {
        val exercise = CreateWorkoutPresenter.Exercise() // Replace with dialog result

        val dialog = ExerciseDialog(exercise)
        dialog.setResultHandler(this)

        exercises += exercise
    }

    // TODO Show a dialog to edit this exercise
    fun editExercise(position: Int) {
        val exercise = exercises[position]

        val dialog = ExerciseDialog(exercise, position)
        dialog.setResultHandler(this)

        TODO("Not yet implemented")
    }

    override fun onAddDialogResult(exercise: CreateWorkoutPresenter.Exercise) {
        exercises += exercise
    }

    override fun onEditDialogResult(exercise: CreateWorkoutPresenter.Exercise, position: Int) {
        exercises[position] = exercise
    }


}