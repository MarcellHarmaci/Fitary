package hu.bme.aut.fitary.interactor

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import hu.bme.aut.fitary.domainModel.DomainWorkout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : Observable<MutableList<DomainWorkout>> {

    override val observers = mutableListOf<Observer<MutableList<DomainWorkout>>>()
    val workoutListChannel = Channel<MutableList<DomainWorkout>>()

    val allWorkoutsLiveData = MutableLiveData<MutableList<DomainWorkout>>()
    val userWorkoutsLiveData = MutableLiveData<MutableList<DomainWorkout>>()

    var currentUserId: String? = null

    init {
        runBlocking {
            launch {
                currentUserId = firebaseDataSource.getCurrentUserId()
            }
        }

        firebaseDataSource.workouts.observeForever { observedWorkouts ->
            CoroutineScope(Dispatchers.Default).launch {
                workoutListChannel.send(observedWorkouts)
            }

            allWorkoutsLiveData.value = observedWorkouts

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