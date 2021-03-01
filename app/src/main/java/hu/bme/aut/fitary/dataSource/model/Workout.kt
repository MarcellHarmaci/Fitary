package hu.bme.aut.fitary.dataSource.model

data class Workout (
    val uid: String,
    val exercisesAndReps: MutableList<Pair<Long, Int>> = mutableListOf(),
    var score: Double = 0.0,
    var comment: String? = null
)