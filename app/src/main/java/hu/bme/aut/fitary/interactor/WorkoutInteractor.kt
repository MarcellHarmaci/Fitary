package hu.bme.aut.fitary.interactor

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import hu.bme.aut.fitary.domainModel.DomainWorkout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : Observable<MutableList<DomainWorkout>> {

    private var currentUserId: String? = null

    override val observers = mutableListOf<Observer<MutableList<DomainWorkout>>>()

    val allWorkoutsFlow = firebaseDataSource.workoutsFlow.shareIn(
        scope = CoroutineScope(Dispatchers.Default),
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
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.Eagerly,
        replay = 1
    )


    val userWorkoutsLiveData = MutableLiveData<MutableList<DomainWorkout>>()

    init {
        runBlocking {
            launch {
                currentUserId = firebaseDataSource.getCurrentUserId()
            }
        }

        firebaseDataSource.workouts.observeForever { observedWorkouts ->
            if (currentUserId != null) {
                userWorkoutsLiveData.value = observedWorkouts.filter {
                    it.uid == currentUserId
                }.toMutableList()
            }
        }

    }

    suspend fun saveWorkout(
        workout: DomainWorkout,
        onSuccessListener: OnSuccessListener<Void>,
        onFailureListener: OnFailureListener
    ) {
        firebaseDataSource.saveWorkout(workout, onSuccessListener, onFailureListener)
    }

}