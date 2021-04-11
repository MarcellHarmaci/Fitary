package hu.bme.aut.fitary.ui.userWorkouts.adapter

import androidx.recyclerview.widget.DiffUtil
import hu.bme.aut.fitary.ui.userWorkouts.UserWorkoutsPresenter

object WorkoutComparator : DiffUtil.ItemCallback<UserWorkoutsPresenter.Workout>() {

    override fun areItemsTheSame(
        oldItem: UserWorkoutsPresenter.Workout,
        newItem: UserWorkoutsPresenter.Workout
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: UserWorkoutsPresenter.Workout,
        newItem: UserWorkoutsPresenter.Workout
    ): Boolean {
        return oldItem == newItem
    }

}