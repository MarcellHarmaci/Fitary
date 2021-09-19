package hu.bme.aut.fitary.ui.socialWorkouts

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SocialWorkoutsViewModel @Inject constructor(
    private val socialWorkoutsPresenter: SocialWorkoutsPresenter
) : RainbowCakeViewModel<SocialWorkoutsViewState>(Loading) {

    init {
        executeNonBlocking {
            socialWorkoutsPresenter.workoutsFlow.collect {
                viewState = SocialWorkoutsLoaded(it)
            }
        }
    }

}