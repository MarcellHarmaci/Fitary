package hu.bme.aut.fitary.dataSource

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.dataSource.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDAO @Inject constructor() {

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val currentUser: UserProfile?
        get() = users[auth.currentUser?.uid]

    private val _users = mutableMapOf<String?, UserProfile>()
    val users: Map<String?, UserProfile>
        get() = _users.toMap()

    init {
        database
            .getReference("users")
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newUser = dataSnapshot.getValue(UserProfile::class.java)

                    newUser?.let { _users += Pair(it.id, it) }
                }

                @RequiresApi(Build.VERSION_CODES.N)
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

    suspend fun getCurrentUserId() = auth.currentUser?.uid

    suspend fun saveUser(user: UserProfile) {
        // TODO remove this
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

    // TODO Separate function to update existing users

}