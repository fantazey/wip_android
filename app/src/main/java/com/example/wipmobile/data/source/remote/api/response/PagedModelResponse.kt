package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.ModelsPage
import okhttp3.internal.toImmutableList


data class PagedModelResponse(
    val count: Int,
    val page: Int,
    val pages: Int,
    val pageSize: Int,
    val results: List<ModelResponse>
) {
    fun mapToModel(): ModelsPage {
        return ModelsPage(
            count = count,
            pageSize = pageSize,
            page = page,
            pages = pages,
            models = results.map { it.mapToModel() }.toImmutableList()
        )
    }
}