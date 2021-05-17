package hu.bme.aut.fitary.ui.socialWorkouts

import hu.bme.aut.fitary.interactor.UserInteractor
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class SocialWorkoutsPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
    private val userInteractor: UserInteractor
) {

    val workoutsChannel = Channel<MutableList<Workout>>()

    // TODO move to consumeEach function
    private var workouts = mutableListOf<Workout>()

    init {
        CoroutineScope(Dispatchers.IO).launch {

            workoutInteractor.workoutListChannel.consumeEach { consumedWorkouts ->

                workouts = consumedWorkouts.map { domainWorkout ->
                    Workout(
                        username = userInteractor.getUsernameById(domainWorkout.uid) ?: "-",
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
        val username: String,
        val score: Double,
        val comment: String
    )
}