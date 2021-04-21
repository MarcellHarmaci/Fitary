package hu.bme.aut.fitary.ui.createWorkout

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.fitary.ui.createWorkout.dialog.*
import javax.inject.Inject

class CreateWorkoutViewModel @Inject constructor(
    private val createWorkoutPresenter: CreateWorkoutPresenter
) : RainbowCakeViewModel<CreateWorkoutViewState>(Loading), ResultHandler {

    var comment: String? = null
    val exercises = mutableListOf<CreateWorkoutPresenter.Exercise>()

    private var addExerciseDialogHandler: AddExerciseDialogHandler? = null
    fun setAddExerciseDialogHandler(handler: AddExerciseDialogHandler) {
        addExerciseDialogHandler = handler
    }

    private var editExerciseDialogHandler: EditExerciseDialogHandler? = null
    fun setEditExerciseDialogHandler(handler: EditExerciseDialogHandler) {
        editExerciseDialogHandler = handler
    }

    fun createAddExerciseDialog() = execute {
        val exerciseNames = createWorkoutPresenter.getExerciseNames()
        val exerciseScores = createWorkoutPresenter.getExerciseScores()

        val dialog = AddExerciseDialog(exerciseNames, exerciseScores)
        dialog.setResultHandler(this)

        addExerciseDialogHandler?.onAddExerciseDialogReady(dialog)
    }

    fun createEditExerciseDialog(position: Int) = execute {
        val exercise = exercises[position]

        val dialog = EditExerciseDialog(exercise, position)
        dialog.setResultHandler(this)

        editExerciseDialogHandler?.onEditExerciseDialogReady(dialog)
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