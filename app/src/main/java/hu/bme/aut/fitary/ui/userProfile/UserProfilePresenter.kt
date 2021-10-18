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
        val user = userInteractor.getCurrentUser()
        val numberOfWorkouts = workoutInteractor.userWorkoutsFlow.value.size
        val fullScore = workoutInteractor.userWorkoutsFlow.value.sumOf { it.score }

        UserProfile(
            userId = user?.id,
            username = user?.username ?: "No username",
            userMail = user?.mail ?: "No email address",
            numberOfWorkouts = numberOfWorkouts,
            fullScore = fullScore,
            avatar = user?.avatar
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

    suspend fun save(userId: String?, username: String, userMail: String, avatar: ByteArray?) =
        withIOContext {
            userInteractor.updateUser(
                DomainUser(
                    id = userId,
                    username = username,
                    mail = userMail,
                    avatar = avatar
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
        val avatar: ByteArray? = null
    )
}
