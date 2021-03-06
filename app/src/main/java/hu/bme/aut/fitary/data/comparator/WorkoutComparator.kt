package hu.bme.aut.fitary.data.comparator

import androidx.recyclerview.widget.DiffUtil
import hu.bme.aut.fitary.data.DomainWorkout

object WorkoutComparator : DiffUtil.ItemCallback<DomainWorkout>() {
    override fun areItemsTheSame(oldItem: DomainWorkout, newItem: DomainWorkout): Boolean {
        // Does DomainWorkout need an ID?
        TODO("Not yet implemented") 
    }

    override fun areContentsTheSame(oldItem: DomainWorkout, newItem: DomainWorkout): Boolean {
        return oldItem == newItem
    }
}