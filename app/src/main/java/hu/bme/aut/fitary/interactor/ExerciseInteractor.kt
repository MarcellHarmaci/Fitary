package hu.bme.aut.fitary.interactor

import hu.bme.aut.fitary.dataSource.FirebaseDataSource
import javax.inject.Inject

class ExerciseInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    suspend fun getExercises() = firebaseDataSource.getExercises()

    suspend fun getExerciseNames() = firebaseDataSource.getExercises().map { it.value.name }

    suspend fun getExerciseScoreById(id: Long) = firebaseDataSource.getExerciseScoreById(id)

    suspend fun getExerciseScoresByNames(): Map<String, Double> {

        return firebaseDataSource.getExercises().map {
            val name = it.value.name
            val score = it.value.scorePerRep

            Pair(name, score)
        }.toMap()
    }

    suspend fun getExerciseIdByName(name: String): Long? {
        return firebaseDataSource.getExercises().filterValues { domainExercise ->
            domainExercise.name == name
        }.keys.toList()[0]
    }

}