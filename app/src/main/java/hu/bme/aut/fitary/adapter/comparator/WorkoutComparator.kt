package hu.bme.aut.fitary.adapter.comparator

import androidx.recyclerview.widget.DiffUtil
import hu.bme.aut.fitary.ui.socialWorkouts.SocialWorkoutsPresenter

object WorkoutComparator : DiffUtil.ItemCallback<SocialWorkoutsPresenter.Workout>() {

    override fun areItemsTheSame(
        oldItem: SocialWorkoutsPresenter.Workout,
        newItem: SocialWorkoutsPresenter.Workout
    ): Boolean {
        // Does SWPresenter.Workout needs an ID?
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(
        oldItem: SocialWorkoutsPresenter.Workout,
        newItem: SocialWorkoutsPresenter.Workout
    ): Boolean {
        return oldItem == newItem
    }

}