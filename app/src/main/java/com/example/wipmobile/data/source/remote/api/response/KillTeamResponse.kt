package com.example.wipmobile.data.source.remote.api.response

import com.example.wipmobile.data.model.KillTeam

class KillTeamResponse(
    private val id: Int,
    private val name: String,
) {
    fun toKillTeam(): KillTeam {
        return KillTeam(id, name)
    }
}