package hu.bme.aut.fitary.ui.userProfile

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.fitary.interactor.UserInteractor
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import javax.inject.Inject

class UserProfilePresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val workoutInteractor: WorkoutInteractor
) {

    suspend fun loadUserProfile() = withIOContext {
        val user = userInteractor.getCurrentUser()
        val numberOfWorkouts = workoutInteractor.userWorkoutsFlow.value.size
        val fullScore = workoutInteractor.userWorkoutsFlow.value.sumOf { it.score }

        UserProfile(
            userId = user?.id,
            username = user?.username ?: "No username",
            numberOfWorkouts = numberOfWorkouts,
            fullScore = fullScore
        )
    }

    // Presentation model
    data class UserProfile(
        val userId: String?,
        val username: String,
        val numberOfWorkouts: Int,
        val fullScore: Double
    )
}
