package hu.bme.aut.fitary.ui.userWorkouts

import hu.bme.aut.fitary.domainModel.DomainWorkout
import hu.bme.aut.fitary.interactor.Observer
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserWorkoutsPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
) : Observer<MutableList<DomainWorkout>> {

    val workoutsChannel = Channel<MutableList<Workout>>()

    init {
        workoutInteractor.addObserver(this)
    }

    override fun notify(newValue: MutableList<DomainWorkout>) {
        CoroutineScope(Dispatchers.IO).launch {

            val workouts = newValue.map { domainWorkout ->
                    Workout(
                        score = domainWorkout.score,
                        comment = domainWorkout.comment ?: "-"
                    )
            }.toMutableList()

            workoutsChannel.send(workouts)
        }
    }

    // Presentation model
    data class Workout(
        val score: Double,
        val comment: String
    )
}