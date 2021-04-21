package hu.bme.aut.fitary.ui.createWorkout

import androidx.fragment.app.DialogFragment
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.fitary.ui.createWorkout.dialog.AddExerciseDialog
import hu.bme.aut.fitary.ui.createWorkout.dialog.EditExerciseDialog
import hu.bme.aut.fitary.ui.createWorkout.dialog.ResultHandler
import javax.inject.Inject

class CreateWorkoutViewModel @Inject constructor(
    private val createWorkoutPresenter: CreateWorkoutPresenter
) : RainbowCakeViewModel<CreateWorkoutViewState>(Loading), ResultHandler {

    interface AddExerciseDialogHandler {
        fun onDialogReadyToBeShowed(dialog: DialogFragment)
    }

    private var addExerciseDialogHandler: AddExerciseDialogHandler? = null
    fun setDialogHandler(handler: AddExerciseDialogHandler) {
        addExerciseDialogHandler = handler
    }

    var comment: String? = null
    val exercises = mutableListOf<CreateWorkoutPresenter.Exercise>()

    fun createAddExerciseDialog() = execute {
        val exerciseNames = createWorkoutPresenter.getExerciseNames()
        val exerciseScores = createWorkoutPresenter.getExerciseScores()

        val dialog = AddExerciseDialog(exerciseNames, exerciseScores)
        dialog.setResultHandler(this)

        addExerciseDialogHandler?.onDialogReadyToBeShowed(dialog)
    }

    fun createEditExerciseDialog(position: Int): EditExerciseDialog? {
        var dialog: EditExerciseDialog? = null
        execute {
            val exercise = exercises[position]

            dialog = EditExerciseDialog(exercise, position)
            dialog?.setResultHandler(this)
        }

        return dialog
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