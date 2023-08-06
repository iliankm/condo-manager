package com.ikm.condomanager.adapter.persistence.repository

import com.ikm.condomanager.adapter.persistence.entity.PersonEntity
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Spring data repository for [PersonEntity].
 */
@Repository
interface PersonRepository : BaseRepository<PersonEntity, UUID>
