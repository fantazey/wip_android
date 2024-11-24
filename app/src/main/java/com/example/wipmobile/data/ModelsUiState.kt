package com.example.wipmobile.data

import com.example.wipmobile.data.model.Model

data class ModelsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val models: Array<Model> = emptyArray(),
    val modelsLoaded: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ModelsUiState

        if (isLoading != other.isLoading) return false
        if (error != other.error) return false
        if (!models.contentEquals(other.models)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isLoading.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        result = 31 * result + models.contentHashCode()
        return result
    }
}
