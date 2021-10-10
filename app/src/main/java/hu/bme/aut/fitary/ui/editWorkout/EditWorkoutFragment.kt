package hu.bme.aut.fitary.ui.editWorkout

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.iterator
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.fitary.MainActivity
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.editWorkout.adapter.ExerciseListAdapter
import hu.bme.aut.fitary.ui.editWorkout.dialog.AddExerciseDialogHandler
import kotlinx.android.synthetic.main.fragment_edit_or_create_workout.*


class EditWorkoutFragment :
    RainbowCakeFragment<EditWorkoutViewState, EditWorkoutViewModel>(),
    AddExerciseDialogHandler,
    EditWorkoutViewModel.WorkoutSavingFinishedHandler {

    private lateinit var exerciseAdapter: ExerciseListAdapter
    private var progressDialog: ProgressDialog? = null
    private var workoutId: String? = null

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_edit_or_create_workout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workoutId = arguments?.getString("workout_id")
        workoutId?.let {
            viewModel.loadWorkout(workoutId!!)
        }
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

        etComment.doOnTextChanged { text, _, _, _ ->
            viewModel.comment = text.toString()
        }
    }

    override fun onStop() {
        hideProgressDialog()
        (activity as MainActivity).setFloatingActionButtonVisible(true)

        super.onStop()
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

                etComment.setText(viewState.comment ?: "")
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

        // TODO Extract overridden class
        val simpleCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
                0
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val adapter = (recyclerView.adapter as ExerciseListAdapter)
                    adapter.onItemMoved(
                        from = viewHolder.adapterPosition,
                        to = target.adapterPosition
                    )

                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
            }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
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
            true ->
                (activity as MainActivity).onSupportNavigateUp()
            false -> {
                val message = "Couldn't save workout\nTry again later"
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        view: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, view, menuInfo)

        val inflater = MenuInflater(context)
        inflater.inflate(R.menu.exercise_context_menu, menu)

        val position = rvExercises.getChildAdapterPosition(view)
        val posIntent = Intent().putExtra("position", position)

        menu.iterator().forEach {
            it.intent = posIntent
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = item.intent.getIntExtra("position", 0)
        viewModel.onContextItemSelected(item, position)
        return true
    }

}