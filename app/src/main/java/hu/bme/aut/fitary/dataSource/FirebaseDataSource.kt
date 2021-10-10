package hu.bme.aut.fitary.dataSource

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import hu.bme.aut.fitary.dataSource.model.UserProfile
import hu.bme.aut.fitary.dataSource.model.Workout
import hu.bme.aut.fitary.domainModel.DomainExercise
import hu.bme.aut.fitary.domainModel.DomainUser
import hu.bme.aut.fitary.domainModel.DomainWorkout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Responsibilities:
 * Mapping from data models to domain models;
 * Providing access to CRUD operations.
 */
@Singleton
class FirebaseDataSource @Inject constructor(
    private val userDAO: UserDAO,
    private val exerciseDAO: ExerciseDAO,
    private val workoutDAO: WorkoutDAO
) {

    var workoutsFlow: Flow<List<DomainWorkout>> = workoutDAO.workoutsFlow.map {
        it.map { workout ->
            var score = 0.0

            for (i in 0 until workout.exercises.size) {
                val scorePerRep = exerciseDAO.getExerciseScoreById(workout.exercises[i]) ?: 0.0
                score += scorePerRep * workout.reps[i]
            }

            DomainWorkout(
                id = workout.id,
                uid = workout.uid ?: "Unknown user",
                username = userDAO.users[workout.uid]?.username ?: "No username",
                domainExercises = mapWorkoutExercisesToDomain(workout),
                score = score,
                comment = workout.comment
            )
        }
    }

    // TODO Improve mapping code style
    //  docs: https://rainbowcake.dev/best-practices/mapping-code-style/
    private suspend fun mapWorkoutExercisesToDomain(workout: Workout): MutableList<DomainExercise> {
        val domainExercises = mutableListOf<DomainExercise>()

        for (i in 0 until workout.exercises.size) {
            val exerciseId = workout.exercises[i]

            domainExercises += DomainExercise(
                id = exerciseId,
                name = exerciseDAO.getExerciseById(exerciseId)?.name ?: "Unknown exercise",
                reps = workout.reps[i],
                scorePerRep = exerciseDAO.getExerciseScoreById(exerciseId) ?: 1.0
            )
        }

        return domainExercises
    }

    suspend fun saveUser(domainUser: DomainUser) {
        val newUser = UserProfile(
            id = domainUser.id,
            mail = domainUser.mail,
            username = domainUser.username
        )

        userDAO.saveUser(newUser)
    }

    suspend fun getCurrentUserId() = userDAO.getCurrentUserId()

    suspend fun getCurrentUser(): DomainUser? {
        return userDAO.currentUser?.let { userProfile ->
            DomainUser(
                id = userProfile.id,
                mail = userProfile.mail,
                username = userProfile.username
            )
        }
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
                    scorePerRep = dataExercise.value.score
                )
            )
        }.toMap().filter { pair -> pair.key != null }
    }

    suspend fun getExerciseScoreById(id: Long?) = exerciseDAO.exercises[id]?.score ?: 1.0

    suspend fun saveWorkout(
        domainWorkout: DomainWorkout,
        onSuccessListener: OnSuccessListener<Void>,
        onFailureListener: OnFailureListener
    ) {
        val exerciseList = mutableListOf<Long>()
        val repetitionList = mutableListOf<Int>()

        for (exercise in domainWorkout.domainExercises) {
            if (exercise.id == null) continue

            exerciseList += exercise.id
            repetitionList += exercise.reps
        }

        val newWorkout = Workout(
            id = domainWorkout.id ?: "",
            uid = domainWorkout.uid,
            exercises = exerciseList,
            reps = repetitionList,
            comment = domainWorkout.comment
        )

        val key = domainWorkout.id
        if (key.isNullOrBlank()) {
            workoutDAO.saveWorkout(newWorkout, onSuccessListener, onFailureListener)
        }
        else {
            workoutDAO.updateWorkout(key, newWorkout, onSuccessListener, onFailureListener)
        }
    }

}