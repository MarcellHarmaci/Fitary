package hu.bme.aut.fitary.ui.createWorkout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutPresenter
import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutViewModel
import hu.bme.aut.fitary.ui.createWorkout.dialog.EditExerciseDialogHandler
import kotlinx.android.synthetic.main.list_item_exercise.view.*

class ExerciseListAdapter(
    private val createWorkoutViewModel: CreateWorkoutViewModel,
    private val fragmentManager: FragmentManager
) : ListAdapter<CreateWorkoutPresenter.Exercise, ExerciseListAdapter.ExerciseViewHolder>(
    ExerciseComparator
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_exercise, parent, false)

        return ExerciseViewHolder(view, createWorkoutViewModel, fragmentManager)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)
    }

    inner class ExerciseViewHolder(
        itemView: View,
        private val createWorkoutViewModel: CreateWorkoutViewModel,
        private val fragmentManager: FragmentManager
    ) : RecyclerView.ViewHolder(itemView), EditExerciseDialogHandler {

        private val tvName = itemView.tvExerciseName
        private val tvReps = itemView.tvReps
        private val tvScore = itemView.tvScore

        init {
            createWorkoutViewModel.setEditExerciseDialogHandler(this)

            itemView.setOnClickListener {
                createWorkoutViewModel.createEditExerciseDialog(layoutPosition)
            }

            // Set OnCreateContextMenuListener
            val createWorkoutFragment = fragmentManager.primaryNavigationFragment
            createWorkoutFragment?.registerForContextMenu(itemView)
        }

        fun bind(exercise: CreateWorkoutPresenter.Exercise) {
            tvName.text = exercise.name
            tvReps.text = exercise.reps.toString()
            tvScore.text = exercise.score.toString()
        }

        override fun onEditExerciseDialogReady(dialog: DialogFragment) {
            dialog.show(fragmentManager, "Edit exercise")
        }

    }
}