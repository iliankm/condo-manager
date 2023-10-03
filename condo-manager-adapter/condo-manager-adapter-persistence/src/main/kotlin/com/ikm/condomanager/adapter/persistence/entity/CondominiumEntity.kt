package com.ikm.condomanager.adapter.persistence.entity

import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table

/**
 * JPA entity representing a condominium.
 */
@Entity(name = "Condominium")
@Table(name = "condominium")
class CondominiumEntity(
    @Embedded
    val address: CondominiumAddressEntity
) : BaseEntity()
