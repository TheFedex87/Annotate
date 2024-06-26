package it.thefedex87.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = BlockNoteEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("blockNoteId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class NoteEntity(
    @PrimaryKey
    val id: Long?,
    val title: String,
    val body: String,
    val createdAt: Long,
    val updatedAt: Long,
    val alarmTime: Long?,

    val blockNoteId: Long
)
