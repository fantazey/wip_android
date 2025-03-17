package com.example.wipmobile.data.source.remote

import android.graphics.Bitmap
import android.media.Image
import com.example.wipmobile.data.dto.AddModelFormData
import com.example.wipmobile.data.model.BattleScribeCategory
import com.example.wipmobile.data.model.BattleScribeUnit
import com.example.wipmobile.data.model.KillTeam
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.ModelImage
import com.example.wipmobile.data.model.ModelProgress
import com.example.wipmobile.data.model.ModelsPage
import com.example.wipmobile.data.model.UserStatus
import com.example.wipmobile.data.source.remote.api.WipApi
import com.example.wipmobile.data.source.remote.api.request.ModelProgressRequest
import com.example.wipmobile.data.source.remote.api.request.ModelRequest
import com.example.wipmobile.data.source.remote.api.response.BattleScribeUnitResponse
import com.example.wipmobile.data.source.remote.api.response.KillTeamResponse
import com.example.wipmobile.data.source.remote.api.response.ModelGroupResponse
import com.example.wipmobile.data.source.remote.api.response.UserStatusResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.toImmutableList
import okio.ByteString
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class ModelRemoteDataSource @Inject constructor(
    private val wipApi: WipApi
) {
    suspend fun getModels(
        name: String = "",
        page: Int = 1,
        statuses: List<UserStatus> = emptyList(),
        modelGroups: List<ModelGroup> = emptyList()
    ): ModelsPage {
        return wipApi.getUserModels(
            statuses = statuses.map { it.id }.toImmutableList(),
            modelGroups = modelGroups.map { it.id }.toImmutableList(),
            name = name,
            page = page
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

    suspend fun getBattleScribeCategoryList(): List<BattleScribeCategory> {
        return wipApi.getBattleScribeCategories().map { it.mapToModel() }.toImmutableList()
    }

    suspend fun getModelGroups(): List<ModelGroup> {
        return wipApi.getModelGroups().map { it.mapToModel() }.toImmutableList()
    }

    suspend fun createModel(form: AddModelFormData): Model {
        val bsUnit = if (null != form.battleScribeUnit ) {
          BattleScribeUnitResponse.fromModel(form.battleScribeUnit)
        } else {
            null
        }
        val killTeam = if (null != form.killTeam) {
            KillTeamResponse.fromModel(form.killTeam)
        } else {
            null
        }
        val requestBody = ModelRequest(
            name = form.name,
            unitCount = form.unitCount,
            terrain = form.terrain,
            status = UserStatusResponse.fromModel(form.status!!),
            groups = form.groups.map { ModelGroupResponse.fromModel(it) }.toImmutableList(),
            battleScribeUnit = bsUnit,
            killTeam = killTeam
        )
        return wipApi.createModel(requestBody).mapToModel()
    }

    suspend fun updateModel(modelId: Int, request: ModelRequest): Model {
        return wipApi.updateModel(modelId, request).mapToModel()
    }

    suspend fun createModelProgress(modelId: Int, request: ModelProgressRequest): ModelProgress {
        return wipApi.createModelProgress(modelId, request).mapToModel()
    }

    suspend fun updateModelProgress(modelId: Int, progressId: Int, request: ModelProgressRequest): ModelProgress {
        return wipApi.updateModelProgress(modelId, progressId, request).mapToModel()
    }

    suspend fun deleteModelProgress(modelId: Int, progressId: Int) {
        wipApi.deleteModelProgress(modelId, progressId)
    }

    suspend fun createModelImage(modelId: Int, images: List<Bitmap>): List<ModelImage> {
        val imageParts = Array<MultipartBody.Part>(size = images.size, init = { index: Int ->
            val image = images[index]
            val byteArrayOutputStream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val requestBody: RequestBody = byteArrayOutputStream.toByteArray().toRequestBody(contentType = "image/*".toMediaTypeOrNull())
            val name = "${modelId}_${Instant.now()}.jpeg"
            MultipartBody.Part.createFormData("images", name, requestBody)
        })
        return wipApi.createModelImage(modelId, imageParts).map { it.mapToModel() }.toImmutableList()
    }

    suspend fun deleteModelImage(modelId: Int, imageId: Int) {
        wipApi.deleteModelImage(modelId, imageId)
    }
}