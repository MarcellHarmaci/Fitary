package hu.bme.aut.fitary.interactor

import androidx.lifecycle.Observer
import hu.bme.aut.fitary.data.DomainWorkout
import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    val workoutListChannel = Channel<MutableList<DomainWorkout>>()
    private val workouts = mutableListOf<DomainWorkout>()

    private val workoutObserver = Observer<MutableList<DomainWorkout>> {
        CoroutineScope(Dispatchers.IO).launch {
            Timber.d("Interactor's observer called")

            workouts += it
            workoutListChannel.send(workouts)
        }
    }

    init {
        Timber.d("Interactor's observer set")
        firebaseDataSource.workouts.observeForever(workoutObserver)
    }

    suspend fun getAllWorkouts() = workouts

    suspend fun getUserWorkouts() = firebaseDataSource.getUserWorkouts()
}