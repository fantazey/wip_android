package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.BattleScribeUnit
import com.google.gson.annotations.SerializedName

class BattleScribeUnitResponse(
    private val id: Int,
    private val name: String,
    @SerializedName("bs_category")
    private val category: BattleScribeCategoryResponse?
) {
    fun mapToModel(): BattleScribeUnit {
        return BattleScribeUnit(id, name, category = category?.mapToModel())
    }

    companion object {
        fun fromModel(bsUnit: BattleScribeUnit): BattleScribeUnitResponse {
            val category = if (null != bsUnit.category) {
                BattleScribeCategoryResponse.fromModel(bsUnit.category)
            } else {
                null
            }
            return BattleScribeUnitResponse(
                id=bsUnit.id,
                name = bsUnit.name,
                category = category
            )
        }
    }
}