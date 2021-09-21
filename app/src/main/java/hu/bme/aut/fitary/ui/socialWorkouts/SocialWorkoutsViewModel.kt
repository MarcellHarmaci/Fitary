package hu.bme.aut.fitary.ui.socialWorkouts

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SocialWorkoutsViewModel @Inject constructor(
    private val presenter: SocialWorkoutsPresenter
) : RainbowCakeViewModel<SocialWorkoutsViewState>(Loading) {

    private val flowCollection: Job = executeCancellable(blocking = false) {
        presenter.workouts.collect {
            viewState = SocialWorkoutsLoaded(it)
        }
    }

    fun cancelFlowCollection() {
        flowCollection.cancel()
    }

}