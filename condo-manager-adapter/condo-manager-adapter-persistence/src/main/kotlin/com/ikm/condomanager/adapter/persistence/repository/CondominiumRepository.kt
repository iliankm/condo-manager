package com.ikm.condomanager.adapter.persistence.repository

import com.ikm.condomanager.adapter.persistence.entity.CondominiumEntity
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Spring data repository for [CondominiumEntity].
 */
@Repository
interface CondominiumRepository : BaseRepository<CondominiumEntity, UUID>
