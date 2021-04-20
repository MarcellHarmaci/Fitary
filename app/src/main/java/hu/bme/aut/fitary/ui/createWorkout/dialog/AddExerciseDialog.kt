package hu.bme.aut.fitary.ui.createWorkout.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutPresenter
import kotlinx.android.synthetic.main.dialog_add_exercise.view.*

class AddExerciseDialog(
    private val exerciseNames: List<String>,
    private val exerciseScores: Map<String, Double>
) : DialogFragment(), AdapterView.OnItemClickListener {

    private var resultHandler: ResultHandler? = null
    private var exercise = CreateWorkoutPresenter.Exercise(
        name = exerciseNames[0],
        reps = 0,
        scorePerRep = exerciseScores[exerciseNames[0]]!!    // TODO handle exception
    )

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
        val adapter = context?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, exerciseNames)
        }
        spinner.adapter = adapter
        spinner.onItemClickListener = this

        dialogLayout.etReps.doOnTextChanged { currentText, _, _, _ ->
            exercise.reps = currentText.toString().toInt()

            // Update displayed score
            dialogLayout.tvScore.text = exercise.score.toString()
        }

        dialogLayout.btnSave.setOnClickListener {
            resultHandler?.onAddDialogResult(exercise)

            dismissAllowingStateLoss()
        }

        dialogLayout.btnCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }

        return dialogLayout
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val newName = exerciseNames[position]

        exercise.name = newName
        exercise.scorePerRep = exerciseScores[newName]!!    // TODO handle exception
    }

}