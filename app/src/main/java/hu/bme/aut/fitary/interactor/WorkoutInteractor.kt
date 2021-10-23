package hu.bme.aut.fitary.interactor

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import hu.bme.aut.fitary.domainModel.DomainWorkout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    // Block thread during initializing this, because I need this set later
    private val currentUserId = runBlocking {
        firebaseDataSource.getCurrentUserId()
    }

    val allWorkoutsFlow: StateFlow<List<DomainWorkout>> = firebaseDataSource.workoutsFlow.stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    val userWorkoutsFlow: StateFlow<List<DomainWorkout>> = firebaseDataSource.workoutsFlow.map {
        it.filter { domainWorkout ->
            domainWorkout.uid == currentUserId
        }
    }.stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        initialValue = listOf()
    )

    suspend fun saveWorkout(
        workout: DomainWorkout,
        onSuccessListener: OnSuccessListener<Void>,
        onFailureListener: OnFailureListener
    ) {
        firebaseDataSource.saveWorkout(workout, onSuccessListener, onFailureListener)
    }

    suspend fun isWorkoutOwnedByCurrentUser(domainWorkout: DomainWorkout) =
        domainWorkout.uid == currentUserId

}