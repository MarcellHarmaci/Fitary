package hu.bme.aut.fitary.ui.editWorkout

import android.view.MenuItem
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.extensions.sumOfScores
import hu.bme.aut.fitary.ui.editWorkout.dialog.*
import java.util.*
import javax.inject.Inject

class EditWorkoutViewModel @Inject constructor(
    private val presenter: EditWorkoutPresenter
) : RainbowCakeViewModel<EditWorkoutViewState>(Loading),
    ResultHandler, OnSuccessListener<Void>, OnFailureListener {

    fun loadWorkout(openedWithPurpose: Int?, id: String?) = execute {

        if (openedWithPurpose == EditWorkoutFragment.Purpose.CREATE_WORKOUT
            || openedWithPurpose == null
            || id == null
        ) {
            viewState = Editing()
        } else {
            val workout = presenter.loadWorkout(id)

            workout?.let {
                viewState = Editing(
                    id = when (openedWithPurpose) {
                        EditWorkoutFragment.Purpose.EDIT_WORKOUT -> it.id
                        else -> null
                    },
                    exercises = it.exercises.toMutableList(),
                    score = it.score.toDouble(),
                    title = it.title
                )
            } ?: run {
                viewState = Editing()
            }
        }
    }

    interface SavingWorkoutFinishedHandler {
        fun onSaveFinished(isSuccessful: Boolean)
    }

    private var saveFinishedHandler: SavingWorkoutFinishedHandler? = null
    fun setSaveFinishedHandler(handler: SavingWorkoutFinishedHandler) {
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
        val exercise = (viewState as Editing).exercises[position]

        val dialog = EditExerciseDialog(exercise, position)
        dialog.setResultHandler(this)

        editExerciseDialogHandler?.onEditExerciseDialogReady(dialog)
    }

    override fun onAddDialogResult(exercise: EditWorkoutPresenter.Exercise) = execute {
        val oldState = (viewState as Editing)
        val exercises = oldState.exercises.toMutableList()

        exercises.add(exercise)

        viewState = oldState.versionedCopy(
            exercises = exercises,
            score = exercises.sumOfScores()
        )
    }

    override fun onEditDialogResult(
        exercise: EditWorkoutPresenter.Exercise,
        position: Int
    ) = execute {
        val oldState = (viewState as Editing)
        val exercises = oldState.exercises.toMutableList()

        exercises[position] = exercise

        viewState = oldState.versionedCopy(
            exercises = exercises,
            score = exercises.sumOfScores()
        )
    }

    fun validateForm(): Boolean {
        return (viewState as Editing).exercises.isNotEmpty()
    }

    fun saveWorkout() = execute {
        val newState = (viewState as Editing).toSaving()
        viewState = newState

        presenter.saveWorkout(
            id = newState.id,
            exercises = newState.exercises,
            title = newState.title,
            onSuccessListener = this,
            onFailureListener = this
        )
    }

    override fun onSuccess(void: Void?) {
        saveFinishedHandler?.onSaveFinished(true)
    }

    override fun onFailure(exception: Exception) {
        viewState = (viewState as Saving).toEditing()
        saveFinishedHandler?.onSaveFinished(false)
    }

    fun onPopupItemSelected(item: MenuItem) = execute {
        val position = item.intent.getIntExtra("position", 0)
        val exercises = (viewState as Editing).exercises.toMutableList()

        when (item.itemId) {
            R.id.item_duplicate_exercise -> {
                val duplicate = exercises[position].copy()
                exercises.add(position + 1, duplicate)
            }
            R.id.item_delete_exercise -> {
                exercises.removeAt(position)
            }
            else -> return@execute
        }

        viewState = (viewState as Editing).versionedCopy(
            exercises = exercises,
            score = exercises.sumOfScores()
        )
    }

    fun swapExercises(from: Int, to: Int) = execute {
        // Update view state's inner property without incrementing version.
        // Re-rendering drops the currently dragged item
        Collections.swap((viewState as Editing).exercises, from, to)
    }

    fun setTitle(newTitle: String) {
        // Update view state's inner property without incrementing version.
        // Re-rendering moves EditText selection
        (viewState as Editing).title = newTitle
    }

}
