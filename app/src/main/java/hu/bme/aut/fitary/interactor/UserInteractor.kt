package hu.bme.aut.fitary.interactor

import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import hu.bme.aut.fitary.domainModel.DomainUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    suspend fun getCurrentUser() = firebaseDataSource.getCurrentUser()

    suspend fun getUsernameById(userId: String): String? =
        firebaseDataSource.getUserById(userId)?.username

    suspend fun getAvatarById(id: String?): ByteArray? {
        id ?: return null

        return firebaseDataSource.userFlow.value[id]?.avatar
    }

    suspend fun saveUser(firebaseUser: FirebaseUser?) {
        val user = DomainUser(
            id = firebaseUser?.uid ?: return,
            mail = firebaseUser.email ?: return,
            username = firebaseUser.displayName ?: return
        )

        firebaseDataSource.saveUser(user)
    }

    suspend fun updateUser(domainUser: DomainUser) =
        firebaseDataSource.updateUser(domainUser)

}
