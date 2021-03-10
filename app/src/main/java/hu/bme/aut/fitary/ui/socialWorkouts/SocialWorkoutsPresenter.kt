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

        return@withIOContext workoutInteractor.getAllWorkouts().map { workout ->
                Workout(
                    username = userInteractor.getUsernameById(workout.uid) ?: "-",
                    score = workout.score,
                    comment = workout.comment ?: "-"
                )
            }.toMutableList()
    }

    // Presentation model
    data class Workout(
        val username: String,
        val score: Double,
        val comment: String
    )
}