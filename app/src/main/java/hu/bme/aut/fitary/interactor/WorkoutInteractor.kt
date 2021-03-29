package hu.bme.aut.fitary.interactor

import hu.bme.aut.fitary.data.DomainWorkout
import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    val workoutListChannel = Channel<MutableList<DomainWorkout>>()
    val workouts = mutableListOf<DomainWorkout>()

    init {
        CoroutineScope(Dispatchers.IO).launch {

            firebaseDataSource.workoutChannel.consumeEach {
                workouts += it
                workoutListChannel.send(workouts)
            }
        }
    }

    suspend fun getAllWorkouts() = workouts

    suspend fun getUserWorkouts() = firebaseDataSource.getUserWorkouts()
}