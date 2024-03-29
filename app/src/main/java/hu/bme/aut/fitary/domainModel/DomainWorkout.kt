package hu.bme.aut.fitary.domainModel

data class DomainWorkout (
    val id: String? = null,
    val uid: String,
    var username: String,
    var domainExercises: MutableList<DomainExercise> = mutableListOf(),
    var score: Double = 0.0,
    var title: String? = null
)