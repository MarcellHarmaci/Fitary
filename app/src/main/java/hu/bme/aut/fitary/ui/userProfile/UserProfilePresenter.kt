package hu.bme.aut.fitary.ui.userProfile

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.fitary.domainModel.DomainUser
import hu.bme.aut.fitary.interactor.UserInteractor
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import java.io.*
import javax.inject.Inject

class UserProfilePresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val workoutInteractor: WorkoutInteractor
) {

    suspend fun loadUserProfile() = withIOContext {
        val domainUser = userInteractor.getCurrentUser()
        val numberOfWorkouts = workoutInteractor.userWorkoutsFlow.value.size
        val fullScore = workoutInteractor.userWorkoutsFlow.value.sumOf { it.score }

        UserProfile(
            userId = domainUser?.id,
            username = domainUser?.username ?: "No username",
            userMail = domainUser?.mail ?: "No email address",
            numberOfWorkouts = numberOfWorkouts,
            fullScore = fullScore,
            avatar = domainUser?.avatar
        )
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getByteArrayFromImagePath(path: String): ByteArray? = withIOContext {
        val file = File(path)
        val fileSize = file.length().toInt()
        val byteArray = ByteArray(fileSize)

        try {
            val stream = BufferedInputStream(FileInputStream(file))
            stream.read(byteArray, 0, byteArray.size)
            stream.close()
        } catch (exception: Exception) {
            when (exception) {
                is FileNotFoundException, is IOException -> {
                    exception.printStackTrace()
                    return@withIOContext null
                }
                else -> throw exception
            }
        }

        return@withIOContext byteArray
    }

    suspend fun save(userProfile: UserProfile) = withIOContext {
            userInteractor.updateUser(
                DomainUser(
                    id = userProfile.userId,
                    username = userProfile.username,
                    mail = userProfile.userMail,
                    avatar = userProfile.avatar
                )
            )
        }

    // Presentation model
    data class UserProfile(
        val userId: String?,
        val username: String,
        val userMail: String,
        val numberOfWorkouts: Int,
        val fullScore: Double,
        var avatar: ByteArray? = null
    ) {
        override fun toString(): String {
            val isAvatarNull = if (avatar == null) {
                "null"
            } else {
                "notNull"
            }
            return "UserProfile(" +
                    "userId=$userId, " +
                    "username='$username', " +
                    "userMail='$userMail', " +
                    "numberOfWorkouts=$numberOfWorkouts, " +
                    "fullScore=$fullScore, " +
                    "avatar=$isAvatarNull" +
                    ")"
        }
    }
}
