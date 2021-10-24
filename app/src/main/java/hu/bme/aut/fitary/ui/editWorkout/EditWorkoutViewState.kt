package hu.bme.aut.fitary.ui.editWorkout

sealed class EditWorkoutViewState

object Loading : EditWorkoutViewState()

data class Editing(
    val id: String? = null,
    val exercises: List<EditWorkoutPresenter.Exercise> = mutableListOf(),
    val score: Double = 0.0,
    var title: String? = null,
    val version: Long = 0
) : EditWorkoutViewState() {

    /**
     * Creates a copy of the current state with an incremented version.
     * This solves the problem of the UI not updating
     * when **newValue != value** is false between the last two view states.
     * @see <a href="https://github.com/rainbowcake/rainbowcake/blob/1.3.0-RELEASE/rainbow-cake-core/src/main/java/co/zsmb/rainbowcake/internal/livedata/Extensions.kt">
     *     RainbowCake Source Code - distinct() filter
     * </a>
     */
    fun versionedCopy(
        id: String? = this.id,
        exercises: List<EditWorkoutPresenter.Exercise> = this.exercises,
        score: Double = this.score,
        title: String? = this.title
    ): Editing = copy(
        id = id,
        exercises = exercises,
        score = score,
        title = title,
        version = this.version + 1
    )

    fun toSaving() = Saving(id, exercises.toList(), score, title)
}

data class Saving(
    val id: String? = null,
    val exercises: List<EditWorkoutPresenter.Exercise> = listOf(),
    val score: Double = 0.0,
    val title: String? = null
) : EditWorkoutViewState() {

    fun toEditing() = Editing(id, exercises.toMutableList(), score, title)
}
