package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.BattleScribeUnit

class BattleScribeUnitResponse(
    private val id: Int,
    private val name: String,
) {
    fun toBattleScribeUnit(): BattleScribeUnit {
        return BattleScribeUnit(id, name)
    }
}