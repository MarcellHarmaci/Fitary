package hu.bme.aut.fitary.dataSource.model

data class Workout (
    val uid: String? = null,
    val exercises: MutableList<Long> = mutableListOf(),
    val reps: MutableList<Int> = mutableListOf(),
    var score: Double = 0.0,
    var comment: String? = null
)