package hu.bme.aut.fitary.ui.exerciseDialog

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class ExerciseDialogViewModel @Inject constructor(
    private val exerciseDialogPresenter: ExerciseDialogPresenter
): RainbowCakeViewModel<ExerciseDialogViewState>(Loading) {

    // Survives configuration changes
    // Tied to the ViewModel instance, which also survives
    fun loadExercise() = execute {
        // Set the Loading state while fetching data
        viewState = Loading

        // Suspending call -> Lets go of UI thread until it returns
        val exercise = exerciseDialogPresenter.getExercise()

        // Back on UI thread
        // Put data in view state
        viewState = ExerciseDialogReady(
            exercise.name,
            exercise.reps
        )
    }
}