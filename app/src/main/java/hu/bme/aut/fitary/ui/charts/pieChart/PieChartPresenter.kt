package hu.bme.aut.fitary.ui.charts.pieChart

import hu.bme.aut.fitary.domainModel.DomainWorkout
import hu.bme.aut.fitary.interactor.Observer
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class PieChartPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
) : Observer<MutableList<DomainWorkout>> {

    var exercisesChannel = Channel<List<Exercise>>()

    init {
        Timber.d("Adding PieChartPresenter to observe")
        workoutInteractor.addObserver(this)
    }

    override fun notify(newValue: MutableList<DomainWorkout>) {
        Timber.d("PieChartPresenter notified")

        CoroutineScope(Dispatchers.Default).launch {

            val exerciseMap = mutableMapOf<String, Double>()

            newValue.forEach { domainWorkout ->
                domainWorkout.domainExercises.forEach { domainExercise ->

                    val currentScore: Double? = exerciseMap[domainExercise.name]

                    if (currentScore == null) {
                        Timber.d("Exercise \"${domainExercise.name}\" does not exist > " +
                                "score: ${domainExercise.score}")
                        exerciseMap += Pair(domainExercise.name, domainExercise.score)
                    } else {
                        Timber.d("Exercise \"${domainExercise.name}\" already exists > " +
                                "${exerciseMap[domainExercise.name]} += ${domainExercise.score}")
                        exerciseMap[domainExercise.name] = currentScore + domainExercise.score
                        Timber.d("New value: ${exerciseMap[domainExercise.name]}")
                    }
                }
            }

            val exercises = exerciseMap.map {
                Exercise(
                    name = it.key,
                    sumOfScore = it.value
                )
            }

            exercisesChannel.send(exercises)
        }
    }

    // Presentation model
    data class Exercise(
        val name: String,
        val sumOfScore: Double
    )
}
