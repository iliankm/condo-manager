package com.ikm.condomanager.domain

/**
 * id of a [Condominium].
 */
typealias CondominiumId = DomainId

/**
 * Domain object representing a condominium.
 *
 * @property address condominium address
 * @property id the id of the condominium. If null, it is still not persisted and is created only in the memory.
 */
class Condominium(
    var address: CondominiumAddress,
    val id: CondominiumId? = null
) : SelfValidating<Condominium>() {

    companion object {
        /**
         * Factory function for initial creating of [Condominium].
         */
        fun create(address: CondominiumAddress) =
            Condominium(
                address = address
            )
    }

    init {
        validate()
    }

    override fun toString(): String {
        return "Condominium(address=$address, id=$id)"
    }
}
