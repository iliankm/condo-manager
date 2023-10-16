package com.ikm.condomanager.adapter.web.converter

import com.ikm.condomanager.adapter.web.dto.CondominiumAddressDTO
import com.ikm.condomanager.domain.CondominiumAddress

/**
 * Converts [CondominiumAddressDTO] to [CondominiumAddress].
 *
 * @return converted instance of [CondominiumAddress]
 */
fun CondominiumAddressDTO.convertToCondominiumAddress() =
    CondominiumAddress(
        city = city,
        street = street,
        houseNumber = houseNumber,
        location = location?.convertToGeoLocation()
    )

/**
 * Converts [CondominiumAddress] to [CondominiumAddressDTO].
 *
 * @return converted instance of [CondominiumAddressDTO]
 */
fun CondominiumAddress.convertToCondominiumAddressDTO() =
    CondominiumAddressDTO(
        city = city,
        street = street,
        houseNumber = houseNumber,
        location = location?.convertToGeoLocationDTO()
    )
