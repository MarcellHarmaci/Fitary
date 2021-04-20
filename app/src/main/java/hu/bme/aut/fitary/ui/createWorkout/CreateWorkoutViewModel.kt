package hu.bme.aut.fitary.ui.createWorkout

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.fitary.ui.createWorkout.dialog.AddExerciseDialog
import hu.bme.aut.fitary.ui.createWorkout.dialog.EditExerciseDialog
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

    fun addExercise() = execute {
        val exerciseNames = createWorkoutPresenter.getExerciseNames()
        val exerciseScores = createWorkoutPresenter.getExerciseScores()

        val dialog = AddExerciseDialog(exerciseNames, exerciseScores)
        dialog.setResultHandler(this)

        // TODO Show dialog
    }

    fun editExercise(position: Int) = execute {
        val exercise = exercises[position]

        val dialog = EditExerciseDialog(exercise, position)
        dialog.setResultHandler(this)

        // TODO Show a dialog
    }

    override fun onAddDialogResult(exercise: CreateWorkoutPresenter.Exercise) {
        exercises += exercise
    }

    override fun onEditDialogResult(exercise: CreateWorkoutPresenter.Exercise, position: Int) {
        exercises[position] = exercise
    }

    fun saveWorkout() = execute {
        viewState = SavingWorkout

        TODO("Not yet implemented")
    }

}