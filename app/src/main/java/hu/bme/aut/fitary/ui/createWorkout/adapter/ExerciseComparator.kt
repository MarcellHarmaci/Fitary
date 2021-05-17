package hu.bme.aut.fitary.ui.createWorkout.adapter

import androidx.recyclerview.widget.DiffUtil
import hu.bme.aut.fitary.ui.createWorkout.CreateWorkoutPresenter

object ExerciseComparator : DiffUtil.ItemCallback<CreateWorkoutPresenter.Exercise>() {

    override fun areItemsTheSame(
        oldItem: CreateWorkoutPresenter.Exercise,
        newItem: CreateWorkoutPresenter.Exercise
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: CreateWorkoutPresenter.Exercise,
        newItem: CreateWorkoutPresenter.Exercise
    ): Boolean {
        return oldItem == newItem
    }

}