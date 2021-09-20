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
                score = domainWorkout.score,
                comment = domainWorkout.comment ?: "-"
            )
        }
    }

    // Presentation model
    data class Workout(
        val score: Double,
        val comment: String
    )
}