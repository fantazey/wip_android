package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.ModelGroup

data class ModelGroupResponse(
    val id: Int,
    val name: String
) {
    fun toModelGroup(): ModelGroup {
        return ModelGroup(
            id = id,
            name = name
        )
    }
}