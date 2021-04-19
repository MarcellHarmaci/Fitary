package hu.bme.aut.fitary.ui.createWorkout.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutPresenter
import kotlinx.android.synthetic.main.dialog_add_exercise.view.*

class AddExerciseDialog(
    private val exerciseNames: List<String>
) : DialogFragment() {

    private var resultHandler: ResultHandler? = null

    fun setResultHandler(handler: ResultHandler) {
        resultHandler = handler
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val dialogLayout = inflater.inflate(
            R.layout.dialog_add_exercise,
            container,
            false
        )

        // TODO initialize the spinner's adapter
        val spinner = dialogLayout.spinner
//        spinner.onItemClickListener = this

        dialogLayout.btnSave.setOnClickListener {
            resultHandler?.onAddDialogResult(
                // TODO Fetch real values
                CreateWorkoutPresenter.Exercise(
                    name = "",
                    reps = 1,
                    scorePerRep = 1
//                                dialogLayout.etExerciseName.text.toString(),
//                                dialogLayout.etReps.text.toString().toInt()
                )
            )
            dismissAllowingStateLoss()
        }

        dialogLayout.btnCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }

        return dialogLayout
    }

}