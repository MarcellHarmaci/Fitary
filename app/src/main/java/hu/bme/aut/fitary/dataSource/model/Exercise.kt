package hu.bme.aut.fitary.dataSource.model

data class Exercise(
    val id: Long,
    val name: String = "",
    val score: Double = 1.0
)