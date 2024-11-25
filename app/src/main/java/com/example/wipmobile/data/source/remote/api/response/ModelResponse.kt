package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.Model
import com.google.gson.annotations.SerializedName

class ModelResponse(
    val id: Int,
    val name: String,
    @SerializedName("user_status")
    val statusId: Int,
    @SerializedName("get_last_image_url")
    val imagePath: String?
) {
    fun mapToModel(): Model {
        return Model(
            id=id,
            name=name,
            statusId = statusId,
            statusName = null,
            lastImagePath = "http://10.0.2.2:8000$imagePath"
        )
    }
}