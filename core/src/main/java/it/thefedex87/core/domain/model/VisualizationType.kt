package it.thefedex87.core.domain.model

sealed interface VisualizationType {
    data object Grid : VisualizationType

    data object List : VisualizationType

    companion object {
        fun fromString(name: String) : VisualizationType {
            return when(name) {
                "Grid" -> Grid
                "List" -> List
                else -> Grid
            }
        }

        fun toSimpleString(type: VisualizationType): String {
            return when(type) {
                Grid -> "Grid"
                List -> "List"
            }
        }
    }
}