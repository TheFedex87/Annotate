package it.thefedex87.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BlockNoteEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val createdOn: String,
    val color: String
)
