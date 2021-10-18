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
            viewState = UserProfileLoaded(
                userId = profile.userId,
                username = profile.username,
                numberOfWorkouts = profile.numberOfWorkouts,
                fullScore = profile.fullScore,
                avatar = profile.avatar
            )
        }
    }

    fun loadImageAsAvatar(filePath: String?) = execute {
        filePath ?: return@execute

        val byteArray = presenter.getByteArrayFromImagePath(filePath)

        byteArray?.let {
            val oldState = viewState as UserProfileLoaded

            viewState = oldState.copy(
                userId = oldState.userId,
                username = oldState.username,
                numberOfWorkouts = oldState.numberOfWorkouts,
                fullScore = oldState.fullScore,
                avatar = byteArray
            )
        }
    }

    fun save() = execute {
        val currentState = viewState as UserProfileLoaded
        presenter.save(
            currentState.userId,
            currentState.username,
            currentState.avatar
        )
    }

}
