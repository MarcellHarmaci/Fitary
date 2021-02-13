package hu.bme.aut.fitary.ui.exerciseDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.zsmb.rainbowcake.base.RainbowCakeDialogFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutActivity
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.data.DomainExercise
import kotlinx.android.synthetic.main.fragment_dialog_exercise.*
import kotlinx.android.synthetic.main.fragment_dialog_exercise.view.*

class ExerciseDialog(
    val position: Int,
    var domainExercise: DomainExercise
) : RainbowCakeDialogFragment<ExerciseDialogViewState, ExerciseDialogViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_dialog_exercise

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO Setup views
        //  Ask Krisztián what exactly should be done here!
        //  Should I just call render?
    }

    override fun onStart() {
        super.onStart()

        viewModel.loadExercise()
        // TODO load if the current operation is Add or Edit
    }

    override fun render(viewState: ExerciseDialogViewState) {
        when (viewState) {
            is Loading -> {
                // TODO Show that the view is loading
            }
            is ExerciseDialogReady -> {
                etExerciseName.setText(viewState.exerciseName)
                etReps.setText(viewState.reps)
            }
        }.exhaustive
    }

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
                        dialogLayout.etExerciseName.text.toString(),
                        dialogLayout.etReps.text.toString().toInt()
                    )
                )
            else
                resultHandler.onSuccessEditExercise(
                    position,
                    DomainExercise(
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
        fun onSuccessAddExercise(domainExercise: DomainExercise)
        fun onSuccessEditExercise(position: Int, domainExercise: DomainExercise)
    }
}