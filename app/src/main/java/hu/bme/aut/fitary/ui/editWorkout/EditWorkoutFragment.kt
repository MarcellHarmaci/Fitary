package hu.bme.aut.fitary.ui.editWorkout

import android.app.ProgressDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.fitary.MainActivity
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.editWorkout.adapter.ExerciseListAdapter
import hu.bme.aut.fitary.ui.editWorkout.dialog.AddExerciseDialogHandler
import hu.bme.aut.fitary.ui.editWorkout.helper.DragAndDropCallback
import kotlinx.android.synthetic.main.fragment_edit_or_create_workout.*

// TODO Hide keyboard on toolbar back pressed
class EditWorkoutFragment :
    RainbowCakeFragment<EditWorkoutViewState, EditWorkoutViewModel>(),
    AddExerciseDialogHandler,
    EditWorkoutViewModel.SavingWorkoutFinishedHandler,
    PopupMenu.OnMenuItemClickListener {

    object Purpose {
        const val CREATE_WORKOUT = 10000
        const val EDIT_WORKOUT = 10001
        const val COPY_WORKOUT = 10002
    }

    private lateinit var exerciseAdapter: ExerciseListAdapter
    private var progressDialog: ProgressDialog? = null

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_edit_or_create_workout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val purpose = arguments?.getInt("purpose")
        val workoutId = arguments?.getString("workout_id")
        viewModel.loadWorkout(purpose, workoutId)
    }

    override fun onStart() {
        super.onStart()

        viewModel.setAddExerciseDialogHandler(this)
        viewModel.setSaveFinishedHandler(this)

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

        etTitle.doOnTextChanged { text, _, _, _ ->
            viewModel.setTitle(text.toString())
        }
    }

    override fun render(viewState: EditWorkoutViewState) {
        when (viewState) {
            is Loading -> {
                hideProgressDialog()
            }
            is Editing -> {
                hideProgressDialog()

                // Weird behaviour here!
                // Editing exercises doesn't update the UI unless notifyDataSetChanged() is called.
                // Explained in more depth here: https://stackoverflow.com/a/50031492/10658813
                exerciseAdapter.submitList(viewState.exercises)
                exerciseAdapter.notifyDataSetChanged()

                etTitle.setText(viewState.title)
                etTitle.setSelection(etTitle.length())
            }
            is Saving -> {
                showProgressDialog()
            }
        }.exhaustive
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseAdapter = ExerciseListAdapter(viewModel, this)
        rvExercises.adapter = exerciseAdapter
        rvExercises.layoutManager = LinearLayoutManager(view.context)

        val dndCallback = DragAndDropCallback(exerciseAdapter)
        val itemTouchHelper = ItemTouchHelper(dndCallback)
        itemTouchHelper.attachToRecyclerView(rvExercises)
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
        when (isSuccessful) {
            true -> {
                hideProgressDialog()
                (activity as MainActivity).onSupportNavigateUp()
            }
            false -> {
                val message = "Couldn't save workout\nTry again later"
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId !in setOf(R.id.item_duplicate_exercise, R.id.item_delete_exercise)) {
            return false
        }

        viewModel.onPopupItemSelected(item)
        return true
    }

    fun getListItemPosition(view: View) = rvExercises.getChildAdapterPosition(view)

}