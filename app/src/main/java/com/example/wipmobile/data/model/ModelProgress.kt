package com.example.wipmobile.data.model


data class ModelProgress(
    val id: Int,
    val title: String,
    val description: String?,
    val createdAt: String,
    val time: Float,
    val imagePath: String?,
    val status: UserStatus,
)