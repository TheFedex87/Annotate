package it.thefedex87.core.domain.model

sealed interface OrderBy {
    companion object {
        fun fromString(name: String): OrderBy {
            return when (name) {
                "Title" -> Title
                "CreatedRecent" -> CreatedAt(DateOrderType.RECENT)
                "CreatedOlder" -> CreatedAt(DateOrderType.OLDER)
                "UpdatedRecent" -> UpdatedAt(DateOrderType.RECENT)
                "UpdatedOlder" -> UpdatedAt(DateOrderType.OLDER)
                else -> CreatedAt(DateOrderType.RECENT)
            }
        }

        fun toSimpleString(orderBy: OrderBy): String {
            return when (orderBy) {
                Title -> "Title"
                CreatedAt(DateOrderType.RECENT) -> "CreatedRecent"
                CreatedAt(DateOrderType.OLDER) -> "CreatedOlder"
                UpdatedAt(DateOrderType.RECENT) -> "UpdatedRecent"
                UpdatedAt(DateOrderType.OLDER) -> "UpdatedOlder"
                else -> "CreatedRecent"
            }
        }
    }

    data object Title : OrderBy
    data class CreatedAt(val dateOrderType: DateOrderType) : OrderBy
    data class UpdatedAt(val dateOrderType: DateOrderType) : OrderBy
}

enum class DateOrderType {
    RECENT,
    OLDER
}