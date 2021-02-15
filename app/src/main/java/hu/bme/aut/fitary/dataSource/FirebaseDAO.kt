package hu.bme.aut.fitary.dataSource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.data.Workout
import javax.inject.Singleton

@Singleton
class FirebaseDAO {

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    private val _users = mutableMapOf<String, FirebaseUser>()
    val users: Map<String, FirebaseUser>
        get() = _users.toMap()

    private val _workouts = mutableListOf<Workout>()
    val workouts: List<Workout>
        get() = _workouts.toList()

    private val _userWorkouts = mutableListOf<Workout>()
    val userWorkouts
        get() = _userWorkouts.toList()

    init {
        initFirebaseConnection()
    }

    private fun initFirebaseConnection() {
        database
            .getReference("workouts")
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newWorkout = dataSnapshot.getValue(Workout::class.java)

                    newWorkout?.let {
                        _workouts += newWorkout

                        if (newWorkout.uid.equals(currentUser?.uid))
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

        database
            .getReference("users")
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newUser = dataSnapshot.getValue(FirebaseUser::class.java)

                    newUser?.let { _users += Pair(it.uid, it) }
                }

                override fun onChildChanged(
                    dataSnapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    val user = dataSnapshot.getValue(FirebaseUser::class.java)
                    user?.let { _users.replace(it.uid, it) }
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(FirebaseUser::class.java)
                    user?.let { _users.remove(it.uid) }
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

    suspend fun saveUser(user: FirebaseUser) {
        if (_users.containsKey(user.uid))
            return

        val key = database.reference
            .child("users")
            .push().key ?: return

        database.reference
            .child("users")
            .child(key)
            .setValue(user)
    }

}