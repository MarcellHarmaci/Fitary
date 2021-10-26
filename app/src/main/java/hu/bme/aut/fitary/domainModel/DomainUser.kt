package hu.bme.aut.fitary.domainModel

data class DomainUser(
    val id: String?,
    val mail: String,
    val username: String,
    val avatar: ByteArray? = null
) {
    override fun toString(): String {
        val isAvatarNull = if (avatar == null) {
            "null"
        } else {
            "notNull"
        }
        return "DomainUser(id=$id, mail='$mail', username='$username', avatar=$isAvatarNull)"
    }
}