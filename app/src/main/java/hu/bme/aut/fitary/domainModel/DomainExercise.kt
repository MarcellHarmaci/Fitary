package hu.bme.aut.fitary.domainModel

data class DomainExercise(
    val id: Long? = null,
    var name: String = "",
    var reps: Int = 0
) {
    fun validate(): Boolean {
        return name !== "" && reps > 0
    }
}