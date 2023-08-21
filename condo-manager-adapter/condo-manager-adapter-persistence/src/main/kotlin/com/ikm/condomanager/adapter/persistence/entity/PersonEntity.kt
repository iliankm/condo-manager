package com.ikm.condomanager.adapter.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

/**
 * JPA entity representing a person.
 */
@Entity(name = "Person")
@Table(name = "person")
class PersonEntity(
    @Column(name = "name", nullable = false, length = 70)
    var name: String,
    @Column(name = "email", length = 254)
    var email: String? = null,
    @Column(name = "phone_number", length = 50)
    var phoneNumber: String? = null
) : BaseEntity()
