package com.example.wipmobile.data.dto

import android.graphics.Bitmap
import com.example.wipmobile.data.model.UserStatus

data class ModelProgressFormData(
    val title: String = "",
    val description: String = "",
    val time: Float = 0f,
    val images: List<Bitmap> = emptyList(),
    val status: UserStatus? = null,
)
