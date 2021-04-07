package hu.bme.aut.fitary.interactor

import androidx.lifecycle.Observer
import hu.bme.aut.fitary.data.DomainWorkout
import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    val workoutListChannel = Channel<MutableList<DomainWorkout>>()
    val userWorkoutsChannel = Channel<MutableList<DomainWorkout>>()

    private var workouts = mutableListOf<DomainWorkout>()
    private var userWorkouts = mutableListOf<DomainWorkout>()

    private val workoutObserver = Observer<MutableList<DomainWorkout>> { observedWorkouts ->
        CoroutineScope(Dispatchers.Default).launch {
            val currentUserId = firebaseDataSource.getCurrentUser()?.id ?: return@launch

            workouts = observedWorkouts
            workoutListChannel.send(workouts)

            userWorkouts = observedWorkouts.filter { it.uid == currentUserId }.toMutableList()
            userWorkoutsChannel.send(userWorkouts)
        }
    }

    init {
        firebaseDataSource.workouts.observeForever(workoutObserver)
    }

}