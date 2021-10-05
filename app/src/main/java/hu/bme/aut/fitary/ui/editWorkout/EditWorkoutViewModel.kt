package hu.bme.aut.fitary.ui.editWorkout

import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.editWorkout.dialog.*
import javax.inject.Inject

class EditWorkoutViewModel @Inject constructor(
    private val presenter: EditWorkoutPresenter
) : RainbowCakeViewModel<EditWorkoutViewState>(Loading),
    ResultHandler, OnSuccessListener<Void>, OnFailureListener {

    var comment: String? = null
    private val exercises = mutableListOf<EditWorkoutPresenter.Exercise>()
    val exercisesLiveData = MutableLiveData<MutableList<EditWorkoutPresenter.Exercise>>()

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

    override fun onAddDialogResult(exercise: EditWorkoutPresenter.Exercise) {
        exercises += exercise
        exercisesLiveData.value = exercises
    }

    override fun onEditDialogResult(exercise: EditWorkoutPresenter.Exercise, position: Int) {
        exercises[position] = exercise
        exercisesLiveData.value = exercises
    }

    fun validateForm(): Boolean {
        return exercises.isNotEmpty()
    }

    fun saveWorkout() = execute {
        viewState = Saving

        presenter.saveWorkout(exercises, comment, this, this)
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
                exercisesLiveData.value = exercises
            }
            R.id.context_item_delete_exercise -> {
                exercises.removeAt(position)
                exercisesLiveData.value = exercises
            }
        }
    }

    fun loadWorkout(workoutId: String) = execute {
        val workout = presenter.loadWorkout(workoutId)

        workout?.let {
            viewState = Editing(
                exercises = it.exercises,
                score = it.score.toDouble(),
                comment = it.comment
            )
        }
    }

}