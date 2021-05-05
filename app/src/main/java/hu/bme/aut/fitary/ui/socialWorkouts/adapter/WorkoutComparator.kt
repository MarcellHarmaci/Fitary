package hu.bme.aut.fitary.ui.socialWorkouts.adapter

import androidx.recyclerview.widget.DiffUtil
import hu.bme.aut.fitary.ui.socialWorkouts.SocialWorkoutsPresenter

object WorkoutComparator : DiffUtil.ItemCallback<SocialWorkoutsPresenter.Workout>() {

    // TODO Replace with better solution
    override fun areItemsTheSame(
        oldItem: SocialWorkoutsPresenter.Workout,
        newItem: SocialWorkoutsPresenter.Workout
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: SocialWorkoutsPresenter.Workout,
        newItem: SocialWorkoutsPresenter.Workout
    ): Boolean {
        return oldItem == newItem
    }

}