package hu.bme.aut.fitary.ui.userWorkouts

import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserWorkoutsPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
) {

    val workouts: Flow<List<Workout>> = workoutInteractor.userWorkoutsFlow.map {
        it.map { domainWorkout ->
            Workout(
                id = domainWorkout.id,
                score = domainWorkout.score,
                comment = domainWorkout.comment ?: "-"
            )
        }
    }

    // Presentation model
    data class Workout(
        val id: String?,
        val score: Double,
        val comment: String
    )
}