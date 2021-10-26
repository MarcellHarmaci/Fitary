package hu.bme.aut.fitary.ui.editWorkout.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.editWorkout.EditWorkoutPresenter
import kotlinx.android.synthetic.main.dialog_edit_exercise.*
import kotlinx.android.synthetic.main.dialog_edit_exercise.view.*

class EditExerciseDialog(
    private var exercise: EditWorkoutPresenter.Exercise,
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

        dialogLayout.tvName.text = exercise.name
        dialogLayout.etReps.setText(exercise.reps.toString())
        dialogLayout.tvScore.text = exercise.score.toString()

        dialogLayout.etReps.doOnTextChanged { currentText, _, _, _ ->
            if (currentText.isNullOrBlank())
                exercise.reps = 0
            else
                exercise.reps = currentText.toString().toInt()

            // Update displayed score
            dialogLayout.tvScore.text = exercise.score.toString()
        }

        dialogLayout.etReps.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btnModify.callOnClick()
                true
            }
            else false
        }

        dialogLayout.btnModify.setOnClickListener {
            if (validateForm()) {
                resultHandler?.onEditDialogResult(exercise, position)

                dismissAllowingStateLoss()
            }
            else {
                val message = "You should do at least 1 repetition"
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }

        dialogLayout.btnCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }

        return dialogLayout
    }

    private fun validateForm(): Boolean {
        return !etReps.text.isNullOrBlank() && etReps.text.toString().toInt() != 0
    }

}