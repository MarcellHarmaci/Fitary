package hu.bme.aut.fitary.dataSource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.dataSource.model.Exercise
import hu.bme.aut.fitary.dataSource.model.UserProfile
import hu.bme.aut.fitary.dataSource.model.Workout
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutDAO @Inject constructor(
    private val userDAO: UserDAO
) {

    private val database = FirebaseDatabase.getInstance()

    private val _workouts = mutableListOf<Workout>()
    val workouts: List<Workout>
        get() = _workouts.toList()

    private val _userWorkouts = mutableListOf<Workout>()
    val userWorkouts
        get() = _userWorkouts.toList()

    init {
        database
            .getReference("workouts")
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newWorkout = dataSnapshot.getValue(Workout::class.java)

                    newWorkout?.let {
                        _workouts += newWorkout

                        if (newWorkout.uid == userDAO.currentUser?.id)
                            _userWorkouts += newWorkout
                    }
                }

                override fun onChildChanged(
                    dataSnapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    suspend fun saveWorkout(workout: Workout) {
        val key = database.reference
            .child("workouts")
            .push().key ?: return

        database.reference
            .child("workouts")
            .child(key)
            .setValue(workout)
    }

}