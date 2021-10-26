package hu.bme.aut.fitary.ui.editWorkout.adapter

import androidx.recyclerview.widget.DiffUtil
import hu.bme.aut.fitary.ui.editWorkout.EditWorkoutPresenter

object ExerciseComparator : DiffUtil.ItemCallback<EditWorkoutPresenter.Exercise>() {

    override fun areItemsTheSame(
        oldItem: EditWorkoutPresenter.Exercise,
        newItem: EditWorkoutPresenter.Exercise
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: EditWorkoutPresenter.Exercise,
        newItem: EditWorkoutPresenter.Exercise
    ): Boolean {
        return oldItem == newItem
    }

}