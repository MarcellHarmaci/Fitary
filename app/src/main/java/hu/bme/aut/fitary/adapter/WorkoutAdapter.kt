package hu.bme.aut.fitary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.data.DomainWorkout
import kotlinx.android.synthetic.main.list_item_workout.view.*

class WorkoutAdapter(private val context: Context?) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    private val domainWorkoutList: MutableList<DomainWorkout> = mutableListOf()
    private var lastPosition = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.tvUsername
        val tvReps: TextView = itemView.tvScore
        val tvComment: TextView = itemView.tvComment
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(R.layout.list_item_workout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val tmpWorkout = domainWorkoutList[position]

        var allReps = 0
        for (exercise in tmpWorkout.domainExercises)
            allReps += exercise.reps

        viewHolder.tvUsername.text = tmpWorkout.userName
        viewHolder.tvReps.text = allReps.toString()
        viewHolder.tvComment.text = tmpWorkout.comment

        setAnimation(viewHolder.itemView, position)
    }

    override fun getItemCount() = domainWorkoutList.size

    fun addWorkout(domainWorkout: DomainWorkout?) {
        domainWorkout ?: return

        domainWorkoutList.add(domainWorkout)
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