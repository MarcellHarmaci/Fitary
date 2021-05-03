package hu.bme.aut.fitary.ui.createWorkout

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.fitary.MainActivity
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.createWorkout.adapter.ExerciseListAdapter
import hu.bme.aut.fitary.ui.createWorkout.dialog.AddExerciseDialogHandler
import kotlinx.android.synthetic.main.fragment_create_workout.*

class CreateWorkoutFragment :
    RainbowCakeFragment<CreateWorkoutViewState, CreateWorkoutViewModel>(),
    AddExerciseDialogHandler,
    CreateWorkoutViewModel.WorkoutSavingFinishedHandler {

    private lateinit var exerciseAdapter: ExerciseListAdapter
    private var progressDialog: ProgressDialog? = null

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_create_workout

    override fun onStart() {
        super.onStart()

        viewModel.setAddExerciseDialogHandler(this)
        viewModel.setSaveFinishedHandler(this)
        viewModel.exercisesLiveData.observe(this, Observer {
            it?.let {
                exerciseAdapter.submitList(it)

                // Weird behaviour here!
                // Editing exercises doesn't update the UI unless notifyDataSetChanged() is called.
                // Explained in more depth here: https://stackoverflow.com/a/50031492/10658813
                exerciseAdapter.notifyDataSetChanged()
            }
        })

        btnAddExercise.setOnClickListener {
            viewModel.createAddExerciseDialog()
        }

        btnSaveWorkout.setOnClickListener {
            if (viewModel.validateForm()) {
                viewModel.saveWorkout()
            } else {
                val message = "Add at least 1 exercise to save the workout"
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }

        etComment.doOnTextChanged { text, _, _, _ ->
            viewModel.comment = text.toString()
        }
    }

    override fun onStop() {
        hideProgressDialog()
        (activity as MainActivity).setFloatingActionButtonVisible(true)

        super.onStop()
    }

    override fun render(viewState: CreateWorkoutViewState) {
        when (viewState) {
            is Loading -> {
                hideProgressDialog()
            }
            is WorkoutCreationInProgress -> {
                hideProgressDialog()

                exerciseAdapter.submitList(viewState.exercises)
                etComment.setText(viewState.comment ?: "")
            }
            is SavingWorkout -> {
                showProgressDialog()
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
    }

    private fun showProgressDialog() {
        if (progressDialog != null) {
            return
        }

        progressDialog = ProgressDialog(context).apply {
            setCancelable(false)
            setMessage("Saving workout...")
            show()
        }
    }

    private fun hideProgressDialog() {
        progressDialog?.let { dialog ->
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
        progressDialog = null
    }

    override fun onSaveFinished(isSuccessful: Boolean) {
        when(isSuccessful) {
            true ->
                parentFragmentManager.popBackStack()
            false -> {
                val message = "Couldn't save workout\nTry again later"
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

}