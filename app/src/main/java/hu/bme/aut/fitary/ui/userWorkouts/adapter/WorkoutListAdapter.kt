package hu.bme.aut.fitary.ui.userWorkouts.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.iterator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.userWorkouts.UserWorkoutsFragment
import hu.bme.aut.fitary.ui.userWorkouts.UserWorkoutsPresenter
import kotlinx.android.synthetic.main.list_item_user_workout.view.*

class WorkoutListAdapter(
    private val fragment: UserWorkoutsFragment
) : ListAdapter<UserWorkoutsPresenter.Workout, WorkoutListAdapter.WorkoutViewHolder>(
    WorkoutComparator
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_user_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = getItem(position)
        holder.bind(workout)
    }

    inner class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvScore = itemView.tvScore
        private val tvTitle = itemView.tvTitle
        private val ivProfile = itemView.ivProfile

        init {
            itemView.ibWorkoutActions.setOnClickListener { button ->
                PopupMenu(fragment.requireContext(), button).apply {
                    inflate(R.menu.popup_menu_workout_actions)

                    // Add list item position to every menu item
                    val posIntent = Intent().putExtra("position", adapterPosition)
                    menu.iterator().forEach { menuItem ->
                        menuItem.intent = posIntent
                    }

                    setOnMenuItemClickListener(fragment)
                    show()
                }
            }
        }

        fun bind(workout: UserWorkoutsPresenter.Workout) {
            tvScore.text = workout.score.toString()
            tvTitle.text = workout.title

            val context = fragment.requireContext()
            if (workout.avatar != null) {
                Glide.with(context)
                    .asBitmap()
                    .load(workout.avatar)
                    .circleCrop()
                    .into(ivProfile)
            } else {
                Glide.with(context)
                    .load(R.drawable.ic_launcher_background)
                    .circleCrop()
                    .into(ivProfile)
            }

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("workout_id", workout.id)

                fragment.findNavController()
                    .navigate(R.id.nav_view_workout, bundle)
            }
        }

    }

}