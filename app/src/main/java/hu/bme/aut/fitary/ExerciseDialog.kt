package hu.bme.aut.fitary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import hu.bme.aut.fitary.data.Exercise
import kotlinx.android.synthetic.main.activity_create_workout.view.*
import kotlinx.android.synthetic.main.fragment_dialog_exercise.*
import kotlinx.android.synthetic.main.fragment_dialog_exercise.view.*

class ExerciseDialog(val position: Int, var exercise: Exercise) : DialogFragment() {

    private lateinit var resultHandler: ExerciseResultHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resultHandler = activity as CreateWorkoutActivity

        val dialogLayout = inflater.inflate(R.layout.fragment_dialog_exercise, container, false)
        dialogLayout.etExerciseName.setText(exercise.name)
        dialogLayout.etReps.setText(exercise.reps.toString())

        dialogLayout.btnSave.setOnClickListener {
            if (position == -1)
                resultHandler.onSuccessAddExercise(
                    Exercise(
                        dialogLayout.etExerciseName.text.toString(),
                        dialogLayout.etReps.text.toString().toInt()
                    )
                )
            else
                resultHandler.onSuccessEditExercise(
                    position,
                    Exercise(
                        dialogLayout.etExerciseName.text.toString(),
                        dialogLayout.etReps.text.toString().toInt()
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
        fun onSuccessAddExercise(exercise: Exercise)
        fun onSuccessEditExercise(position: Int, exercise: Exercise)
    }
}