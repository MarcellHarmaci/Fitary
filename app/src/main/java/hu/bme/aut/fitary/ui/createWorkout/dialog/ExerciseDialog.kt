package hu.bme.aut.fitary.ui.exerciseDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.domainModel.DomainExercise
import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutActivity
import kotlinx.android.synthetic.main.fragment_dialog_exercise.view.*

class ExerciseDialog(
    val position: Int,
    var domainExercise: DomainExercise
) : DialogFragment() {

    private lateinit var resultHandler: ExerciseResultHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resultHandler = activity as CreateWorkoutActivity

        val dialogLayout =
            inflater.inflate(R.layout.fragment_dialog_exercise, container, false)
        dialogLayout.etExerciseName.setText(domainExercise.name)
        dialogLayout.etReps.setText(domainExercise.reps.toString())

        dialogLayout.btnSave.setOnClickListener {
            // TODO Replace magic numbers with symbolic constant
            if (position == -1)
                resultHandler.onSuccessAddExercise(
                    DomainExercise(
                        0L // TODO Fetch real values
//                        dialogLayout.etExerciseName.text.toString(),
//                        dialogLayout.etReps.text.toString().toInt()
                    )
                )
            else
                resultHandler.onSuccessEditExercise(
                    position,
                    DomainExercise(
                        0L // TODO Fetch real values
//                        dialogLayout.etExerciseName.text.toString(),
//                        dialogLayout.etReps.text.toString().toInt()
                    )
                )
            dismiss()
        }
        dialogLayout.btnCancel.setOnClickListener {
            dismiss()
        }

        return dialogLayout
    }

    interface ExerciseResultHandler {
        fun onSuccessAddExercise(domainExercise: DomainExercise)
        fun onSuccessEditExercise(position: Int, domainExercise: DomainExercise)
    }
}