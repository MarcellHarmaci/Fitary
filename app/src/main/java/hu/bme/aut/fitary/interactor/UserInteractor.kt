package hu.bme.aut.fitary.interactor

import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun getUsernameById(userId: String): String? =
        firebaseDataSource.getUserById(userId)?.username
}
