package hu.bme.aut.fitary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.data.Workout
import kotlinx.android.synthetic.main.list_item_workout.view.*

class WorkoutAdapter(private val context: Context?) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    private val workoutList: MutableList<Workout> = mutableListOf()
    private var lastPosition = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.tvUsername
        val tvReps: TextView = itemView.tvReps
        val tvComment: TextView = itemView.tvComment
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(R.layout.list_item_workout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val tmpWorkout = workoutList[position]

        var allReps = 0
        for (exercise in tmpWorkout.exercises)
            allReps += exercise.reps

        viewHolder.tvUsername.text = tmpWorkout.userName
        viewHolder.tvReps.text = allReps.toString()
        viewHolder.tvComment.text = tmpWorkout.comment

        setAnimation(viewHolder.itemView, position)
    }

    override fun getItemCount() = workoutList.size

    fun addWorkout(workout: Workout?) {
        workout ?: return

        workoutList.add(workout)
        notifyDataSetChanged()
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

}