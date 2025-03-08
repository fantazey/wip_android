package com.example.wipmobile.data.model

data class ModelsPage(
    val models: List<Model>,
    val count: Int,
    val page: Int,
    val pages: Int,
    val pageSize: Int
)