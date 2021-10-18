package hu.bme.aut.fitary.domainModel

data class DomainUser(
    val id: String?,
    val mail: String,
    val username: String,
    val avatar: ByteArray? = null
)