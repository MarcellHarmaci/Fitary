package hu.bme.aut.fitary.interactor

import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import javax.inject.Inject

class ExerciseInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    suspend fun getExercises() = firebaseDataSource.getExercises()

    suspend fun getExerciseNames() = firebaseDataSource.getExercises().map { it.value.name }

    suspend fun getExerciseScoreById(id: Long) = firebaseDataSource.getExerciseScoreById(id)

}