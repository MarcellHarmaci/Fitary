package hu.bme.aut.fitary.data

data class Workout (
    var uid: String? = null,
    var userName: String? = null,
    var exercises: MutableList<Exercise> = mutableListOf(),
    var comment: String? = null
)