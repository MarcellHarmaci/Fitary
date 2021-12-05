package hu.bme.aut.fitary.dataSource

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.dataSource.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDAO @Inject constructor() {

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val currentUser: User?
        get() = users[auth.currentUser?.uid]

    private val _users = mutableMapOf<String, User>()
    val users: Map<String, User>
        get() = _users.toMap()

    val userFlow = MutableStateFlow<Map<String, User>>(mapOf())

    // SingleThreadContext to avoid concurrent modification of user map
    @ObsoleteCoroutinesApi
    val userDaoContext = newSingleThreadContext("UserDaoContext")

    init {
        database
            .getReference("users")
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newUser = dataSnapshot.getValue(User::class.java)

                    newUser?.id?.let {
                        CoroutineScope(userDaoContext).launch {
                            _users += Pair(newUser.id, newUser)
                            userFlow.emit(users)
                        }
                    }
                }

                @RequiresApi(Build.VERSION_CODES.N)
                override fun onChildChanged(
                    dataSnapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    val user = dataSnapshot.getValue(User::class.java)

                    user?.id?.let {
                        CoroutineScope(userDaoContext).launch {
                            _users.replace(user.id, user)
                            userFlow.emit(users)
                        }
                    }
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)
                    user?.let { _users.remove(it.id) }
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {
                    val dbException = error.toException()

                    Timber.e(dbException, error.details)
                    throw dbException
                }
            })
    }

    suspend fun getCurrentUserId() = auth.currentUser!!.uid

    suspend fun saveUser(user: User) {
        if (user.id == null || _users.containsKey(user.id))
            return // Return if user id already exists

        database.reference
            .child("users")
            .child(user.id)
            .setValue(user)
    }

    suspend fun updateUser(user: User) {
        if (user.id == null || !_users.containsKey(user.id))
            return

        database.reference
            .child("users")
            .child(user.id)
            .setValue(user)
    }

}