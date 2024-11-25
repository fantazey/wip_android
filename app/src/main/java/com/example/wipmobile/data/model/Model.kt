package com.example.wipmobile.data.model

data class Model(
    val id: Int,
    val name: String,
    val statusId: Int,
    var statusName: String?,
    val lastImagePath: String?
)
