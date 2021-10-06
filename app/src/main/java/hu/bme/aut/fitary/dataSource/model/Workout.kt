package hu.bme.aut.fitary.dataSource.model

// TODO add a DateTime property
data class Workout (
    var id: String = "",
    val uid: String? = null,
    val exercises: MutableList<Long> = mutableListOf(),
    val reps: MutableList<Int> = mutableListOf(),
    var comment: String? = null
)