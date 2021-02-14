package hu.bme.aut.fitary.interactor

import hu.bme.aut.fitary.dataSource.FirebaseDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutInteractor @Inject constructor(
    private val firebaseDAO: FirebaseDAO
) {

    // TODO Mapping from data models to domain models
    fun getWorkouts() = firebaseDAO.workouts
    fun getUserWorkouts() = firebaseDAO.userWorkouts
}