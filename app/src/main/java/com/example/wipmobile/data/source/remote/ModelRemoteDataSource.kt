package com.example.wipmobile.data.source.remote

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
import com.example.wipmobile.data.source.remote.api.WipApi
import com.example.wipmobile.data.source.remote.api.request.ModelGroupRequest
import com.example.wipmobile.data.source.remote.api.request.ModelProgressRequest
import com.example.wipmobile.data.source.remote.api.request.ModelRequest
import com.example.wipmobile.data.source.remote.api.request.UserStatusRequest
import com.google.gson.Gson
import com.google.gson.JsonElement
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.toImmutableList
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.ZoneOffset
import javax.inject.Inject

class ModelRemoteDataSource @Inject constructor(
    private val wipApi: WipApi,
    private val gson: Gson
) {

    private fun <T> responseHandler(response: Response<T>): T {
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val errorBody: String = response.errorBody()?.string()?.let {
                val errorBody: JsonElement = gson.toJsonTree(it)
                if (errorBody.isJsonObject) {
                    listOf("error", "detail").map { errorKey: String ->
                        if (errorBody.asJsonObject.has(errorKey)) {
                            errorBody.asJsonObject.get(errorKey)
                        }
                    }
                }
                "Ошибка ${response.code()}"
            } ?: run {
                "Ошибка ${response.code()}"
            }
            throw Exception(errorBody)
        }
    }

    suspend fun getModels(
        name: String = "",
        page: Int = 1,
        statuses: List<UserStatus> = emptyList(),
        modelGroups: List<ModelGroup> = emptyList()
    ): ModelsPage {
        val result = wipApi.getUserModels(
            statuses = statuses.map { it.id }.toImmutableList(),
            modelGroups = modelGroups.map { it.id }.toImmutableList(),
            name = name,
            page = page
        )
        return responseHandler(result).mapToModel()
    }

    suspend fun getModel(id: Int): Model {
        val result = wipApi.getModel(id)
        return responseHandler(result).mapToModel()
    }

    suspend fun getModelImages(modelId: Int): List<ModelImage> {
        val result = wipApi.getModelImages(modelId)
        return responseHandler(result).map { it.mapToModel() }.toImmutableList()
    }

    suspend fun getModelProgress(modelId: Int): List<ModelProgress> {
        val result = wipApi.getModelProgress(modelId)
        return responseHandler(result).map { it.mapToModel() }.toImmutableList()
    }

    suspend fun getUserStatusList(): List<UserStatus> {
        val result = wipApi.getUserStatuses()
        return responseHandler(result).map { it.mapToModel() }.toImmutableList()
    }

    suspend fun getKillTeamList(): List<KillTeam> {
        val result = wipApi.getKillTeams()
        return responseHandler(result).map { it.mapToModel() }.toImmutableList()
    }

    suspend fun getBattleScribeUnitList(): List<BattleScribeUnit> {
        val result = wipApi.getBattleScribeUnits()
        return responseHandler(result).map { it.mapToModel() }.toImmutableList()
    }

    suspend fun getBattleScribeCategoryList(): List<BattleScribeCategory> {
        val result = wipApi.getBattleScribeCategories()
        return responseHandler(result).map { it.mapToModel() }.toImmutableList()
    }

    suspend fun getModelGroups(): List<ModelGroup> {
        val result = wipApi.getModelGroups()
        return responseHandler(result).map { it.mapToModel() }.toImmutableList()
    }

    suspend fun createModel(form: ModelFormData): Model {
        val result = wipApi.createModel(prepareModelRequest(form))
        return responseHandler(result).mapToModel()
    }

    suspend fun updateModel(modelId: Int, formData: ModelFormData): Model {
        val result = wipApi.updateModel(modelId, prepareModelRequest(formData))
        return responseHandler(result).mapToModel()
    }

    private fun prepareModelRequest(form: ModelFormData): ModelRequest {
        return ModelRequest(
            name = form.name,
            unitCount = form.unitCount,
            terrain = form.terrain,
            status = form.status!!.id,
            groups = form.groups.map { it.id }.toImmutableList(),
            battleScribeUnit = form.battleScribeUnit?.id,
            killTeam = form.killTeam?.id
        )
    }

    suspend fun createModelProgress(modelId: Int, formData: ModelProgressFormData): ModelProgress {
        val request = ModelProgressRequest(
            title = formData.title,
            description = formData.description,
            status = formData.status!!.id,
            time = formData.time,
            dateTime = Instant.now().atOffset(ZoneOffset.of("+3")).toString()
        )
        val result = wipApi.createModelProgress(modelId, request)
        return responseHandler(result).mapToModel()
    }

    suspend fun updateModelProgress(
        modelId: Int,
        progressId: Int,
        formData: ModelProgressFormData
    ): ModelProgress {
        val request = ModelProgressRequest(
            title = formData.title,
            description = formData.description,
            status = formData.status!!.id,
            time = formData.time,
            dateTime = Instant.now().atOffset(ZoneOffset.of("+3")).toString()
        )
        val result = wipApi.updateModelProgress(modelId, progressId, request)
        return responseHandler(result).mapToModel()
    }

    suspend fun deleteModelProgress(modelId: Int, progressId: Int) {
        val result = wipApi.deleteModelProgress(modelId, progressId)
        responseHandler(result)
    }

    suspend fun createModelImage(
        modelId: Int,
        progressId: Int?,
        images: List<Bitmap>
    ): List<ModelImage> {
        val imageParts = Array<MultipartBody.Part>(size = images.size, init = { index: Int ->
            val image = images[index]
            val byteArrayOutputStream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val requestBody: RequestBody = byteArrayOutputStream.toByteArray()
                .toRequestBody(contentType = "image/*".toMediaTypeOrNull())
            val name = "${modelId}_${Instant.now()}.jpeg"
            MultipartBody.Part.createFormData("images", name, requestBody)
        })
        if (progressId != null) {
            val body = HashMap<String, RequestBody>()
            body["progress"] = (progressId.toString()).toRequestBody(MultipartBody.FORM)
            val result = wipApi.createModelImage(modelId, body, imageParts)
            return responseHandler(result).map { it.mapToModel() }.toImmutableList()
        }
        val result = wipApi.createModelImage(modelId, imageParts)
        return responseHandler(result).map { it.mapToModel() }.toImmutableList()
    }

    suspend fun deleteModelImage(modelId: Int, imageId: Int) {
        val result = wipApi.deleteModelImage(modelId, imageId)
        responseHandler(result)
    }

    suspend fun createStatus(form: StatusFormData): UserStatus {
        val result = wipApi.createStatus(UserStatusRequest(
            name = form.name,
            order = form.order,
            previous = form.previous?.id,
            next = form.next?.id,
            isInitial = form.isInitial,
            isFinal = form.isFinal
        ))
        return responseHandler(result).mapToModel()
    }

    suspend fun updateStatus(status: UserStatus, form: StatusFormData): UserStatus {
        val result = wipApi.updateStatus(status.id, UserStatusRequest(
            name = form.name,
            order = form.order,
            previous = form.previous?.id,
            next = form.next?.id,
            isInitial = form.isInitial,
            isFinal = form.isFinal
        ))
        return responseHandler(result).mapToModel()
    }

    suspend fun deleteStatus(status: UserStatus) {
        responseHandler(wipApi.deleteStatus(status.id))
    }

    suspend fun createModelGroup(form: ModelGroupFormData): ModelGroup {
        val result = wipApi.createModelGroup(ModelGroupRequest(
            name = form.name
        ))
        return responseHandler(result).mapToModel()
    }

    suspend fun updateModelGroup(group: ModelGroup, form: ModelGroupFormData): ModelGroup {
        val result = wipApi.updateModelGroup(group.id, ModelGroupRequest(
            name = form.name
        ))
        return responseHandler(result).mapToModel()
    }

    suspend fun deleteModelGroup(group: ModelGroup) {
        responseHandler(wipApi.deleteModelGroup(group.id))
    }
}