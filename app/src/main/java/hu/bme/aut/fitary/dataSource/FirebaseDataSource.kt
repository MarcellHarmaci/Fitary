package hu.bme.aut.fitary.dataSource

import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.fitary.data.Workout
import hu.bme.aut.fitary.dataSource.model.UserProfile
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    private val firebaseDAO: FirebaseDAO
) {

    // TODO Mapping from data models to domain models on the whole the public interface
    suspend fun getAllWorkouts() = firebaseDAO.workouts

    suspend fun getUserWorkouts() = firebaseDAO.userWorkouts

    suspend fun saveWorkout(workout: Workout) = firebaseDAO.saveWorkout(workout)

    suspend fun saveUser(firebaseUser: FirebaseUser) = firebaseDAO.saveUser(firebaseUser)

    suspend fun getUserById(userId: String): UserProfile? {
        for (user in firebaseDAO.users)
            if (user.id == userId) return user

        return null
    }
}