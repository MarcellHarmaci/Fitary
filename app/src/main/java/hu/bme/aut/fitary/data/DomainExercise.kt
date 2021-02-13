package hu.bme.aut.fitary.data

data class DomainExercise(
    var name: String = "",
    var reps: Int = 0
) {
    fun validate(): Boolean {
        return name !== "" && reps > 0
    }
}