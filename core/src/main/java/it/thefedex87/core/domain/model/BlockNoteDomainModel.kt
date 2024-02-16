package it.thefedex87.core.domain.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class BlockNoteDomainModel(
    val id: Long,
    val name: String,
    val color: Int,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
)
