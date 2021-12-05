package hu.bme.aut.fitary.ui.userProfile

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import java.io.*
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

    fun loadImageAsAvatar(filePath: String?) = execute {
        filePath ?: return@execute

        val byteArray = presenter.getByteArrayFromImagePath(filePath)

        byteArray?.let {
            val oldState = viewState as UserProfileLoaded
            val updatedProfile = oldState.profile.copy(avatar = byteArray)

            viewState = oldState.copy(profile = updatedProfile)
        }
    }

    fun save() = execute {
        val currentState = viewState as UserProfileLoaded
        presenter.save(currentState.profile)
    }

}
