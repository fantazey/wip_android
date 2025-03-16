package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.UserStatus

class UserStatusResponse(
    private val id: Int,
    private val name: String,
    private val previous: Int?,
    private val next: Int?,
    private val order: Int?
) {
    fun mapToModel(): UserStatus {
        return UserStatus(id, name, order, previous, next)
    }
}