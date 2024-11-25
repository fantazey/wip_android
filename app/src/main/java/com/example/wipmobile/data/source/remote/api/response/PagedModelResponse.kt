package com.example.wipmobile.data.source.remote.api.response


data class PagedModelResponse(
    val count: Int,
    val results: Array<ModelResponse>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PagedModelResponse

        if (count != other.count) return false
        if (!results.contentEquals(other.results)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = count
        result = 31 * result + results.contentHashCode()
        return result
    }
}