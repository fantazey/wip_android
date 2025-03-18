package com.example.wipmobile.data.source.remote

import android.graphics.Bitmap
import com.example.wipmobile.data.dto.ModelFormData
import com.example.wipmobile.data.dto.ModelProgressFormData
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
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.ZoneOffset
import javax.inject.Inject


class ModelRemoteDataSource @Inject constructor(
    private val wipApi: WipApi
) {

    private var userStatus: List<UserStatus> = emptyList()
    private var modelGroups: List<ModelGroup> = emptyList()
    private var killTeams: List<KillTeam> = emptyList()
    private var battleScribeCategories: List<BattleScribeCategory> = emptyList()
    private var battleScribeUnits: List<BattleScribeUnit> = emptyList()

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
        if (userStatus.isEmpty()) {
            userStatus = wipApi.getUserStatuses().map { it.mapToModel() }.toImmutableList()
        }
        return userStatus
    }

    suspend fun getKillTeamList(): List<KillTeam> {
        if (killTeams.isEmpty()) {
            killTeams = wipApi.getKillTeams().map { it.mapToModel() }.toImmutableList()
        }
        return killTeams
    }

    suspend fun getBattleScribeUnitList(): List<BattleScribeUnit> {
        if (battleScribeUnits.isEmpty()) {
            battleScribeUnits = wipApi.getBattleScribeUnits().map { it.mapToModel() }.toImmutableList()
        }
        return battleScribeUnits
    }

    suspend fun getBattleScribeCategoryList(): List<BattleScribeCategory> {
        if (battleScribeCategories.isEmpty()) {
            battleScribeCategories = wipApi.getBattleScribeCategories().map { it.mapToModel() }.toImmutableList()
        }
        return battleScribeCategories
    }

    suspend fun getModelGroups(): List<ModelGroup> {
        if (modelGroups.isEmpty()) {
            modelGroups
        }
        return wipApi.getModelGroups().map { it.mapToModel() }.toImmutableList()
    }

    suspend fun createModel(form: ModelFormData): Model {
        return wipApi.createModel(prepareModelRequest(form)).mapToModel()
    }

    suspend fun updateModel(modelId: Int, formData: ModelFormData): Model {
        return wipApi.updateModel(modelId, prepareModelRequest(formData)).mapToModel()
    }

    private fun prepareModelRequest(form: ModelFormData): ModelRequest {
        val bsUnit = if (null != form.battleScribeUnit) {
            BattleScribeUnitResponse.fromModel(form.battleScribeUnit)
        } else {
            null
        }
        val killTeam = if (null != form.killTeam) {
            KillTeamResponse.fromModel(form.killTeam)
        } else {
            null
        }
        return ModelRequest(
            name = form.name,
            unitCount = form.unitCount,
            terrain = form.terrain,
            status = UserStatusResponse.fromModel(form.status!!),
            groups = form.groups.map { ModelGroupResponse.fromModel(it) }.toImmutableList(),
            battleScribeUnit = bsUnit,
            killTeam = killTeam
        )
    }

    suspend fun createModelProgress(modelId: Int, formData: ModelProgressFormData): ModelProgress {
        if (formData.images.isEmpty()) {
            val request = ModelProgressRequest(
                title = formData.title?:"",
                description = formData.description,
                status = UserStatusResponse.fromModel(formData.status!!),
                time = formData.time,
                dateTime = Instant.now().atOffset(ZoneOffset.of("+3"))
            )
            return wipApi.createModelProgress(modelId, request).mapToModel()
        } else {
            val imageParts = Array<MultipartBody.Part>(size = formData.images.size, init = { index: Int ->
                val image = formData.images[index]
                val byteArrayOutputStream = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val requestBody: RequestBody = byteArrayOutputStream.toByteArray()
                    .toRequestBody(contentType = "image/*".toMediaTypeOrNull())
                val name = "${modelId}_${Instant.now()}.jpeg"
                MultipartBody.Part.createFormData("images", name, requestBody)
            })
            val body = HashMap<String, RequestBody>()
            body["title"] = (formData.title?:"").toRequestBody(MultipartBody.FORM)
            body["description"] = (formData.description?:"").toRequestBody()
            body["time"] = formData.time.toString().toRequestBody(MultipartBody.FORM)
            body["datetime"] = Instant.now().atOffset(ZoneOffset.UTC).toString().toRequestBody(MultipartBody.FORM)
            body["user_status"] = formData.status!!.id.toString().toRequestBody(MultipartBody.FORM)
            return wipApi.createModelProgress(modelId, body, imageParts).mapToModel()
        }
    }

    suspend fun updateModelProgress(
        modelId: Int,
        progressId: Int,
        formData: ModelProgressFormData
    ): ModelProgress {
        if (formData.images.isEmpty()) {
            val request = ModelProgressRequest(
                title = formData.title?:"",
                description = formData.description,
                status = UserStatusResponse.fromModel(formData.status!!),
                time = formData.time,
                dateTime = Instant.now().atOffset(ZoneOffset.of("+3"))
            )
            return wipApi.updateModelProgress(modelId, progressId, request).mapToModel()
        } else {
            val imageParts = Array<MultipartBody.Part>(size = formData.images.size, init = { index: Int ->
                val image = formData.images[index]
                val byteArrayOutputStream = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val requestBody: RequestBody = byteArrayOutputStream.toByteArray()
                    .toRequestBody(contentType = "image/*".toMediaTypeOrNull())
                val name = "${modelId}_${Instant.now()}.jpeg"
                MultipartBody.Part.createFormData("images", name, requestBody)
            })
            val body = HashMap<String, RequestBody>()
            body["title"] = (formData.title?:"").toRequestBody(MultipartBody.FORM)
            body["description"] = (formData.description?:"").toRequestBody()
            body["time"] = formData.time.toString().toRequestBody(MultipartBody.FORM)
            body["datetime"] = Instant.now().atOffset(ZoneOffset.UTC).toString().toRequestBody(MultipartBody.FORM)
            body["user_status"] = formData.status!!.id.toString().toRequestBody(MultipartBody.FORM)
            return wipApi.updateModelProgress(modelId, progressId, body, imageParts).mapToModel()
        }
    }

    suspend fun deleteModelProgress(modelId: Int, progressId: Int) {
        wipApi.deleteModelProgress(modelId, progressId)
    }

    suspend fun createModelImage(modelId: Int, images: List<Bitmap>): List<ModelImage> {
        val imageParts = Array<MultipartBody.Part>(size = images.size, init = { index: Int ->
            val image = images[index]
            val byteArrayOutputStream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val requestBody: RequestBody = byteArrayOutputStream.toByteArray()
                .toRequestBody(contentType = "image/*".toMediaTypeOrNull())
            val name = "${modelId}_${Instant.now()}.jpeg"
            MultipartBody.Part.createFormData("images", name, requestBody)
        })
        return wipApi.createModelImage(modelId, imageParts).map { it.mapToModel() }
            .toImmutableList()
    }

    suspend fun deleteModelImage(modelId: Int, imageId: Int) {
        wipApi.deleteModelImage(modelId, imageId)
    }
}