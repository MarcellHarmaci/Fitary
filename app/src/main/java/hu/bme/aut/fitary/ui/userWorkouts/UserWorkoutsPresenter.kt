package hu.bme.aut.fitary.ui.userWorkouts

import hu.bme.aut.fitary.domainModel.DomainWorkout
import hu.bme.aut.fitary.interactor.Observer
import hu.bme.aut.fitary.interactor.WorkoutInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class UserWorkoutsPresenter @Inject constructor(
    private val workoutInteractor: WorkoutInteractor,
) : Observer<MutableList<DomainWorkout>> {

    val workoutsChannel = Channel<MutableList<Workout>>()
    private var workouts = mutableListOf<Workout>()

    init {
        Timber.d("Adding UserWorkoutsPresenter to observe")
        workoutInteractor.addObserver(this)
    }

    override fun notify(newValue: MutableList<DomainWorkout>) {
        Timber.d("UserWorkoutsPresenter notified")

        CoroutineScope(Dispatchers.IO).launch {

            workouts = newValue.mapNotNull { domainWorkout ->
                domainWorkout.id?.let { workoutId ->
                    Workout(
                        id = workoutId,
                        score = domainWorkout.score,
                        comment = domainWorkout.comment ?: "-"
                    )
                }
            }.toMutableList()

            workoutsChannel.send(workouts)
        }
    }

    // Presentation model
    data class Workout(
        val id: String,
        val score: Double,
        val comment: String
    )
}