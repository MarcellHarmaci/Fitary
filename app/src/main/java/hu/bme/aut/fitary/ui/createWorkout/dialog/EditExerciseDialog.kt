package hu.bme.aut.fitary.ui.createWorkout.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutPresenter
import kotlinx.android.synthetic.main.dialog_edit_exercise.view.*

class EditExerciseDialog(
    var exercise: CreateWorkoutPresenter.Exercise,
    val position: Int,
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

        dialogLayout.btnSave.setOnClickListener {
            resultHandler?.onEditDialogResult(
                // TODO Fetch real values
                CreateWorkoutPresenter.Exercise(
//                    dialogLayout.etExerciseName.text.toString(),
//                    dialogLayout.etReps.text.toString().toInt()
                ),
                position
            )
            dismissAllowingStateLoss()
        }

        dialogLayout.btnCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }

        return dialogLayout
    }

}