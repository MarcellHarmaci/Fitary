package hu.bme.aut.fitary.interactor

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import hu.bme.aut.fitary.domainModel.DomainWorkout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    private var currentUserId: String? = null

    val allWorkoutsFlow = firebaseDataSource.workoutsFlow.shareIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        replay = 1
    )

    val userWorkoutsFlow = firebaseDataSource.workoutsFlow.map { collectedWorkouts ->
        collectedWorkouts.filter {
            if (currentUserId == null)
                currentUserId = firebaseDataSource.getCurrentUserId()

            it.uid == currentUserId
        }
    }.shareIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        replay = 1
    )

    suspend fun saveWorkout(
        workout: DomainWorkout,
        onSuccessListener: OnSuccessListener<Void>,
        onFailureListener: OnFailureListener
    ) {
        firebaseDataSource.saveWorkout(workout, onSuccessListener, onFailureListener)
    }

}