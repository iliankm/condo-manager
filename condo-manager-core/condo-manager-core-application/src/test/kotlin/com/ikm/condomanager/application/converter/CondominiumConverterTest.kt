package com.ikm.condomanager.application.converter

import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.domain.CondominiumAddress
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.usecase.condominium.UpdateCondominiumData
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class CondominiumConverterTest {
    @Test
    fun `should merge UpdateCondominiumData to Condominium`() {
        // given
        val updateCondominiumData = UpdateCondominiumData(
            address = CondominiumAddress(
                city = "City",
                street = "Street",
                houseNumber = 1,
                location = null
            )
        )
        val condominium = Condominium(
            id = CondominiumId(UUID.randomUUID().toString(), 1),
            address = mockk()
        )
        // when
        updateCondominiumData.mergeToCondominium(condominium)
        // then
        assertEquals(updateCondominiumData.address, condominium.address)
    }
}
