package com.example.wipmobile.data.source.remote.api.request

import com.google.gson.annotations.SerializedName

data class UserStatusRequest(
    private val name: String,
    private val previous: Int?,
    private val next: Int?,
    private val order: Int,
    @SerializedName("is_initial")
    private val isInitial: Boolean,
    @SerializedName("is_final")
    private val isFinal: Boolean
)
