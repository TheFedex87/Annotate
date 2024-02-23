package it.thefedex87.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BlockNoteEntity(
    @PrimaryKey
    val id: Long?,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
    val color: Int
)
