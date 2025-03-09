package com.example.wipmobile.data.model

import java.time.Instant

data class ModelProgress(
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: String,
    val time: Double,
    val imagePath: String?,
    val statusId: Int,
    val statusName: String?,
)