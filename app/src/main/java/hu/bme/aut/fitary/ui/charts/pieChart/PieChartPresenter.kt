package hu.bme.aut.fitary.ui.charts.pieChart

import co.zsmb.rainbowcake.withIOContext
import com.github.mikephil.charting.data.PieEntry
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PieChartPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
) {

    val exercises: Flow<List<PieEntry>> = workoutInteractor.userWorkoutsFlow.map {
        val exerciseMap = mutableMapOf<String, Double>()

        // Build a map of exercises and their sum of scores
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

        convertToPieEntries(exerciseMap)
    }

    private suspend fun convertToPieEntries(exercises: Map<String, Double>) = withIOContext {
        val pieEntries: MutableList<PieEntry> = mutableListOf()
        var otherScore = 0.0

        for ((index, exercise) in exercises.toList().sortedByDescending { it.second }.withIndex()) {
            if (index < 5) {
                // Highest 5 gets their own pie entry
                pieEntries.add(
                    PieEntry(
                        exercise.second.toFloat(),
                        exercise.first
                    )
                )
            } else {
                // Others get a joint 'other' entry
                otherScore += exercise.second
            }
        }

        if (otherScore != 0.0) {
            pieEntries.add(
                PieEntry(
                    otherScore.toFloat(),
                    "Other"
                )
            )
        }

        return@withIOContext pieEntries.toList()
    }

    // The presentation model is a PieEntry
}
