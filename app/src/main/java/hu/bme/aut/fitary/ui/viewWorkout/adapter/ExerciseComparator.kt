package hu.bme.aut.fitary.ui.viewWorkout.adapter

import androidx.recyclerview.widget.DiffUtil
import hu.bme.aut.fitary.ui.viewWorkout.ViewWorkoutPresenter

object ExerciseComparator : DiffUtil.ItemCallback<ViewWorkoutPresenter.Exercise>() {

    override fun areItemsTheSame(
        oldItem: ViewWorkoutPresenter.Exercise,
        newItem: ViewWorkoutPresenter.Exercise
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: ViewWorkoutPresenter.Exercise,
        newItem: ViewWorkoutPresenter.Exercise
    ): Boolean {
        return oldItem == newItem
    }

}