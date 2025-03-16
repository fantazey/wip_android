package com.example.wipmobile.data.source.remote

import com.example.wipmobile.data.model.BattleScribeUnit
import com.example.wipmobile.data.model.KillTeam
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.ModelImage
import com.example.wipmobile.data.model.ModelProgress
import com.example.wipmobile.data.model.ModelsPage
import com.example.wipmobile.data.model.UserStatus
import com.example.wipmobile.data.source.remote.api.WipApi
import okhttp3.internal.toImmutableList
import javax.inject.Inject

class ModelRemoteDataSource @Inject constructor(
    private val wipApi: WipApi
) {
    suspend fun getModels(filter: LoadModelsFilter): ModelsPage {
        return wipApi.getUserModels(
            statuses = filter.statusesToQuery(),
            modelGroups = filter.modelGroupsToQuery(),
            name = filter.name,
            page = filter.page
        ).mapToModel()
    }

    suspend fun getModel(id: Int): Model {
        return wipApi.getModel(id).mapToModel()
    }

    suspend fun getModelImages(modelId: Int): List<ModelImage> {
        return wipApi.getModelImages(modelId).map { it.mapToModel() }.toImmutableList()
    }

    suspend fun getModelProgress(modelId: Int): List<ModelProgress> {
        return wipApi.getModelProgress(modelId).map { it.mapToModel() }.toImmutableList()
    }

    suspend fun getUserStatusList(): List<UserStatus> {
        return wipApi.getUserStatuses().map { it.mapToModel() }.toImmutableList()
    }

    suspend fun getKillTeamList(): List<KillTeam> {
        return wipApi.getKillTeams().map { it.mapToModel() }.toImmutableList()
    }

    suspend fun getBattleScribeUnitList(): List<BattleScribeUnit> {
        return wipApi.getBattleScribeUnits().map { it.mapToModel() }.toImmutableList()
    }

    suspend fun getModelGroups(): List<ModelGroup> {
        return wipApi.getModelGroups().map { it.mapToModel() }.toImmutableList()
    }

    data class LoadModelsFilter(
        val statuses: List<UserStatus> = emptyList(),
        val modelGroups: List<ModelGroup> = emptyList(),
        val name: String = "",
        val page: Int = 1
    ) {
        fun statusesToQuery(): List<Int> {
            return statuses.map { it.id }.toImmutableList()
        }

        fun modelGroupsToQuery(): List<Int> {
            return modelGroups.map { it.id }.toImmutableList()
        }
    }
}