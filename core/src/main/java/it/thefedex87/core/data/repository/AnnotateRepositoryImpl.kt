package it.thefedex87.core.data.repository

import android.graphics.Color
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.repository.AnnotateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class AnnotateRepositoryImpl: AnnotateRepository {
    override val blockNotes: Flow<List<BlockNoteDomainModel>>
        get() = flow {
            emit(
                listOf(
                    BlockNoteDomainModel(
                        id = 1,
                        name = "Default",
                        color = Color.argb(255, 0, 128, 0),
                        createdAt = LocalDate.of(2024, 1, 12),
                        updatedAt = LocalDate.of(2024, 1, 12),
                    ),
                    BlockNoteDomainModel(
                        id = 2,
                        name = "Blocco note 1",
                        color = Color.argb(255, 23, 128, 99),
                        createdAt = LocalDate.of(2024, 1, 12),
                        updatedAt = LocalDate.of(2024, 1, 12),
                    ),
                    BlockNoteDomainModel(
                        id = 3,
                        name = "Cucina",
                        color = Color.argb(255, 128, 128, 0),
                        createdAt = LocalDate.of(2024, 1, 12),
                        updatedAt = LocalDate.of(2024, 1, 12),
                    ),
                    BlockNoteDomainModel(
                        id = 4,
                        name = "Lavoro",
                        color = Color.argb(255, 145, 45, 100),
                        createdAt = LocalDate.of(2024, 1, 12),
                        updatedAt = LocalDate.of(2024, 1, 12),
                    )
                )
            )
        }
}