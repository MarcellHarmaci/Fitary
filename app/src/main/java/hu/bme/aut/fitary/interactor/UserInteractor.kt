package hu.bme.aut.fitary.interactor

import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.fitary.data.DomainUser
import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun getUsernameById(userId: String): String? =
        firebaseDataSource.getUserById(userId)?.username

    suspend fun saveUser(firebaseUser: FirebaseUser?) {
        firebaseDataSource.saveUser(
            DomainUser(
                firebaseUser?.uid ?: return,
                firebaseUser.email ?: return,
                firebaseUser.displayName ?: return
            )
        )
    }
}
