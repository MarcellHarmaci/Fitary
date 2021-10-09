package hu.bme.aut.fitary.ui.editWorkout.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.editWorkout.EditWorkoutPresenter
import hu.bme.aut.fitary.ui.editWorkout.EditWorkoutViewModel
import hu.bme.aut.fitary.ui.editWorkout.dialog.EditExerciseDialogHandler
import kotlinx.android.synthetic.main.list_item_exercise.view.*

class ExerciseListAdapter(
    private val editWorkoutViewModel: EditWorkoutViewModel,
    private val fragmentManager: FragmentManager
) : ListAdapter<EditWorkoutPresenter.Exercise, ExerciseListAdapter.ExerciseViewHolder>(
    ExerciseComparator
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_exercise, parent, false)

        return ExerciseViewHolder(view, editWorkoutViewModel, fragmentManager)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class ExerciseViewHolder(
        itemView: View,
        private val editWorkoutViewModel: EditWorkoutViewModel,
        private val fragmentManager: FragmentManager
    ) : RecyclerView.ViewHolder(itemView), EditExerciseDialogHandler {

        private val tvName = itemView.tvExerciseName
        private val tvReps = itemView.tvReps
        private val tvScore = itemView.tvScore

        init {
            editWorkoutViewModel.setEditExerciseDialogHandler(this)

            itemView.setOnClickListener {
                editWorkoutViewModel.createEditExerciseDialog(layoutPosition)
            }

            itemView.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        itemView.setBackgroundColor(Color.LTGRAY)
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_OUTSIDE, MotionEvent.ACTION_CANCEL -> {
                        itemView.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
                false
            }
            // Set OnCreateContextMenuListener
            val createWorkoutFragment = fragmentManager.primaryNavigationFragment
            createWorkoutFragment?.registerForContextMenu(itemView)
        }

        fun bind(exercise: EditWorkoutPresenter.Exercise) {
            tvName.text = exercise.name
            tvReps.text = exercise.reps.toString()
            tvScore.text = exercise.score.toString()
        }

        override fun onEditExerciseDialogReady(dialog: DialogFragment) {
            dialog.show(fragmentManager, "Edit exercise")
        }

    }
}