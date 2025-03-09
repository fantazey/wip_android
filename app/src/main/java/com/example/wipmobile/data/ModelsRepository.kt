package com.example.wipmobile.data

import com.example.wipmobile.data.model.BattleScribeUnit
import com.example.wipmobile.data.model.KillTeam
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.ModelImage
import com.example.wipmobile.data.model.ModelProgress
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

    suspend fun loadModelProgress(model: Model): List<ModelProgress> {
        return remoteDataSource.getModelProgress(model.id)
    }

    suspend fun loadModelImages(model: Model): List<ModelImage> {
        return remoteDataSource.getModelImages(model.id)
    }

    suspend fun loadModel(id: Int): Model {
        return remoteDataSource.getModel(id)
    }

}