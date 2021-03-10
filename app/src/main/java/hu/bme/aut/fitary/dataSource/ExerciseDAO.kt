package hu.bme.aut.fitary.dataSource

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.dataSource.model.Exercise
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseDAO @Inject constructor() {

    private val database = FirebaseDatabase.getInstance()

    private val _exercises = mutableMapOf<Long?, Exercise>()
    val exercises: Map<Long?, Exercise>
        get() = _exercises.toMap()

    init {
        database
            .getReference("exercises")
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newExercise = dataSnapshot.getValue(Exercise::class.java)

                    newExercise?.let {
                        _exercises += Pair(it.id, it)
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

    fun getExerciseById(id: Long) = exercises[id]

}