package hu.bme.aut.fitary.dataSource

import hu.bme.aut.fitary.data.DomainExercise
import hu.bme.aut.fitary.data.DomainUser
import hu.bme.aut.fitary.data.DomainWorkout
import hu.bme.aut.fitary.dataSource.model.UserProfile
import hu.bme.aut.fitary.dataSource.model.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
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

    val workoutChannel = Channel<DomainWorkout>()

    init {
        CoroutineScope(Dispatchers.IO).launch {

            workoutDAO.workouts.consumeEach { workout ->

                workoutChannel.send(
                    DomainWorkout(
                        uid = workout.uid ?: "Unknown user",
                        username = getUserById(workout.uid)?.username ?: "No username",
                        domainExercises = mapWorkoutExercisesToDomain(workout),
                        score = workout.score,
                        comment = workout.comment
                    )
                )

            }
        }
    }

    private fun mapWorkoutExercisesToDomain(workout: Workout): MutableList<DomainExercise> {
        val domainExercises = mutableListOf<DomainExercise>()

        for (i in 1 until workout.exercises.size) {
            val exerciseId = workout.exercises[i]

            domainExercises += DomainExercise(
                id = exerciseId,
                reps = workout.reps[i],
                name = exerciseDAO.getExerciseById(exerciseId)?.name ?: "Unknown exercise"
            )
        }

        return domainExercises
    }

    suspend fun getUserWorkouts(): List<DomainWorkout> {
        val user = userDAO.currentUser ?: return listOf<DomainWorkout>()

        return workoutDAO.userWorkouts.map { workout ->

            DomainWorkout(
                uid = user.id ?: "Unknown user",
                username = user.username,
                domainExercises = mapWorkoutExercisesToDomain(workout),
                score = workout.score,
                comment = workout.comment
            )
        }
    }

    suspend fun saveWorkout(domainWorkout: DomainWorkout) {
        // User must be logged in to save a workout
        val user = userDAO.currentUser ?: return

        var score = 0.0
        val exercises = mutableListOf<Long>()
        val reps = mutableListOf<Int>()
        for (exercise in domainWorkout.domainExercises) {
            if (exercise.id == null) continue

            exercises += exercise.id
            reps += exercise.reps

            score += exercise.reps * getExerciseScoreById(exercise.id)
        }

        val newWorkout = Workout(
            uid = user.id,
            exercises = exercises,
            reps = reps,
            score = score,
            comment = domainWorkout.comment
        )

        workoutDAO.saveWorkout(newWorkout)
    }

    suspend fun saveUser(domainUser: DomainUser) {
        val newUser = UserProfile(
            id = domainUser.id,
            mail = domainUser.mail,
            username = domainUser.username
        )

        userDAO.saveUser(newUser)
    }

    suspend fun getUserById(userId: String?): DomainUser? {

        return userDAO.users[userId]?.let { user ->
            DomainUser(
                id = user.id,
                mail = user.mail,
                username = user.username
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