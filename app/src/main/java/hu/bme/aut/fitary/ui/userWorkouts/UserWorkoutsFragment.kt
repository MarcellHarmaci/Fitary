package hu.bme.aut.fitary.ui.userWorkouts

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.adapter.WorkoutListAdapter
import kotlinx.android.synthetic.main.fragment_workouts_user.*

class UserWorkoutsFragment :
    RainbowCakeFragment<UserWorkoutsViewState, UserWorkoutsViewModel>() {

    private lateinit var workoutAdapter: WorkoutListAdapter

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_workouts_user

    override fun onStart() {
        super.onStart()

        viewModel.loadWorkouts()
    }

    override fun render(viewState: UserWorkoutsViewState) {
        when (viewState) {
            is Loading -> {
                //pbListLoading.visibility = View.VISIBLE
            }
            is UserWorkoutsLoaded -> {
                //pbListLoading.visibility = View.GONE

                // TODO Implement user workout list adapter
                workoutAdapter.submitList(viewState.workouts)
            }
        }.exhaustive
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutAdapter = WorkoutListAdapter()
        rvUserWorkouts.adapter = workoutAdapter
        rvUserWorkouts.layoutManager = LinearLayoutManager(view.context).apply {
            reverseLayout = true
            stackFromEnd = true
        }
    }

}