package hu.bme.aut.fitary.ui.userWorkouts

import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserWorkoutsPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
) {

    val workoutsChannel = Channel<MutableList<UserWorkoutsPresenter.Workout>>()
    private var workouts = mutableListOf<UserWorkoutsPresenter.Workout>()

    init {
        CoroutineScope(Dispatchers.IO).launch {

            workoutInteractor.workoutListChannel.consumeEach { consumedWorkouts ->

                workouts = consumedWorkouts.map {
                    Workout(
                        score = it.score,
                        comment = it.comment ?: "-"
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