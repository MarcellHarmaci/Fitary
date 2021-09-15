package hu.bme.aut.fitary.ui.charts.pieChart

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.fitary.domainModel.DomainWorkout
import hu.bme.aut.fitary.interactor.Observer
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

class PieChartPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
) {

    val exercises = MutableLiveData<List<Exercise>>()

    init {
        workoutInteractor.userWorkoutsLiveData.observeForever { observedWorkouts ->
            val exerciseMap = mutableMapOf<String, Double>()

            observedWorkouts.forEach { domainWorkout ->
                domainWorkout.domainExercises.forEach { domainExercise ->

                    val currentScore: Double? = exerciseMap[domainExercise.name]

                    if (currentScore == null) {
                        exerciseMap += Pair(domainExercise.name, domainExercise.score)
                    } else {
                        exerciseMap[domainExercise.name] = currentScore + domainExercise.score
                    }
                }
            }

            val mappedExercises = exerciseMap.map {
                Exercise(
                    name = it.key,
                    sumOfScore = it.value
                )
            }

            exercises.value = mappedExercises
        }
    }

    // Presentation model
    data class Exercise(
        val name: String,
        val sumOfScore: Double
    )
}
