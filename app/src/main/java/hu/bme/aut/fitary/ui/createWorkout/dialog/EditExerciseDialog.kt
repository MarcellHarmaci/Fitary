package hu.bme.aut.fitary.ui.createWorkout.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutPresenter
import kotlinx.android.synthetic.main.dialog_edit_exercise.view.*

class EditExerciseDialog(
    private var exercise: CreateWorkoutPresenter.Exercise,
    private val position: Int,
) : DialogFragment() {

    private var resultHandler: ResultHandler? = null

    fun setResultHandler(handler: ResultHandler) {
        resultHandler = handler
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val dialogLayout = inflater.inflate(
            R.layout.dialog_edit_exercise,
            container,
            false
        )

        dialogLayout.etReps.doOnTextChanged { currentText, _, _, _ ->
            exercise.reps = currentText.toString().toInt()

            // Update displayed score
            dialogLayout.tvScore.text = exercise.score.toString()
        }

        dialogLayout.btnSave.setOnClickListener {
            resultHandler?.onEditDialogResult(exercise, position)

            dismissAllowingStateLoss()
        }

        dialogLayout.btnCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }

        return dialogLayout
    }

}