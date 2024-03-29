package hu.bme.aut.fitary.dataSource

import android.util.Base64
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import hu.bme.aut.fitary.dataSource.model.User
import hu.bme.aut.fitary.dataSource.model.Workout
import hu.bme.aut.fitary.domainModel.DomainExercise
import hu.bme.aut.fitary.domainModel.DomainUser
import hu.bme.aut.fitary.domainModel.DomainWorkout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    var workoutsFlow: StateFlow<List<DomainWorkout>> = workoutDAO.workoutsFlow.map {
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
                title = workout.title
            )
        }
    }.stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        initialValue = listOf()
    )

    val userFlow: StateFlow<Map<String, DomainUser>> = userDAO.userFlow.map {
        it.map { dataPair ->
            Pair(
                dataPair.key,
                DomainUser(
                    id = dataPair.key,
                    mail = dataPair.value.mail,
                    username = dataPair.value.username,
                    avatar = dataPair.value.avatar?.let { encodedAvatar ->
                        Base64.decode(encodedAvatar, Base64.DEFAULT)
                    }
                )
            )
        }.toMap()
    }.stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        initialValue = mapOf()
    )

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
        val newUser = User(
            id = domainUser.id,
            mail = domainUser.mail,
            username = domainUser.username
        )

        userDAO.saveUser(newUser)
    }

    suspend fun updateUser(domainUser: DomainUser) {
        val user = User(
            id = domainUser.id,
            mail = domainUser.mail,
            username = domainUser.username,
            avatar = Base64.encodeToString(domainUser.avatar, Base64.DEFAULT)
        )

        userDAO.updateUser(user)
    }

    suspend fun getCurrentUserId() = userDAO.getCurrentUserId()

    suspend fun getCurrentUser(): DomainUser? {
        return userDAO.currentUser?.let { userProfile ->
            DomainUser(
                id = userProfile.id,
                mail = userProfile.mail,
                username = userProfile.username,
                avatar = userProfile.avatar?.let {
                    Base64.decode(userProfile.avatar, Base64.DEFAULT)
                }
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
        val exercises = exerciseDAO.exercises.map { dataExercise ->
            Pair(
                dataExercise.key,
                DomainExercise(
                    id = dataExercise.key,
                    name = dataExercise.value.name,
                    scorePerRep = dataExercise.value.score
                )
            )
        }.toMap().filter { pair -> pair.key != null }

        return exercises.toSortedMap(compareBy { exercises[it]?.name })
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
            title = domainWorkout.title
        )

        val key = domainWorkout.id
        if (key.isNullOrBlank()) {
            workoutDAO.saveWorkout(newWorkout, onSuccessListener, onFailureListener)
        }
        else {
            workoutDAO.updateWorkout(key, newWorkout, onSuccessListener, onFailureListener)
        }
    }

    suspend fun deleteWorkoutByKey(key: String) = workoutDAO.deleteWorkout(key)

}