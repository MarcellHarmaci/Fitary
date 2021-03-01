package hu.bme.aut.fitary.dataSource

import hu.bme.aut.fitary.data.DomainExercise
import hu.bme.aut.fitary.data.DomainUser
import hu.bme.aut.fitary.data.DomainWorkout
import hu.bme.aut.fitary.dataSource.model.UserProfile
import hu.bme.aut.fitary.dataSource.model.Workout
import javax.inject.Inject

// Responsibility: Mapping from data models to domain models
class FirebaseDataSource @Inject constructor(
    private val firebaseDAO: FirebaseDAO
) {

    suspend fun getAllWorkouts(): List<DomainWorkout> {
        val domainWorkouts = mutableListOf<DomainWorkout>()

        for (workout in firebaseDAO.workouts) {
            // Skip workout with non-existent user
            val user = getUserById(workout.uid) ?: continue

            val domainExercises = mutableListOf<DomainExercise>()

            for (exerciseIdAndReps in workout.exercisesAndReps) {
                val exerciseId = exerciseIdAndReps.first
                domainExercises += DomainExercise(
                    id = exerciseId,
                    reps = exerciseIdAndReps.second,
                    name = firebaseDAO.getExerciseById(exerciseId)?.name ?: "Unknown exercise"
                )
            }

            domainWorkouts += DomainWorkout(
                uid = workout.uid,
                userName = user.username,
                domainExercises = domainExercises,
                score = workout.score,
                comment = workout.comment
            )
        }

        return domainWorkouts.toList()
    }

    suspend fun getUserWorkouts(): List<DomainWorkout> {
        val user = firebaseDAO.currentUser ?: return listOf<DomainWorkout>()
        val domainWorkouts = mutableListOf<DomainWorkout>()

        for (workout in firebaseDAO.userWorkouts) {
            val domainExercises = mutableListOf<DomainExercise>()

            for (exerciseIdAndReps in workout.exercisesAndReps) {
                val exerciseId = exerciseIdAndReps.first
                domainExercises += DomainExercise(
                    id = exerciseId,
                    reps = exerciseIdAndReps.second,
                    name = firebaseDAO.getExerciseById(exerciseId)?.name ?: "Unknown exercise"
                )
            }

            domainWorkouts += DomainWorkout(
                uid = workout.uid,
                userName = user.username,
                domainExercises = domainExercises,
                score = workout.score,
                comment = workout.comment
            )
        }

        return domainWorkouts.toList()
    }

    suspend fun saveWorkout(domainWorkout: DomainWorkout) {
        // User logged in to save workout
        val user = firebaseDAO.currentUser ?: return

        val exercises = mutableListOf<Pair<Long, Int>>()
        // Map exercises
        for (domainExercise in domainWorkout.domainExercises) {
            exercises += Pair(domainExercise.id, domainExercise.reps)
        }

        val newWorkout = Workout(
            uid = user.id,
            exercisesAndReps = exercises,
            score = domainWorkout.score,
            comment = domainWorkout.comment
        )

        firebaseDAO.saveWorkout(newWorkout)
    }

    suspend fun saveUser(domainUser: DomainUser) {
        val newUser = UserProfile(
            domainUser.id,
            domainUser.userMail,
            domainUser.username
        )

        firebaseDAO.saveUser(newUser)
    }

    suspend fun getUserById(userId: String): DomainUser? {
        for (user in firebaseDAO.users) {
            if (user.key == userId)
                return DomainUser(
                    user.value.id,
                    user.value.userMail,
                    user.value.username
                )
        }

        return null
    }
}