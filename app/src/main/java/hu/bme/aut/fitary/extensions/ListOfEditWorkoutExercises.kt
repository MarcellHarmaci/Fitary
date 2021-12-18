package hu.bme.aut.fitary.extensions

import hu.bme.aut.fitary.ui.editWorkout.EditWorkoutPresenter

fun List<EditWorkoutPresenter.Exercise>.sumOfScores(): Double {
    return this.sumOf { it.score.toDouble() }
}