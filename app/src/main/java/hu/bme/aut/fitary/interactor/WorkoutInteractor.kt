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
    private var workouts = mutableListOf<DomainWorkout>()

    private val workoutObserver = Observer<MutableList<DomainWorkout>> {
        Timber.d("Size: ${it.size}")

        CoroutineScope(Dispatchers.Default).launch {
            workouts = it
            workoutListChannel.send(workouts)
        }
    }

    init {
        firebaseDataSource.workouts.observeForever(workoutObserver)
        Timber.d("Interactor's observer set")
    }

    suspend fun getAllWorkouts() = workouts

    suspend fun getUserWorkouts() = firebaseDataSource.getUserWorkouts()
}