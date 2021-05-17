package hu.bme.aut.fitary.ui.userWorkouts

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import kotlinx.coroutines.channels.consumeEach

import javax.inject.Inject

class UserWorkoutsViewModel @Inject constructor(
    private val userWorkoutsPresenter: UserWorkoutsPresenter
) : RainbowCakeViewModel<UserWorkoutsViewState>(Loading) {

    fun loadWorkouts() = execute {
        viewState = Loading

        userWorkoutsPresenter.workoutsChannel.consumeEach {
            viewState = UserWorkoutsLoaded(it)
        }
    }

}