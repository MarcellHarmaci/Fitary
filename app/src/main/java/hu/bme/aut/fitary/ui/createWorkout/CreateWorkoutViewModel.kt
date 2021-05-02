package hu.bme.aut.fitary.ui.createWorkout

import androidx.lifecycle.MutableLiveData
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.fitary.ui.createWorkout.dialog.*
import javax.inject.Inject

class CreateWorkoutViewModel @Inject constructor(
    private val createWorkoutPresenter: CreateWorkoutPresenter
) : RainbowCakeViewModel<CreateWorkoutViewState>(Loading), ResultHandler {

    var comment: String? = null
    private val exercises = mutableListOf<CreateWorkoutPresenter.Exercise>()
    val exercisesLiveData = MutableLiveData<MutableList<CreateWorkoutPresenter.Exercise>>()

    interface WorkoutSavingFinishedHandler {
        fun onSaveFinished(isSuccessful: Boolean)
    }

    private var saveFinishedHandler: WorkoutSavingFinishedHandler? = null
    fun setSaveFinishedHandler(handler: WorkoutSavingFinishedHandler) {
        saveFinishedHandler = handler
    }

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
        exercisesLiveData.value = exercises
    }

    override fun onEditDialogResult(exercise: CreateWorkoutPresenter.Exercise, position: Int) {
        exercises[position] = exercise
        exercisesLiveData.value = exercises
    }

    fun saveWorkout() = execute {
        viewState = SavingWorkout

        var isSaveSuccessful = false

        // TODO Inspect how this behaves
        //  Wait till workout is saved (Async-await pattern?)
        isSaveSuccessful = createWorkoutPresenter.saveWorkout(exercises, comment)

        if (isSaveSuccessful) {
            saveFinishedHandler?.onSaveFinished(true)
        }
        else {
            viewState = WorkoutCreationInProgress(exercises, comment)
            saveFinishedHandler?.onSaveFinished(false)
        }

    }

    fun validateForm(): Boolean {
        return exercises.isNotEmpty()
    }

}