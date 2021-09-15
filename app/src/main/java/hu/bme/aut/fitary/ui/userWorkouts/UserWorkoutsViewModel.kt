package hu.bme.aut.fitary.ui.userWorkouts

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class UserWorkoutsViewModel @Inject constructor(
    private val userWorkoutsPresenter: UserWorkoutsPresenter
) : RainbowCakeViewModel<UserWorkoutsViewState>(Loading) {

    init {
        userWorkoutsPresenter.workoutsLiveData.observeForever {
            viewState = UserWorkoutsLoaded(it)
        }
    }

}