package hu.bme.aut.fitary.ui.userProfile

sealed class UserProfileViewState

object Loading : UserProfileViewState()

data class UserProfileLoaded(
    val userProfile: UserProfilePresenter.UserProfile
) : UserProfileViewState()
