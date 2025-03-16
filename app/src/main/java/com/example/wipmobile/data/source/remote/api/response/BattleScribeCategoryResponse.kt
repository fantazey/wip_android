package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.BattleScribeCategory

class BattleScribeCategoryResponse(
    private val id: Int,
    private val name: String,
) {
    fun mapToModel(): BattleScribeCategory {
        return BattleScribeCategory(id, name)
    }

    companion object {
        fun fromModel(category: BattleScribeCategory): BattleScribeCategoryResponse {
            return BattleScribeCategoryResponse(
                id = category.id,
                name = category.name
            )
        }
    }
}