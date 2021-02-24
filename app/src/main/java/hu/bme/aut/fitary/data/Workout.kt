package hu.bme.aut.fitary.data

data class Workout (
    val uid: String? = null,
    var userName: String? = null,
    var domainExercises: MutableList<DomainExercise> = mutableListOf(),
    var score: Double? = 0.0,
    var comment: String? = null
)