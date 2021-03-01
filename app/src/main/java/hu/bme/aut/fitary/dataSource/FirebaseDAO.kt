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
import javax.inject.Singleton

@Singleton
class FirebaseDAO {

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val currentUser: UserProfile?
        get() = users[auth.currentUser?.uid]

    private val _users = mutableMapOf<String, UserProfile>()
    val users: Map<String, UserProfile>
        get() = _users.toMap()

    private val _workouts = mutableListOf<Workout>()
    val workouts: List<Workout>
        get() = _workouts.toList()

    private val _userWorkouts = mutableListOf<Workout>()
    val userWorkouts
        get() = _userWorkouts.toList()

    private val _exercises = mutableMapOf<Long, Exercise>()
    val exercises: Map<Long, Exercise>
        get() = _exercises.toMap()

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

                        if (newWorkout.uid == currentUser?.id)
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
                    val newUser = dataSnapshot.getValue(UserProfile::class.java)

                    newUser?.let { _users += Pair(it.id, it) }
                }

                override fun onChildChanged(
                    dataSnapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    val user = dataSnapshot.getValue(UserProfile::class.java)
                    user?.let { _users.replace(it.id, it) }
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(UserProfile::class.java)
                    user?.let { _users.remove(it.id) }
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

    suspend fun saveUser(user: UserProfile) {
        if (_users.containsKey(user.id) && _users[user.id] == user)
            return

        val key = database.reference
            .child("users")
            .push().key ?: return

        database.reference
            .child("users")
            .child(key)
            .setValue(user)
    }

    suspend fun getExerciseById(id: Long) = exercises[id]

}