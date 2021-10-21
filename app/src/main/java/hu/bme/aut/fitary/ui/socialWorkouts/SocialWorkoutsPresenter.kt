package hu.bme.aut.fitary.ui.socialWorkouts

import hu.bme.aut.fitary.interactor.UserInteractor
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SocialWorkoutsPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
    private val userInteractor: UserInteractor
) {

    val workouts: Flow<List<Workout>> = workoutInteractor.allWorkoutsFlow.map {
        it.map { domainWorkout ->
            Workout(
                username = userInteractor.getUsernameById(domainWorkout.uid) ?: "-",
                score = domainWorkout.score,
                title = domainWorkout.title ?: "Awesome workout",
                avatar = userInteractor.getAvatarById(domainWorkout.uid)
            )
        }
    }

    // Presentation model
    data class Workout(
        val username: String,
        val score: Double,
        val title: String,
        val avatar: ByteArray? = null
    ) {
        override fun toString(): String {
            val isAvatarNull = if (avatar == null) {
                "null"
            } else {
                "notNull"
            }
            return "Workout(username='$username', score=$score, title='$title', avatar=$isAvatarNull)"
        }
    }
}