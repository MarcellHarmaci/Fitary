package hu.bme.aut.fitary.data

data class DomainWorkout (
    val uid: String,
    var username: String,
    var domainExercises: MutableList<DomainExercise> = mutableListOf(),
    var score: Double = 0.0,
    var comment: String? = null
)