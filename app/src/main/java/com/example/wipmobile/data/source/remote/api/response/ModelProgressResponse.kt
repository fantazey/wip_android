package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.ModelProgress
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
    val time: Double,
    @SerializedName("get_last_image_url")
    val imagePath: String?,
    @SerializedName("user_status_id")
    val statusId: Int,
    @SerializedName("user_status_name")
    val statusName: String?
) {
    fun mapToModel(): ModelProgress {
        return ModelProgress(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt,
            time = time,
            imagePath = "http://10.0.2.2:8000$imagePath",
            statusId = statusId,
            statusName = statusName
        )
    }
}