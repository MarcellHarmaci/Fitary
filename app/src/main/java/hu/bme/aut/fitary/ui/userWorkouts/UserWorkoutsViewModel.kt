package hu.bme.aut.fitary.ui.userWorkouts

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import kotlinx.coroutines.channels.consumeEach
import javax.inject.Inject

class UserWorkoutsViewModel @Inject constructor(
    private val userWorkoutsPresenter: UserWorkoutsPresenter
) : RainbowCakeViewModel<UserWorkoutsViewState>(Loading) {

    init { // TODO Use load function or init?
        executeNonBlocking { // TODO execute or executeNonBlocking?
            viewState = Loading

            userWorkoutsPresenter.workoutsChannel.consumeEach {
                viewState = UserWorkoutsLoaded(it)
            }
        }
    }

}