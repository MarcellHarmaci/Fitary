package hu.bme.aut.fitary.ui.socialWorkouts

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.fitary.interactor.UserInteractor
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import javax.inject.Inject

class SocialWorkoutsPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
    private val userInteractor: UserInteractor
) {

    suspend fun getWorkouts(): MutableList<Workout> = withIOContext {

        val workouts = workoutInteractor.getAllWorkouts().map { workout ->
            val username = userInteractor.getUsernameById(workout.uid)
            val score = workout.score
            val comment = workout.comment ?: "-"

            Workout(username ?: "-", score, comment)
        }.toMutableList()

        return@withIOContext workouts
    }

    // Presentation model
    data class Workout(
        val username: String,
        val score: Double,
        val comment: String
    )
}