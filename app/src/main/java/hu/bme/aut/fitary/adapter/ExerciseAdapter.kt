package hu.bme.aut.fitary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.data.Exercise
import kotlinx.android.synthetic.main.list_item_exercise.view.*

class ExerciseAdapter(
    private val context: Context,
    private val exercises: MutableList<Exercise>
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    fun addItem(newItem: Exercise) {
        exercises.add(newItem)
    }

    override fun getCount(): Int {
        return exercises.size
    }

    override fun getItem(position: Int): Any {
        return exercises[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val exerciseRowView = inflater.inflate(R.layout.list_item_exercise, parent, false)

        val exercise = exercises[position]
        exerciseRowView.tvExerciseName.text = exercise.name
        exerciseRowView.tvReps.text = exercise.reps.toString()

        return exerciseRowView
    }

}