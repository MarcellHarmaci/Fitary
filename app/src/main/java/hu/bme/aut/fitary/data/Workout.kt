package hu.bme.aut.fitary.data

data class Workout (
    var uid: String? = null,
    var userName: String? = null,
    var domainExercises: MutableList<DomainExercise> = mutableListOf(),
    var comment: String? = null
)