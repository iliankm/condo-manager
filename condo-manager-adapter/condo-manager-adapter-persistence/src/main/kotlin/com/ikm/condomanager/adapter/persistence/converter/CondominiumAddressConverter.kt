package com.ikm.condomanager.adapter.persistence.converter

import com.ikm.condomanager.adapter.persistence.entity.CondominiumAddressEntity
import com.ikm.condomanager.domain.CondominiumAddress
import com.ikm.condomanager.domain.GeoLocation

/**
 * Converts [CondominiumAddress] to [CondominiumAddressEntity].
 *
 * @return converted instance of [CondominiumAddressEntity]
 */
fun CondominiumAddress.convertToCondominiumAddressEntity() =
    CondominiumAddressEntity(
        city = city,
        street = street,
        houseNumber = houseNumber,
        lat = location?.lat,
        lon = location?.lon
    )

/**
 * Merges all possible data from this [CondominiumAddress] to the passed [CondominiumAddressEntity].
 *
 * @param condominiumAddressEntity the [CondominiumAddressEntity] to be merged into.
 * @return the merged instance passed in [condominiumAddressEntity].
 */
fun CondominiumAddress.mergeToCondominiumAddressEntity(condominiumAddressEntity: CondominiumAddressEntity) =
    condominiumAddressEntity.let {
        it.city = city
        it.street = street
        it.houseNumber = houseNumber
        it.lat = location?.lat
        it.lon = location?.lon
        it
    }

/**
 * Converts [CondominiumAddressEntity] to [CondominiumAddress].
 *
 * @return converted instance of [CondominiumAddress]
 */
fun CondominiumAddressEntity.convertToCondominiumAddress() =
    CondominiumAddress(
        city = city,
        street = street,
        houseNumber = houseNumber,
        location = if (lat != null && lon != null) {
            GeoLocation(
                lat = lat!!,
                lon = lon!!
            )
        } else {
            null
        }
    )
