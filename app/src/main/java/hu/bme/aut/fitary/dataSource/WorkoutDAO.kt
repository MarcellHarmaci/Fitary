package hu.bme.aut.fitary.dataSource

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.dataSource.model.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutDAO @Inject constructor() {

    private val database = FirebaseDatabase.getInstance()

    private val workoutMap = mutableMapOf<String?, Workout>()

    val workoutsFlow = MutableStateFlow<List<Pair<String?, Workout>>>(listOf())

    init {
        database
            .getReference("workouts")
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(
                    dataSnapshot: DataSnapshot,
                    previousChildName: String?
                ) = workoutUpsertHandler(
                    workout = dataSnapshot.getValue(Workout::class.java),
                    dbNodeId = previousChildName
                )

                override fun onChildChanged(
                    dataSnapshot: DataSnapshot,
                    previousChildName: String?
                ) = workoutUpsertHandler(
                    workout = dataSnapshot.getValue(Workout::class.java),
                    dbNodeId = previousChildName
                )

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

                        emitNewStateOfWorkouts()
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

    private fun workoutUpsertHandler(workout: Workout?, dbNodeId: String?) {
        if (workout != null) {
            workoutMap[dbNodeId] = workout

            emitNewStateOfWorkouts()
        }
    }

    private fun emitNewStateOfWorkouts() {
        CoroutineScope(Dispatchers.IO).launch {
            workoutsFlow.emit(workoutMap.toList())
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

    suspend fun updateWorkout(
        key: String,
        workout: Workout,
        onSuccessListener: OnSuccessListener<Void>,
        onFailureListener: OnFailureListener
    ) {
        database.reference
            .child("workouts")
            .child(key)
            .setValue(workout)
            .addOnSuccessListener(onSuccessListener)
            .addOnFailureListener(onFailureListener)
    }

}

