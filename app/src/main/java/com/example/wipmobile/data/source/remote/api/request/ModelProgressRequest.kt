package com.example.wipmobile.data.source.remote.api.request

import com.example.wipmobile.data.source.remote.api.response.UserStatusResponse
import com.google.gson.annotations.SerializedName
import java.time.OffsetDateTime

/**
 * {
 *     "title": "Готово",
 *     "description": "",
 *     "datetime": "2024-11-18T20:12:45.820692+03:00",
 *     "time": 1.0,
 *     "user_status": {
 *       "id": 14,
 *       "name": "Закончено",
 *       "order": 14,
 *       "previous": 13,
 *       "next": null
 *     }
 * }
 */
data class ModelProgressRequest(
    val title: String,
    val description: String?,
    @SerializedName("datetime")
    val dateTime: String,
    val time: Float,
    @SerializedName("user_status")
    val status: UserStatusResponse
)
