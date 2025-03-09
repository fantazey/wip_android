package com.example.wipmobile.data.model

import java.time.Instant

data class ModelImage(
    val id: Int,
    val imagePath: String?,
    val isImageForProgress: Boolean,
    val createdAt: String
)