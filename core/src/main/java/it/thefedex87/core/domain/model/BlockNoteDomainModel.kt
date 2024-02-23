package it.thefedex87.core.domain.model

import java.time.LocalDateTime

data class BlockNoteDomainModel(
    val id: Long? = null,
    val name: String,
    val color: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
