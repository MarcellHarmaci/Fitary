package hu.bme.aut.fitary.ui.socialWorkouts

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.fitary.interactor.UserInteractor
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import javax.inject.Inject

class SocialWorkoutsPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
    private val userInteractor: UserInteractor
) {

    suspend fun getUsername(): String {
        TODO("Get username from UserInteractor")
    }

    suspend fun getWorkouts(): MutableList<Workout> = withIOContext {
        val domainWorkouts = workoutInteractor.getAllWorkouts()
        val workouts = mutableListOf<Workout>()

        for(workout in domainWorkouts) {
            var username: String? = null
            if (workout.uid != null)
                username = userInteractor.getUsernameById(workout.uid)
            val score = workout.score ?: 0.0
            val comment = workout.comment ?: "-"

            workouts.add(Workout(username ?: "-", score, comment))
        }

        return@withIOContext workouts
    }

    // Presentation model
    class Workout(username: String, score: Double, comment: String)
}