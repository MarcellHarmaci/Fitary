package hu.bme.aut.fitary.interactor

import hu.bme.aut.fitary.dataSource.FirebaseDAO
import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    suspend fun getWorkouts() = firebaseDataSource.getWorkouts()
    suspend fun getUserWorkouts() = firebaseDataSource.getUserWorkouts()
}