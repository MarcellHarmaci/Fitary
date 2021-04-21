package hu.bme.aut.fitary.ui.createWorkout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.base.ViewModelScope
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.createWorkout.adapter.ExerciseListAdapter
import hu.bme.aut.fitary.ui.createWorkout.dialog.AddExerciseDialogHandler
import kotlinx.android.synthetic.main.fragment_create_workout.*

class CreateWorkoutFragment :
    RainbowCakeFragment<CreateWorkoutViewState, CreateWorkoutViewModel>(),
    AddExerciseDialogHandler {

    private lateinit var exerciseAdapter: ExerciseListAdapter

    // VM Scope in bound to MainActivity to keep VM when this Fragment is destroyed
    // TODO Correct scope
    override fun provideViewModel() = getViewModelFromFactory(scope = ViewModelScope.Activity)
    override fun getViewResource() = R.layout.fragment_create_workout

    override fun onStart() {
        super.onStart()

        viewModel.setAddExerciseDialogHandler(this)

        btnAddExercise.setOnClickListener {
            viewModel.createAddExerciseDialog()
        }
        btnSaveWorkout.setOnClickListener {
            // TODO validate view state
            viewModel.saveWorkout()
        }
    }

    override fun render(viewState: CreateWorkoutViewState) {
        when (viewState) {
            is Loading -> {
                //pbListLoading.visibility = View.VISIBLE
            }
            is CreateWorkoutLoaded -> {
                //pbListLoading.visibility = View.GONE

                exerciseAdapter.submitList(viewState.exercises)
            }
            is SavingWorkout -> {

            }
        }.exhaustive
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseAdapter = ExerciseListAdapter(viewModel, parentFragmentManager)
        rvExercises.adapter = exerciseAdapter
        rvExercises.layoutManager = LinearLayoutManager(view.context)
    }

    override fun onAddExerciseDialogReady(dialog: DialogFragment) {
        dialog.show(parentFragmentManager, "Add exercise")

        // TODO Make exercise list some LiveData or Channel
        //  so Fragment can be notified on state changes
        exerciseAdapter.submitList(viewModel.exercises)
    }

}