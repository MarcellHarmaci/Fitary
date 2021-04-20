package hu.bme.aut.fitary.domainModel

data class DomainExercise(
    val id: Long? = null,
    var name: String = "",
    var reps: Int = 0,
    var scorePerRep: Double = 1.0
) {

    val score: Double
        get() = reps * scorePerRep

    fun validate(): Boolean {
        return name !== "" && reps > 0
    }
}