package hu.bme.aut.fitary.domainModel

data class DomainExercise(
    val id: Long? = null,
    var name: String = "",
    var reps: Int = 0,
    var scorePerRep: Int = 1
) {

    val score: Int
        get() = reps * scorePerRep

    fun validate(): Boolean {
        return name !== "" && reps > 0
    }
}