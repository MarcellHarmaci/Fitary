package hu.bme.aut.fitary.dataSource

import hu.bme.aut.fitary.data.DomainExercise
import hu.bme.aut.fitary.data.DomainUser
import hu.bme.aut.fitary.data.DomainWorkout
import hu.bme.aut.fitary.dataSource.model.Exercise
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

        return workoutDAO.workouts.map { workout ->

            val domainExercises = workout.exercisesAndReps.map { exerciseIdAndReps ->
                val exerciseId = exerciseIdAndReps.first

                DomainExercise(
                    id = exerciseId,
                    reps = exerciseIdAndReps.second,
                    name = exerciseDAO.getExerciseById(exerciseId)?.name ?: "Unknown exercise"
                )
            }.toMutableList()

            DomainWorkout(
                uid = workout.uid ?: "Unknown user",
                userName = getUserById(workout.uid)?.username ?: "No username",
                domainExercises = domainExercises,
                score = workout.score,
                comment = workout.comment
            )
        }
    }

    suspend fun getUserWorkouts(): List<DomainWorkout> {
        val user = userDAO.currentUser ?: return listOf<DomainWorkout>()

        return workoutDAO.userWorkouts.map { workout ->

            val domainExercises =
                workout.exercisesAndReps.map { exerciseIdAndReps ->
                    val exerciseId = exerciseIdAndReps.first
                    DomainExercise(
                        id = exerciseId,
                        reps = exerciseIdAndReps.second,
                        name = exerciseDAO.getExerciseById(exerciseId)?.name ?: "Unknown exercise"
                    )
                }.toMutableList()

            DomainWorkout(
                uid = user.id!!,
                userName = user.username,
                domainExercises = domainExercises,
                score = workout.score,
                comment = workout.comment
            )
        }
    }

    suspend fun saveWorkout(domainWorkout: DomainWorkout) {
        // User must be logged in to save a workout
        val user = userDAO.currentUser ?: return

        var score = 0.0
        val exercises = domainWorkout.domainExercises.mapNotNull { domainExercise ->

            score += domainExercise.reps * getExerciseScoreById(domainExercise.id)

            if (domainExercise.id == null)
                null
            else
                Pair(domainExercise.id, domainExercise.reps)

        }.toMutableList()

        val newWorkout = Workout(
            uid = user.id,
            exercisesAndReps = exercises,
            score = score,
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

    suspend fun getUserById(userId: String?): DomainUser? {

        return userDAO.users[userId]?.let { user ->
            DomainUser(
                user.id,
                user.userMail,
                user.username
            )
        }
    }

    suspend fun getExercises(): Map<Long?, DomainExercise> {
        return exerciseDAO.exercises.map { dataExercise ->
            Pair(
                dataExercise.key,
                DomainExercise(
                    id = dataExercise.key,
                    name = dataExercise.value.name,
                    reps = 0
                )
            )
        }.toMap().filter { pair -> pair.key != null }
    }

    suspend fun getExerciseScoreById(id: Long?) = exerciseDAO.exercises[id]?.score ?: 1.0

}