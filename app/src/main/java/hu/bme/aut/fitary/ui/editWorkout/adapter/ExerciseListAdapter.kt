package hu.bme.aut.fitary.ui.editWorkout.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.iterator
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.editWorkout.EditWorkoutFragment
import hu.bme.aut.fitary.ui.editWorkout.EditWorkoutPresenter
import hu.bme.aut.fitary.ui.editWorkout.EditWorkoutViewModel
import hu.bme.aut.fitary.ui.editWorkout.dialog.EditExerciseDialogHandler
import hu.bme.aut.fitary.ui.editWorkout.helper.DragAndDropCallback
import kotlinx.android.synthetic.main.list_item_exercise.view.*
import java.util.*

class ExerciseListAdapter(
    private val editWorkoutViewModel: EditWorkoutViewModel,
    private val fragment: EditWorkoutFragment
) : ListAdapter<EditWorkoutPresenter.Exercise, ExerciseListAdapter.ExerciseViewHolder>(
    ExerciseComparator
), DragAndDropCallback.OnItemMovedHandler {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_exercise, parent, false)

        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)
    }

    override fun onItemMoved(from: Int, to: Int) {
        editWorkoutViewModel.swapExercises(from, to)

        val list = currentList.toMutableList()
        Collections.swap(list, from, to)
        notifyItemMoved(from, to)
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class ExerciseViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), EditExerciseDialogHandler {

        private val tvName = itemView.tvExerciseName
        private val tvReps = itemView.tvReps
        private val tvScore = itemView.tvScore

        private var defaultBackgroundColor = Color.TRANSPARENT

        init {
            editWorkoutViewModel.setEditExerciseDialogHandler(this)

            itemView.setOnClickListener {
                editWorkoutViewModel.createEditExerciseDialog(layoutPosition)
            }

            itemView.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val background = itemView.background
                        if (background is ColorDrawable) {
                            defaultBackgroundColor = background.color
                        }

                        itemView.setBackgroundColor(Color.LTGRAY)
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        itemView.setBackgroundColor(defaultBackgroundColor)
                    }
                }
                false
            }

            itemView.ibExerciseActions.setOnClickListener { button ->
                PopupMenu(fragment.context, button).apply {
                    setOnMenuItemClickListener(fragment)
                    inflate(R.menu.popup_menu_exercise)

                    val position: Int = fragment.getListItemPosition(itemView)
                    val posIntent = Intent().putExtra("position", position)
                    menu.iterator().forEach { menuItem ->
                        menuItem.intent = posIntent
                    }

                    show()
                }
            }
        }

        fun bind(exercise: EditWorkoutPresenter.Exercise) {
            tvName.text = exercise.name
            tvReps.text = "x " + exercise.reps.toString()
            tvScore.text = exercise.score.toString()
        }

        override fun onEditExerciseDialogReady(dialog: DialogFragment) {
            dialog.show(fragment.childFragmentManager, "Edit exercise")
        }
    }
}