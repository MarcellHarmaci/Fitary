package hu.bme.aut.fitary.ui.userWorkouts

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class UserWorkoutsViewModel @Inject constructor(
    private val presenter: UserWorkoutsPresenter
) : RainbowCakeViewModel<UserWorkoutsViewState>(Loading) {

    init {
        executeNonBlocking {
            presenter.workouts.collect {
                viewState = UserWorkoutsLoaded(it)
            }
        }
    }

}