package hu.bme.aut.fitary.ui.userWorkouts

import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserWorkoutsPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
) {

    val workoutsChannel = Channel<MutableList<Workout>>()

    init {
        workoutInteractor.userWorkoutsLiveData.observeForever {

            CoroutineScope(Dispatchers.IO).launch {

                val workouts = it.map { domainWorkout ->
                    Workout(
                        score = domainWorkout.score,
                        comment = domainWorkout.comment ?: "-"
                    )
                }.toMutableList()

                workoutsChannel.send(workouts)
            }
        }
    }

    // Presentation model
    data class Workout(
        val score: Double,
        val comment: String
    )
}