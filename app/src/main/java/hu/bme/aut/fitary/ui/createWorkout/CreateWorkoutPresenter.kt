package hu.bme.aut.fitary.ui.createWorkout

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.fitary.domainModel.DomainWorkout
import hu.bme.aut.fitary.interactor.ExerciseInteractor
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import javax.inject.Inject

class CreateWorkoutPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
    private val exerciseInteractor: ExerciseInteractor
) {

    suspend fun getExerciseNames() = withIOContext {
        exerciseInteractor.getExerciseNames()
    }

    suspend fun getExerciseScores() = withIOContext {
        exerciseInteractor.getExerciseScoresByNames()
    }

    // TODO Implement on IO thread
    fun saveWorkout(workout: DomainWorkout) {}

    // Presentation model
    data class Exercise(
        var name: String = "",
        var reps: Int = 0,
        var scorePerRep: Double = 1.0
    ) {
        val score: Double
            get() = reps * scorePerRep
    }
}