package hu.bme.aut.fitary.dataSource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.data.Workout

class FirebaseDAO {

    val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private val _workouts = mutableListOf<Workout>()
    val workouts: List<Workout>
        get() = _workouts.toList()

    val userWorkouts
        get() = lazy {
            _workouts.filter { it.uid.equals(currentUser?.uid) }
        }

    init {
        initFirebaseConnection()
    }

    private fun initFirebaseConnection() {
        FirebaseDatabase.getInstance()
            .getReference("workouts")
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newWorkout = dataSnapshot.getValue(Workout::class.java)
                    newWorkout?.let { _workouts += newWorkout }
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

}