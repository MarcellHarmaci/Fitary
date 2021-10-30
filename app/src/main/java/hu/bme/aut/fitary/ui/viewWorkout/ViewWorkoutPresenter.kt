package hu.bme.aut.fitary.ui.viewWorkout

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.fitary.interactor.UserInteractor
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import javax.inject.Inject

class ViewWorkoutPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
    private val userInteractor: UserInteractor
) {

    suspend fun loadWorkout(workoutId: String): Workout? = withIOContext {
        TODO("Not yet implemented")
    }

    // Presentation model
    data class Workout(
        val id: String?,
        val title: String,
        val exercises: List<Exercise> = listOf(),
        val score: Double = 0.0,
        val author: String,
        val avatar: ByteArray? = null
    ) {
        override fun toString(): String {
            val isAvatarNull = if (avatar == null) {
                "null"
            } else {
                "notNull"
            }
            return "Workout(id=$id, score=$score, title='$title', avatar=$isAvatarNull)"
        }
    }

    data class Exercise(
        val name: String = "",
        val reps: Int = 0,
        val score: Double = 0.0
    )
}