package hu.bme.aut.fitary.ui.userWorkouts

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.fitary.MainActivity
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.userWorkouts.adapter.WorkoutListAdapter
import kotlinx.android.synthetic.main.fragment_workouts_user.*

class UserWorkoutsFragment :
    RainbowCakeFragment<UserWorkoutsViewState, UserWorkoutsViewModel>(),
    PopupMenu.OnMenuItemClickListener {

    private lateinit var workoutAdapter: WorkoutListAdapter

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_workouts_user

    override fun render(viewState: UserWorkoutsViewState) {
        when (viewState) {
            is Loading -> {
                pbListLoading.visibility = View.VISIBLE
            }
            is UserWorkoutsLoaded -> {
                pbListLoading.visibility = View.GONE

                workoutAdapter.submitList(viewState.workouts)

                if (workoutAdapter.itemCount > 0)
                    rvUserWorkouts.smoothScrollToPosition(workoutAdapter.itemCount - 1)
                return
            }
        }.exhaustive
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutAdapter = WorkoutListAdapter(this)
        rvUserWorkouts.adapter = workoutAdapter
        rvUserWorkouts.layoutManager = LinearLayoutManager(view.context).apply {
            reverseLayout = true
            stackFromEnd = true
        }
    }

    fun setFabVisible(isVisible: Boolean) {
        (activity as MainActivity).setFloatingActionButtonVisible(isVisible)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId !in setOf(R.id.item_copy_workout, R.id.item_delete_workout)) {
            return false
        }

        viewModel.onPopupItemSelected(item)
        return true
    }

    fun getListItemPosition(view: View) = rvUserWorkouts.getChildAdapterPosition(view)

}