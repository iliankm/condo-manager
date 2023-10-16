package com.ikm.condomanager.adapter.web.converter

import com.ikm.condomanager.adapter.web.dto.GeoLocationDTO
import com.ikm.condomanager.domain.GeoLocation

/**
 * Converts [GeoLocationDTO] to [GeoLocation].
 *
 * @return converted instance of [GeoLocation]
 */
fun GeoLocationDTO.convertToGeoLocation() =
    GeoLocation(
        lat = lat,
        lon = lon
    )

/**
 * Converts [GeoLocation] to [GeoLocationDTO].
 *
 * @return converted instance of [GeoLocationDTO]
 */
fun GeoLocation.convertToGeoLocationDTO() =
    GeoLocationDTO(
        lat = lat,
        lon = lon
    )
