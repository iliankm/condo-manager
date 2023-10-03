package com.ikm.condomanager.adapter.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.math.BigDecimal

/**
 * JPA embeddable entity representing a condominium address.
 */
@Embeddable
class CondominiumAddressEntity(
    @Column(name = "city", nullable = false, length = 50)
    var city: String,
    @Column(name = "street", nullable = false, length = 200)
    var street: String,
    @Column(name = "house_number", nullable = false)
    var houseNumber: Short,
    @Column(name = "lat")
    var lat: BigDecimal? = null,
    @Column(name = "lon")
    var lon: BigDecimal? = null
)
