package it.thefedex87.core.domain.model

import java.time.LocalDateTime

data class NoteDomainModel(
    val id: Long?,
    val title: String,
    val body: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val blockNote: BlockNoteDomainModel
)