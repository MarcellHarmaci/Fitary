package hu.bme.aut.fitary.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import hu.bme.aut.fitary.data.DomainExercise
import hu.bme.aut.fitary.data.DomainUser
import hu.bme.aut.fitary.data.DomainWorkout
import hu.bme.aut.fitary.dataSource.model.UserProfile
import hu.bme.aut.fitary.dataSource.model.Workout
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/* Responsibilities:
    Mapping from data models to domain models
    Providing access to CRUD operations
 */
@Singleton
class FirebaseDataSource @Inject constructor(
    private val userDAO: UserDAO,
    private val exerciseDAO: ExerciseDAO,
    private val workoutDAO: WorkoutDAO
) {

    val workouts = MutableLiveData<MutableList<DomainWorkout>>()

    private val workoutObserver = Observer<MutableList<Workout>> {
        workouts.value = it.map { workout ->
            DomainWorkout(
                uid = workout.uid ?: "Unknown user",
                username = "No username", //getUserById(workout.uid)?.username ?: "No username",
                domainExercises = mapWorkoutExercisesToDomain(workout),
                score = workout.score,
                comment = workout.comment
            )
        }.toMutableList()
    }

    init {
        workoutDAO.workouts.observeForever(workoutObserver)
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

            // TODO Compute this in business logic layer
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

    suspend fun getCurrentUser() = userDAO.currentUser

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