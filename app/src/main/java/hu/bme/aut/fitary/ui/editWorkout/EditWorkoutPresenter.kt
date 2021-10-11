package hu.bme.aut.fitary.ui.editWorkout

import co.zsmb.rainbowcake.withIOContext
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import hu.bme.aut.fitary.domainModel.DomainExercise
import hu.bme.aut.fitary.domainModel.DomainWorkout
import hu.bme.aut.fitary.interactor.ExerciseInteractor
import hu.bme.aut.fitary.interactor.UserInteractor
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class EditWorkoutPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
    private val exerciseInteractor: ExerciseInteractor,
    private val userInteractor: UserInteractor
) {

    suspend fun getExerciseNames() = withIOContext {
        exerciseInteractor.getExerciseNames()
    }

    suspend fun getExerciseScores() = withIOContext {
        exerciseInteractor.getExerciseScoresByNames()
    }

    suspend fun saveWorkout(
        workoutId: String?,
        exercises: List<Exercise>,
        title: String?,
        onSuccessListener: OnSuccessListener<Void>,
        onFailureListener: OnFailureListener
    ) = withIOContext {

        val currentUser = userInteractor.getCurrentUser()
        var workoutScore = BigDecimal(0)

        val domainExercises = exercises.map {
            workoutScore += it.score

            DomainExercise(
                id = exerciseInteractor.getExerciseIdByName(it.name),
                name = it.name,
                reps = it.reps,
                scorePerRep = it.scorePerRep
            )
        }

        currentUser?.id?.let {
            val workout = DomainWorkout(
                id = workoutId,
                uid = it,
                username = currentUser.username,
                domainExercises = domainExercises.toMutableList(),
                score = workoutScore.toDouble(),
                title = title
            )

            workoutInteractor.saveWorkout(workout, onSuccessListener, onFailureListener)
        }
    }

    suspend fun loadWorkout(id: String): Workout? = withIOContext {
        val domainWorkout: DomainWorkout? = workoutInteractor.userWorkoutsFlow.value
            .find { it.id == id }

        domainWorkout?.let {
            val exercises = it.domainExercises.map { domainExercise ->
                Exercise(
                    name = domainExercise.name,
                    reps = domainExercise.reps,
                    scorePerRep = domainExercise.scorePerRep
                )
            }

            Workout(
                id = domainWorkout.id,
                exercises = exercises,
                title = domainWorkout.title
            )
        }
    }

    // Presentation model
    data class Workout(
        val id: String?,
        var exercises: List<Exercise> = listOf(),
        var title: String?
    ) {
        val score: BigDecimal
            get() = exercises.sumOf { it.score }
                .setScale(1, RoundingMode.HALF_EVEN)
    }

    data class Exercise(
        var name: String = "",
        var reps: Int = 0,
        var scorePerRep: Double = 1.0
    ) {
        val score: BigDecimal
            get() = BigDecimal(reps.toDouble() * scorePerRep)
                .setScale(1, RoundingMode.HALF_EVEN)
    }
}