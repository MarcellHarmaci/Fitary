package hu.bme.aut.fitary.dataSource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import hu.bme.aut.fitary.dataSource.model.UserProfile
import hu.bme.aut.fitary.dataSource.model.Workout
import hu.bme.aut.fitary.domainModel.DomainExercise
import hu.bme.aut.fitary.domainModel.DomainUser
import hu.bme.aut.fitary.domainModel.DomainWorkout
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

            var score = 0.0
            for (i in 0 until workout.exercises.size) {
                val scorePerRep = exerciseDAO.getExerciseScoreById(workout.exercises[i]) ?: 0.0
                score += scorePerRep * workout.reps[i]
            }

            DomainWorkout(
                uid = workout.uid ?: "Unknown user",
                username = userDAO.users[workout.uid]?.username ?: "No username",
                domainExercises = mapWorkoutExercisesToDomain(workout),
                score = score,
                comment = workout.comment
            )
        }.toMutableList()
    }

    init {
        workoutDAO.workouts.observeForever(workoutObserver)
    }

    // TODO Improve mapping code style
    //  docs: https://rainbowcake.dev/best-practices/mapping-code-style/
    private fun mapWorkoutExercisesToDomain(workout: Workout): MutableList<DomainExercise> {
        val domainExercises = mutableListOf<DomainExercise>()

        for (i in 0 until workout.exercises.size) {
            val exerciseId = workout.exercises[i]

            domainExercises += DomainExercise(
                id = exerciseId,
                reps = workout.reps[i],
                name = exerciseDAO.getExerciseById(exerciseId)?.name ?: "Unknown exercise"
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
            uid = domainWorkout.uid,
            exercises = exerciseList,
            reps = repetitionList,
            comment = domainWorkout.comment
        )

        workoutDAO.saveWorkout(newWorkout, onSuccessListener, onFailureListener)
    }

}