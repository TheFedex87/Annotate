package it.thefedex87.core.domain.model

import java.time.LocalDate

data class BlockNoteDomainModel(
    val id: Long,
    val name: String,
    val color: Int,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,

)
