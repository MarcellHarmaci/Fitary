package hu.bme.aut.fitary.ui.socialWorkouts

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.fitary.interactor.UserInteractor
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SocialWorkoutsPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
    private val userInteractor: UserInteractor
) {
    val workoutsChannel = Channel<MutableList<Workout>>()
    private var workouts = mutableListOf<Workout>()

    init {
        CoroutineScope(Dispatchers.IO).launch {

            workoutInteractor.workoutListChannel.consumeEach { consumedWorkouts ->
                Timber.d("Size: ${consumedWorkouts.size}")

                workouts = consumedWorkouts.map { it ->
                    Workout(
                        username = userInteractor.getUsernameById(it.uid) ?: "-",
                        score = it.score,
                        comment = it.comment ?: "-"
                    )
                }.toMutableList()

                workoutsChannel.send(workouts)
            }
        }
    }

    suspend fun getWorkouts(): MutableList<Workout> = withIOContext {

        return@withIOContext workoutInteractor.getAllWorkouts()
            .map { workout ->
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