package hu.bme.aut.fitary.ui.userWorkouts

import hu.bme.aut.fitary.interactor.UserInteractor
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserWorkoutsPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
    private val userInteractor: UserInteractor
) {

    val workouts: Flow<List<Workout>> = workoutInteractor.userWorkoutsFlow.map {
        it.map { domainWorkout ->
            Workout(
                id = domainWorkout.id,
                score = domainWorkout.score,
                title = domainWorkout.title ?: "Awesome workout",
                avatar = userInteractor.getAvatarById(domainWorkout.uid)
            )
        }
    }

    // Presentation model
    data class Workout(
        val id: String?,
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
            return "Workout(id=$id, score=$score, title='$title', avatar=$isAvatarNull)"
        }
    }
}