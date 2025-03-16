package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.ModelGroup

data class ModelGroupResponse(
    val id: Int,
    val name: String
) {
    fun mapToModel(): ModelGroup {
        return ModelGroup(
            id = id,
            name = name
        )
    }

    companion object {
        fun fromModel(group: ModelGroup): ModelGroupResponse {
            return ModelGroupResponse(
                id=group.id,
                name = group.name
            )
        }
    }
}