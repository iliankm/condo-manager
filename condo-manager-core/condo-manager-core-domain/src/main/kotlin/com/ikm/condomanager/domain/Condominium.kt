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
    val id: CondominiumId?,
    var address: CondominiumAddress
) : SelfValidating {

    init {
        validate()
    }

    override fun toString(): String {
        return "Condominium(id=$id, address=$address)"
    }
}
