package com.example.wipmobile.data.dto

import com.example.wipmobile.data.model.UserStatus

data class StatusFormData(
    val id: Int? = null,
    val name: String = "",
    val order: Int = 0,
    val previous: UserStatus? = null,
    val next: UserStatus? = null,
    val isInitial: Boolean = false,
    val isFinal: Boolean = false
)
