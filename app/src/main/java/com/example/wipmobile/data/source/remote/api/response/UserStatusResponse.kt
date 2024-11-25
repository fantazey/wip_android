package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.UserStatus

class UserStatusResponse(
    private val id: Int,
    private val name: String,
    private val order: Int
) {
    fun toUserStatus(): UserStatus {
        return UserStatus(id, name, order)
    }
}