package hu.bme.aut.fitary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.data.DomainWorkout
import hu.bme.aut.fitary.data.comparator.WorkoutComparator
import kotlinx.android.synthetic.main.list_item_workout.view.*

class WorkoutListAdapter : ListAdapter<DomainWorkout, WorkoutListAdapter.WorkoutViewHolder>(
    WorkoutComparator
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = getItem(position)
        holder.bind(workout)
    }

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUsername = itemView.tvUsername
        private val tvScore = itemView.tvScore
        private val tvComment = itemView.tvComment

        fun bind(workout: DomainWorkout) {
            tvUsername.text = workout.userName
            tvScore.text = workout.score.toString()
            tvComment.text = workout.comment
        }
    }
}
