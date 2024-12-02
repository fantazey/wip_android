package com.example.wipmobile.data

import com.example.wipmobile.data.model.BattleScribeUnit
import com.example.wipmobile.data.model.KillTeam
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.UserStatus

import com.example.wipmobile.data.source.remote.ModelRemoteDataSource
import javax.inject.Inject

class ModelsRepository @Inject constructor(
    private val remoteDataSource: ModelRemoteDataSource
) {

    suspend fun loadModels(): Array<Model> {
        return remoteDataSource.getModels()
    }

    suspend fun loadUserStatuses(): Array<UserStatus> {
        return remoteDataSource.getUserStatusList()
    }

    suspend fun loadKillTeams(): Array<KillTeam> {
        return remoteDataSource.getKillTeamList()
    }

    suspend fun loadBsUnits(): Array<BattleScribeUnit> {
        return remoteDataSource.getBattleScribeUnitList()
    }
}