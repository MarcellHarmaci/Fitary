package hu.bme.aut.fitary.dataSource

import hu.bme.aut.fitary.data.DomainExercise
import hu.bme.aut.fitary.data.DomainUser
import hu.bme.aut.fitary.data.DomainWorkout
import hu.bme.aut.fitary.dataSource.model.UserProfile
import hu.bme.aut.fitary.dataSource.model.Workout
import javax.inject.Inject

/* Responsibilities:
    Mapping from data models to domain models
    Providing access to CRUD operations
 */
class FirebaseDataSource @Inject constructor(
    private val userDAO: UserDAO,
    private val exerciseDAO: ExerciseDAO,
    private val workoutDAO: WorkoutDAO
) {

    suspend fun getAllWorkouts(): List<DomainWorkout> {
        val domainWorkouts = mutableListOf<DomainWorkout>()

        for (workout in workoutDAO.workouts) {
            // Skip workout with non-existent user
            val user = getUserById(workout.uid) ?: continue

            val domainExercises = mutableListOf<DomainExercise>()

            for (exerciseIdAndReps in workout.exercisesAndReps) {
                val exerciseId = exerciseIdAndReps.first
                domainExercises += DomainExercise(
                    id = exerciseId,
                    reps = exerciseIdAndReps.second,
                    name = exerciseDAO.getExerciseById(exerciseId)?.name ?: "Unknown exercise"
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
        val user = userDAO.currentUser ?: return listOf<DomainWorkout>()
        val domainWorkouts = mutableListOf<DomainWorkout>()

        for (workout in workoutDAO.userWorkouts) {
            val domainExercises = mutableListOf<DomainExercise>()

            for (exerciseIdAndReps in workout.exercisesAndReps) {
                val exerciseId = exerciseIdAndReps.first
                domainExercises += DomainExercise(
                    id = exerciseId,
                    reps = exerciseIdAndReps.second,
                    name = exerciseDAO.getExerciseById(exerciseId)?.name ?: "Unknown exercise"
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
        val user = userDAO.currentUser ?: return

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

        workoutDAO.saveWorkout(newWorkout)
    }

    suspend fun saveUser(domainUser: DomainUser) {
        val newUser = UserProfile(
            domainUser.id,
            domainUser.userMail,
            domainUser.username
        )

        userDAO.saveUser(newUser)
    }

    suspend fun getUserById(userId: String): DomainUser? {
        val user = userDAO.users[userId] ?: return null

        return DomainUser(
            user.id,
            user.userMail,
            user.username
        )
    }
}