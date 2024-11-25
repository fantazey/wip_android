package com.example.wipmobile.data.source.remote.api.response

data class LoginResponse(
    val token: String,
    val expiry: String,
    val user: UserResponse
)