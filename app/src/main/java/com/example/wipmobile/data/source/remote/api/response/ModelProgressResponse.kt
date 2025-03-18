package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.ModelProgress
import com.example.wipmobile.data.source.remote.api.RemoteHost
import com.google.gson.annotations.SerializedName
import java.time.Instant

/**
 * "id": 1672,
 *     "title": "Готово",
 *     "description": "",
 *     "datetime": "2024-11-18T20:12:45.820692+03:00",
 *     "time": 0.0,
 *     "get_last_image_url": null,
 *     "user_status_id": 14,
 *     "user_status_name": "Закончено"
 */
data class ModelProgressResponse(
    val id: Int,
    val title: String,
    val description: String,
    @SerializedName("datetime")
    val createdAt: String,
    val time: Float,
    @SerializedName("get_last_image_url")
    val imagePath: String?,
    @SerializedName("user_status")
    val status: UserStatusResponse,
) {
    fun mapToModel(): ModelProgress {
        val img = if (null == imagePath) {
            null
        } else {
            "${RemoteHost.HOST}$imagePath"
        }
        return ModelProgress(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt,
            time = time,
            imagePath = img,
            status = status.mapToModel(),
        )
    }
}