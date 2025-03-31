package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.UserStatus
import com.google.gson.annotations.SerializedName

/**
 *   "id": 62,
 *   "name": "updated status1",
 *   "order": 0,
 *   "previous": null,
 *   "next": 63,
 *   "is_initial": false,
 *   "is_final": false,
 *   "transition_title": "to updated status1"
 */
class UserStatusResponse(
    private val id: Int,
    private val name: String,
    private val previous: Int?,
    private val next: Int?,
    private val order: Int,
    @SerializedName("is_initial")
    private val isInitial: Boolean,
    @SerializedName("is_final")
    private val isFinal: Boolean
) {
    fun mapToModel(): UserStatus {
        return UserStatus(id, name, order, previous, next)
    }
}