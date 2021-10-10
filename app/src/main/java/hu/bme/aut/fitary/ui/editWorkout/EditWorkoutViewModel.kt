package hu.bme.aut.fitary.ui.editWorkout

import android.view.MenuItem
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.extensions.sumOfScores
import hu.bme.aut.fitary.ui.editWorkout.dialog.*
import javax.inject.Inject

class EditWorkoutViewModel @Inject constructor(
    private val presenter: EditWorkoutPresenter
) : RainbowCakeViewModel<EditWorkoutViewState>(Loading),
    ResultHandler, OnSuccessListener<Void>, OnFailureListener {

    var comment: String? = null
    private var exercises = mutableListOf<EditWorkoutPresenter.Exercise>()
    private var workoutId: String? = null

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
        val exerciseNames = presenter.getExerciseNames()
        val exerciseScores = presenter.getExerciseScores()

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

    override fun onAddDialogResult(exercise: EditWorkoutPresenter.Exercise) = execute {
        exercises.plusAssign(exercise)
        updateViewStateWithCurrentExercises()
    }

    override fun onEditDialogResult(
        exercise: EditWorkoutPresenter.Exercise,
        position: Int
    ) = execute {
        exercises[position] = exercise
        updateViewStateWithCurrentExercises()
    }

    fun validateForm(): Boolean {
        return exercises.isNotEmpty()
    }

    fun saveWorkout() = execute {
        viewState = Saving

        presenter.saveWorkout(workoutId, exercises, comment, this, this)
    }

    override fun onSuccess(void: Void?) {
        saveFinishedHandler?.onSaveFinished(true)
    }

    override fun onFailure(exception: Exception) {
        viewState = Editing(
            exercises = exercises,
            comment = comment
        )
        saveFinishedHandler?.onSaveFinished(false)
    }

    fun onContextItemSelected(item: MenuItem, position: Int) = execute {
        when (item.itemId) {
            R.id.context_item_duplicate_exercise -> {
                val duplicate = exercises[position].copy()
                exercises.add(position + 1, duplicate)
                updateViewStateWithCurrentExercises()
            }
            R.id.context_item_delete_exercise -> {
                exercises.removeAt(position)
                updateViewStateWithCurrentExercises()
            }
        }
    }

    private fun updateViewStateWithCurrentExercises() = execute {
        if (viewState is Editing) {
            val oldState = viewState as Editing

            viewState = oldState.copy(
                exercises = exercises,
                score = exercises.sumOfScores(),
                comment = oldState.comment
            )
        }
    }

    fun loadWorkout(id: String) = execute {
        val workout = presenter.loadWorkout(id)

        workout?.let {
            workoutId = it.id
            exercises = it.exercises.toMutableList()

            viewState = Editing(
                exercises = it.exercises,
                score = it.score.toDouble(),
                comment = it.comment
            )
        }
    }

}