package com.example.wipmobile.data

import com.example.wipmobile.data.model.BattleScribeUnit
import com.example.wipmobile.data.model.KillTeam
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.ModelsPage
import com.example.wipmobile.data.model.UserStatus

import com.example.wipmobile.data.source.remote.ModelRemoteDataSource
import javax.inject.Inject

class ModelsRepository @Inject constructor(
    private val remoteDataSource: ModelRemoteDataSource
) {

    suspend fun loadModels(
        name: String = "",
        page: Int = 1,
        statuses: List<UserStatus> = emptyList(),
        modelGroups: List<ModelGroup> = emptyList()
    ): ModelsPage {
        val filter = ModelRemoteDataSource.LoadModelsFilter(
            statuses = statuses,
            modelGroups = modelGroups,
            name = name,
            page = page
        )
        return remoteDataSource.getModels(filter)
    }

    suspend fun loadModelGroups(): List<ModelGroup> {
        return remoteDataSource.getModelGroups()
    }

    suspend fun loadUserStatuses(): List<UserStatus> {
        return remoteDataSource.getUserStatusList()
    }

    suspend fun loadKillTeams(): List<KillTeam> {
        return remoteDataSource.getKillTeamList()
    }

    suspend fun loadBsUnits(): List<BattleScribeUnit> {
        return remoteDataSource.getBattleScribeUnitList()
    }


}