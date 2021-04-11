package hu.bme.aut.fitary.ui.socialWorkouts.adapter

import androidx.recyclerview.widget.DiffUtil
import hu.bme.aut.fitary.ui.socialWorkouts.SocialWorkoutsPresenter

object WorkoutComparator : DiffUtil.ItemCallback<SocialWorkoutsPresenter.Workout>() {

    override fun areItemsTheSame(
        oldItem: SocialWorkoutsPresenter.Workout,
        newItem: SocialWorkoutsPresenter.Workout
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SocialWorkoutsPresenter.Workout,
        newItem: SocialWorkoutsPresenter.Workout
    ): Boolean {
        return oldItem == newItem
    }

}