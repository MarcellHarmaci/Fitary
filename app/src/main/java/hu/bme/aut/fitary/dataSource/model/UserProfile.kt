package hu.bme.aut.fitary.dataSource.model

data class UserProfile(
    val key: String? = null,
    val id: String? = null,
    val mail: String = "",
    val username: String = "",
    val avatar: String? = null
)