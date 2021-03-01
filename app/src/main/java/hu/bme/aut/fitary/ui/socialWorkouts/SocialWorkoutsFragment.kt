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
import kotlinx.android.synthetic.main.fragment_workouts_social.view.*

class SocialWorkoutsFragment :
    RainbowCakeFragment<SocialWorkoutsViewState, SocialWorkoutsViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_workouts_social

    override fun onStart() {
        super.onStart()
        viewModel.loadWorkouts()
    }

    override fun render(viewState: SocialWorkoutsViewState) {
        when (viewState) {
            is Loading -> {
                TODO("Show a progress dialog")
            }
            is SocialWorkoutsLoaded -> {
                TODO("Get workouts from view state and give it to the list adapter")
                viewState.workouts
            }
        }.exhaustive
    }


    private lateinit var workoutAdapter: WorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_workouts_social, container, false)
        workoutAdapter = WorkoutAdapter(context?.applicationContext)
        root.rvSocialWorkouts.layoutManager = LinearLayoutManager(container?.context).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        root.rvSocialWorkouts.adapter = workoutAdapter

        // initWorkoutsListener() - handled data changes before

        return root
    }

}