package hu.bme.aut.fitary.ui.exerciseDialog

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.fitary.data.DomainExercise
import javax.inject.Inject

class ExerciseDialogPresenter @Inject constructor() {

    // Runs on background thread pool
    suspend fun getExercise(): Exercise = withIOContext {
        // Fetch Exercise of domain model
        val domainExercise = DomainExercise()

        // Convert to Exercise of presentation model
        val exercise: Exercise = Exercise(
            domainExercise.name,
            domainExercise.reps
        )

        // Return formatted, screen specific data
        exercise
    }

    // Presentation model
    class Exercise(val name: String, val reps: Int)
}