package it.thefedex87.search.domain

import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.error_handling.DataError
import it.thefedex87.error_handling.Result

interface SearchRepository {
    suspend fun filterBlockNotes(query: String): Result<List<BlockNoteDomainModel>, DataError>
    suspend fun filterNotes(query: String): Result<List<NoteDomainModel>, DataError>
}