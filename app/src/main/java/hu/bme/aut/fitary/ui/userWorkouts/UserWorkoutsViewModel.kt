package hu.bme.aut.fitary.ui.userWorkouts

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class UserWorkoutsViewModel @Inject constructor(
    private val presenter: UserWorkoutsPresenter
) : RainbowCakeViewModel<UserWorkoutsViewState>(Loading) {

    private val flowCollection: Job = executeCancellable(blocking = false) {
        presenter.workouts.collect {
            viewState = UserWorkoutsLoaded(it)
        }
    }

    fun cancelFlowCollection() {
        flowCollection.cancel()
    }
}