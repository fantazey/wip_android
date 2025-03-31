package com.example.wipmobile.data.model

data class UserStatus(
    val id: Int,
    val name: String,
    val order: Int,
    val previous: Int? = null,
    val next: Int? = null,
    val isInitial: Boolean = false,
    val isFinal: Boolean = false
)