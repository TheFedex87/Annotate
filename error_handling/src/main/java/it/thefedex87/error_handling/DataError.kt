package it.thefedex87.error_handling

sealed interface DataError: Error {
    enum class Local: DataError {
        NOT_FOUND,
        SAVE_INTO_DB_ERROR,
        REMOVE_FROM_DB_ERROR,
        UNEXPECTED
    }
}