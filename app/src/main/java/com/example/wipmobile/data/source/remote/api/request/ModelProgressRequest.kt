package com.example.wipmobile.data.source.remote.api.request

import com.google.gson.annotations.SerializedName

data class ModelProgressRequest(
    val title: String,
    val description: String?,
    @SerializedName("datetime")
    val dateTime: String,
    val time: Float,
    @SerializedName("user_status_id")
    val status: Int
)
