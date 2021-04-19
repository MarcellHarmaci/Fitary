package hu.bme.aut.fitary.ui.createWorkout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutPresenter
import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutViewModel
import kotlinx.android.synthetic.main.list_item_exercise.view.*

class ExerciseListAdapter(
    private val createWorkoutViewModel: CreateWorkoutViewModel
) : ListAdapter<CreateWorkoutPresenter.Exercise, ExerciseListAdapter.ExerciseViewHolder>(
        ExerciseComparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_exercise, parent, false)

        return ExerciseViewHolder(createWorkoutViewModel, view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)
    }

    class ExerciseViewHolder(
        createWorkoutViewModel: CreateWorkoutViewModel,
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvName = itemView.tvExerciseName
        private val tvReps = itemView.tvReps
        private val tvScore = itemView.tvScore

        init {
            itemView.setOnClickListener {
                createWorkoutViewModel.editExercise(layoutPosition)
            }
        }

        fun bind(exercise: CreateWorkoutPresenter.Exercise) {
            tvName.text = exercise.name
            tvReps.text = exercise.reps.toString()
            tvScore.text = exercise.score.toString()
        }
    }
}