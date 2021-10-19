package hu.bme.aut.fitary.ui.userProfile

sealed class UserProfileViewState

object Loading : UserProfileViewState()

data class UserProfileLoaded(
    val userId: String?,
    val username: String,
    val userMail: String,
    val numberOfWorkouts: Int,
    val fullScore: Double,
    val avatar: ByteArray? = null
) : UserProfileViewState()
