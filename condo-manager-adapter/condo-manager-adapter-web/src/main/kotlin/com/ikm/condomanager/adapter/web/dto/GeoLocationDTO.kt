package com.ikm.condomanager.adapter.web.dto

import java.math.BigDecimal

/**
 * DTO for geolocation resource.
 */
data class GeoLocationDTO(
    val lat: BigDecimal,
    val lon: BigDecimal
)
