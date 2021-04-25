package hu.bme.aut.fitary.ui.createWorkout.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutPresenter
import kotlinx.android.synthetic.main.dialog_add_exercise.*
import kotlinx.android.synthetic.main.dialog_add_exercise.view.*


class AddExerciseDialog(
    private val exerciseNames: List<String>,
    private val exerciseScores: Map<String, Double>
) : DialogFragment(), AdapterView.OnItemSelectedListener {

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

        val spinner = dialogLayout.spinner
        val adapter = context?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, exerciseNames)
        }

        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

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
                btnSave.callOnClick()
                true
            }
            else false
        }

        dialogLayout.btnSave.setOnClickListener {
            if (validateForm()) {
                resultHandler?.onAddDialogResult(exercise)

                dismissAllowingStateLoss()
            } else {
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val newName = exerciseNames[position]

        exercise.name = newName
        exercise.scorePerRep = exerciseScores[newName]!!    // TODO handle exception

        tvScore.text = exercise.score.toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // TODO Implement method
    }

}