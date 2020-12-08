package hu.bme.aut.fitary.data

data class Workout (
    var uid: String? = null,
    var userName: String? = null,
    var exercise: String? = null,
    var repCount: Int? = null,
    var comment: String? = null
)