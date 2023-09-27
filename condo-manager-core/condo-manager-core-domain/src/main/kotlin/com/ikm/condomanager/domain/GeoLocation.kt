package com.ikm.condomanager.domain

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import java.math.BigDecimal

/**
 * Value object representing geolocation.
 *
 * @property lat latitude. Must be a value between -90 and 90.
 * @property lon longitude. Must be a value between -180 and 180.
 */
data class GeoLocation(
    @field:DecimalMin("-90")
    @field:DecimalMax("90")
    val lat: BigDecimal,
    @field:DecimalMin("-180")
    @field:DecimalMax("180")
    val lon: BigDecimal
) : SelfValidating<GeoLocation>() {
    init {
        validate()
    }
}
