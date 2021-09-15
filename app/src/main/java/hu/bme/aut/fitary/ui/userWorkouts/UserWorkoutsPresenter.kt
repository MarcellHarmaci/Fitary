package hu.bme.aut.fitary.ui.userWorkouts

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import javax.inject.Inject

class UserWorkoutsPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
) {

    val workoutsLiveData = MutableLiveData<MutableList<Workout>>()

    init {
        workoutInteractor.userWorkoutsLiveData.observeForever {
            val workouts = it.map { domainWorkout ->
                Workout(
                    score = domainWorkout.score,
                    comment = domainWorkout.comment ?: "-"
                )
            }.toMutableList()

            workoutsLiveData.value = workouts
        }
    }

    // Presentation model
    data class Workout(
        val score: Double,
        val comment: String
    )
}