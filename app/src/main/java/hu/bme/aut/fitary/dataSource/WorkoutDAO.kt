package hu.bme.aut.fitary.dataSource

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.dataSource.model.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutDAO @Inject constructor() {

    private val database = FirebaseDatabase.getInstance()

    private val workoutMap = mutableMapOf<String?, Workout>()
    val workouts = MutableLiveData<MutableList<Workout>>()

    val workoutsFlow = MutableSharedFlow<List<Workout>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        database
            .getReference("workouts")
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(
                    dataSnapshot: DataSnapshot,
                    previousChildName: String?
                ) = updateLocalWorkouts(dataSnapshot, previousChildName)

                override fun onChildChanged(
                    dataSnapshot: DataSnapshot,
                    previousChildName: String?
                ) = updateLocalWorkouts(dataSnapshot, previousChildName)

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    val workout = dataSnapshot.getValue(Workout::class.java)

                    if (workout != null) {
                        val keys = workoutMap.filterValues { it == workout }.keys

                        // There can only be one workout matching the deleted one
                        // UserId with the DateTime of creation can differentiate workouts
                        // That's why it's safe to remove "all" matching keys
                        for (key in keys) {
                            workoutMap.remove(key)
                        }
                    }

                }

                override fun onChildMoved(
                    dataSnapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    // Intentionally not implemented
                    throw IllegalStateException(
                        "onChildMoved event should not occur thus it is not implemented"
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    val dbException = error.toException()

                    Timber.e(dbException, error.details)
                    throw dbException
                }
            })
    }

    private fun updateLocalWorkouts(dataSnapshot: DataSnapshot, previousChildName: String?) {
        val workout = dataSnapshot.getValue(Workout::class.java)

        if (workout != null) {
            workoutMap[previousChildName] = workout
            val newState = workoutMap.values.toMutableList()

            workouts.value = newState

            CoroutineScope(Dispatchers.Default).launch {
                workoutsFlow.emit(newState)
            }
        }
    }

    suspend fun saveWorkout(
        workout: Workout,
        onSuccessListener: OnSuccessListener<Void>,
        onFailureListener: OnFailureListener
    ) {
        val key = database.reference
            .child("workouts")
            .push().key ?: return

        database.reference
            .child("workouts")
            .child(key)
            .setValue(workout)
            .addOnSuccessListener(onSuccessListener)
            .addOnFailureListener(onFailureListener)
    }

}

