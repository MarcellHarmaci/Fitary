package hu.bme.aut.fitary.interactor

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import hu.bme.aut.fitary.domainModel.DomainWorkout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : Observable<MutableList<DomainWorkout>> {

    override val observers = mutableListOf<Observer<MutableList<DomainWorkout>>>()
    val workoutListChannel = Channel<MutableList<DomainWorkout>>()

    private var workouts = mutableListOf<DomainWorkout>()
    private var userWorkouts = mutableListOf<DomainWorkout>()
        set(value) {
            field = value
            notifyObservers(value)
        }

    init {
        firebaseDataSource.workouts.observeForever { observedWorkouts ->
            CoroutineScope(Dispatchers.Default).launch {
                val currentUserId = firebaseDataSource.getCurrentUser()?.id ?: return@launch

                workouts = observedWorkouts
                workoutListChannel.send(workouts)

                userWorkouts = observedWorkouts.filter { it.uid == currentUserId }.toMutableList()
            }
        }
    }

    override fun addObserver(observer: Observer<MutableList<DomainWorkout>>) {
        super.addObserver(observer)

        // Notify new observer about current state
        observer.notify(userWorkouts)
    }

    suspend fun saveWorkout(
        workout: DomainWorkout,
        onSuccessListener: OnSuccessListener<Void>,
        onFailureListener: OnFailureListener
    ) {
        firebaseDataSource.saveWorkout(workout, onSuccessListener, onFailureListener)
    }

}