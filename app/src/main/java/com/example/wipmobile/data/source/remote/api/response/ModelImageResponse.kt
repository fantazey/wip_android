package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.ModelImage
import com.example.wipmobile.data.source.remote.api.RemoteHost
import com.google.gson.annotations.SerializedName
import java.time.Instant

/**
 *   "id": 457,
 *     "image": "/media/wip/andy/SW%20Termiantors/fantazey_AgACAgIAAxkBAAIT_WbzEQf1fkSJ1fF3el45gt6_we7LAAJn5jEb4vSZSwZ_bBZqcPZ",
 *     "is_image_for_progress": true,
 *     "created": "2024-09-24T22:20:40.057838+03:00"
 */
data class ModelImageResponse(
    val id: Int,
    @SerializedName("image")
    val imagePath: String?,
    @SerializedName("is_image_for_progress")
    val isImageForProgress: Boolean,
    @SerializedName("created")
    val createdAt: String
) {
    fun mapToModel(): ModelImage {
        return ModelImage(
            id = id,
            imagePath = "${RemoteHost.HOST}$imagePath",
            isImageForProgress = isImageForProgress,
            createdAt = createdAt
        )
    }
}