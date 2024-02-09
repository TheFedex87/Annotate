package it.thefedex87.core.domain.repository

import it.thefedex87.core.domain.model.BlockNoteDomainModel
import kotlinx.coroutines.flow.Flow

interface AnnotateRepository {
    val blockNotes: Flow<List<BlockNoteDomainModel>>
}