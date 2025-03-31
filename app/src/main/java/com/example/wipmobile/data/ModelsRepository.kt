package com.example.wipmobile.data

import android.graphics.Bitmap
import com.example.wipmobile.data.dto.ModelFormData
import com.example.wipmobile.data.dto.ModelGroupFormData
import com.example.wipmobile.data.dto.ModelProgressFormData
import com.example.wipmobile.data.dto.StatusFormData
import com.example.wipmobile.data.model.BattleScribeCategory
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
        return remoteDataSource.getModels(
            name = name,
            page = page,
            statuses = statuses,
            modelGroups = modelGroups
        )
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

    suspend fun loadBsCategories(): List<BattleScribeCategory> {
        return remoteDataSource.getBattleScribeCategoryList()
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

    suspend fun createModel(model: ModelFormData): Model {
        return remoteDataSource.createModel(model)
    }

    suspend fun updateModel(model: Model, formData: ModelFormData): Model {
        return remoteDataSource.updateModel(model.id, formData)
    }

    suspend fun createModelProgress(model: Model, formData: ModelProgressFormData): ModelProgress {
        return remoteDataSource.createModelProgress(model.id, formData)
    }

    suspend fun updateModelProgress(
        model: Model,
        progress: ModelProgress,
        formData: ModelProgressFormData
    ): ModelProgress {
        return remoteDataSource.updateModelProgress(model.id, progress.id, formData)
    }

    suspend fun deleteModelProgress(model: Model, progress: ModelProgress) {
        remoteDataSource.deleteModelProgress(model.id, progress.id)
    }

    suspend fun createModelImage(model: Model, progress: ModelProgress?, images: List<Bitmap>): List<ModelImage> {
        return remoteDataSource.createModelImage(model.id, progress?.id, images)
    }

    suspend fun deleteModelImage(model: Model, images: List<ModelImage>) {
        images.forEach { image -> remoteDataSource.deleteModelImage(model.id, image.id) }
    }

    suspend fun createModelGroup(form: ModelGroupFormData): ModelGroup {
        return remoteDataSource.createModelGroup(form)
    }

    suspend fun updateModelGroup(group: ModelGroup, form: ModelGroupFormData): ModelGroup {
        return remoteDataSource.updateModelGroup(group, form)
    }

    suspend fun deleteModelGroup(group: ModelGroup) {
        remoteDataSource.deleteModelGroup(group)
    }

    suspend fun createStatus(form: StatusFormData): UserStatus {
        return remoteDataSource.createStatus(form)
    }

    suspend fun updateStatus(status: UserStatus, form: StatusFormData): UserStatus {
        return remoteDataSource.updateStatus(status, form)
    }

    suspend fun deleteStatus(status: UserStatus) {
        remoteDataSource.deleteStatus(status)
    }
}