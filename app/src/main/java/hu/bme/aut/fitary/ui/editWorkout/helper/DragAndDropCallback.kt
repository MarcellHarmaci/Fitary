package hu.bme.aut.fitary.ui.editWorkout.helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class DragAndDropCallback(
    private val ontItemMovedHandler: OnItemMovedHandler
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    0
) {

    interface OnItemMovedHandler {
        fun onItemMoved(from: Int, to: Int)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        ontItemMovedHandler.onItemMoved(
            from = viewHolder.adapterPosition,
            to = target.adapterPosition
        )

        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
}
