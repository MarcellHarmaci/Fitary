package hu.bme.aut.fitary.ui.charts.pieChart

import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PieChartPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
) {

    val exercises: Flow<List<Exercise>> = workoutInteractor.userWorkoutsFlow.map {
        val exerciseMap = mutableMapOf<String, Double>()

        it.forEach { domainWorkout ->
            domainWorkout.domainExercises.forEach { domainExercise ->

                val currentScore: Double? = exerciseMap[domainExercise.name]

                if (currentScore == null) {
                    exerciseMap += Pair(domainExercise.name, domainExercise.score)
                } else {
                    exerciseMap[domainExercise.name] = currentScore + domainExercise.score
                }
            }
        }

        exerciseMap.map { pair ->
            Exercise(
                name = pair.key,
                sumOfScore = pair.value
            )
        }
    }

    // Presentation model
    data class Exercise(
        val name: String,
        val sumOfScore: Double
    )
}
