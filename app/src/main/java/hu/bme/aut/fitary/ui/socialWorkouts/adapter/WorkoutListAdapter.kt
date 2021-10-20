package hu.bme.aut.fitary.ui.socialWorkouts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.socialWorkouts.SocialWorkoutsPresenter
import kotlinx.android.synthetic.main.list_item_workout.view.*

class WorkoutListAdapter :
    ListAdapter<SocialWorkoutsPresenter.Workout, WorkoutListAdapter.WorkoutViewHolder>(
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
        private val tvTitle = itemView.tvTitle
        private val tvUsername = itemView.tvUsername
        private val tvScore = itemView.tvScore

        fun bind(workout: SocialWorkoutsPresenter.Workout) {
            tvTitle.text = workout.title
            tvUsername.text = workout.username
            tvScore.text = workout.score.toString()
        }
    }
}
