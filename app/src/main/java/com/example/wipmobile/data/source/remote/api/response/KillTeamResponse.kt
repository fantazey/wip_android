package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.KillTeam

class KillTeamResponse(
    private val id: Int,
    private val name: String,
) {
    fun mapToModel(): KillTeam {
        return KillTeam(id, name)
    }

    companion object {
        fun fromModel(killTeam: KillTeam): KillTeamResponse {
            return KillTeamResponse(id=killTeam.id, name = killTeam.name)
        }
    }
}