package it.thefedex87.core.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate


@Immutable
data class BlockNoteDomainModel(
    val id: Long,
    val name: String,
    val color: Int,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
)
