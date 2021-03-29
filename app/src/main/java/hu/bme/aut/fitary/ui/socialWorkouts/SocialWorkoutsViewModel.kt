package hu.bme.aut.fitary.ui.socialWorkouts

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import kotlinx.coroutines.channels.consumeEach
import javax.inject.Inject

class SocialWorkoutsViewModel @Inject constructor(
    private val socialWorkoutsPresenter: SocialWorkoutsPresenter
) : RainbowCakeViewModel<SocialWorkoutsViewState>(Loading) {

    fun loadWorkouts() = execute {
        viewState = Loading

        socialWorkoutsPresenter.workoutsChannel.consumeEach {
            viewState = SocialWorkoutsLoaded(it)
        }
    }

}