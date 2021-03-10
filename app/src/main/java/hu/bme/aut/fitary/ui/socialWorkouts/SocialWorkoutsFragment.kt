package hu.bme.aut.fitary.ui.socialWorkouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.adapter.WorkoutAdapter
import hu.bme.aut.fitary.adapter.WorkoutListAdapter
import kotlinx.android.synthetic.main.fragment_workouts_social.*
import kotlinx.android.synthetic.main.fragment_workouts_social.view.*

class SocialWorkoutsFragment :
    RainbowCakeFragment<SocialWorkoutsViewState, SocialWorkoutsViewModel>() {

    private lateinit var workoutAdapter: WorkoutListAdapter

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_workouts_social

    override fun onStart() {
        super.onStart()
        viewModel.loadWorkouts()
    }

    override fun render(viewState: SocialWorkoutsViewState) {
        when (viewState) {
            is Loading -> {
                pbListLoading.visibility = View.VISIBLE
            }
            is SocialWorkoutsLoaded -> {
                pbListLoading.visibility = View.GONE
                workoutAdapter.submitList(viewState.workouts)
            }
        }.exhaustive
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutAdapter = WorkoutListAdapter()
        rvSocialWorkouts.adapter = workoutAdapter
        rvSocialWorkouts.layoutManager = LinearLayoutManager(view.context).apply {
            reverseLayout = true
            stackFromEnd = true
        }
    }

}