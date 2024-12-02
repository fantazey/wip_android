package com.example.wipmobile.ui.add_model

import java.time.Instant
import java.util.Date

data class AddModelUiState(
    val name: String = "",
    val unitCount: Int = 1,
    val terrain: Boolean = false,
    val userStatusId: Int? = null,
    val userStatusName: String = "",
    val buyDate: Date = Date.from(Instant.now()),
    val groupIds: Array<Int> = emptyArray(),
    val groupNames: Array<String> = emptyArray(),
    val killTeamId: Int? = null,
    val killTeamName: String = "",
    val battleScribeId: Int? = null,
    val battleScribeName: String = "",

    val isLoading: Boolean = false,
    val error: String? = null
    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AddModelUiState

        if (name != other.name) return false
        if (unitCount != other.unitCount) return false
        if (terrain != other.terrain) return false
        if (userStatusId != other.userStatusId) return false
        if (userStatusName != other.userStatusName) return false
        if (buyDate != other.buyDate) return false
        if (!groupIds.contentEquals(other.groupIds)) return false
        if (!groupNames.contentEquals(other.groupNames)) return false
        if (killTeamId != other.killTeamId) return false
        if (killTeamName != other.killTeamName) return false
        if (battleScribeId != other.battleScribeId) return false
        if (battleScribeName != other.battleScribeName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + unitCount
        result = 31 * result + terrain.hashCode()
        result = 31 * result + (userStatusId?:0)
        result = 31 * result + userStatusName.hashCode()
        result = 31 * result + buyDate.hashCode()
        result = 31 * result + groupIds.contentHashCode()
        result = 31 * result + groupNames.contentHashCode()
        result = 31 * result + (killTeamId?:0)
        result = 31 * result + killTeamName.hashCode()
        result = 31 * result + (battleScribeId?:0)
        result = 31 * result + battleScribeName.hashCode()
        return result
    }
}