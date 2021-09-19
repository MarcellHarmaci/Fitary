package hu.bme.aut.fitary.ui.socialWorkouts

import hu.bme.aut.fitary.interactor.UserInteractor
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SocialWorkoutsPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
    private val userInteractor: UserInteractor
) {

    val workoutsFlow: Flow<List<Workout>> = workoutInteractor.allWorkoutsFlow.map {
        it.map { domainWorkout ->
            Workout(
                username = userInteractor.getUsernameById(domainWorkout.uid) ?: "-",
                score = domainWorkout.score,
                comment = domainWorkout.comment ?: "-"
            )
        }
    }

    // Presentation model
    data class Workout(
        val username: String,
        val score: Double,
        val comment: String
    )
}