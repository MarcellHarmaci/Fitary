package hu.bme.aut.fitary.ui.socialWorkouts

sealed class SocialWorkoutsViewState

object Loading : SocialWorkoutsViewState()

data class SocialWorkoutsLoaded(
    val workouts: List<SocialWorkoutsPresenter.Workout>
) : SocialWorkoutsViewState()