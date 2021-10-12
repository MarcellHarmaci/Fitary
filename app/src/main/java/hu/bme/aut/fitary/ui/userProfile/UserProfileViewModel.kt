package hu.bme.aut.fitary.ui.userProfile

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    private val presenter: UserProfilePresenter
) : RainbowCakeViewModel<UserProfileViewState>(Loading) {

    init {
        execute {
            val profile = presenter.loadUserProfile()
            viewState = UserProfileLoaded(profile)
        }
    }

}
