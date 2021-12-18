package hu.bme.aut.fitary.ui.viewWorkout.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.viewWorkout.ViewWorkoutPresenter
import kotlinx.android.synthetic.main.list_item_view_exercise.view.*

class ExerciseListAdapter :
    ListAdapter<ViewWorkoutPresenter.Exercise, ExerciseListAdapter.ExerciseViewHolder>(
        ExerciseComparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_view_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)

        if (position % 2 == 1)
            holder.itemView.setBackgroundColor(Color.LTGRAY)
    }

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.tvName
        private val tvReps = itemView.tvReps
        private val tvScore = itemView.tvScore

        fun bind(exercise: ViewWorkoutPresenter.Exercise) {
            tvName.text = exercise.name
            tvReps.text = exercise.reps.toString()
            tvScore.text = String.format("%.2f", exercise.score)
        }
    }
}