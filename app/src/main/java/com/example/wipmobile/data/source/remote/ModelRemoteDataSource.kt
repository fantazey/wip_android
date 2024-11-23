package com.example.wipmobile.data.source.remote

import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.source.remote.api.WipApi
import javax.inject.Inject

class ModelRemoteDataSource @Inject constructor(
    private val wipApi: WipApi
){
    suspend fun getModels(): Array<Model> {
        return wipApi.getUserModels()
    }


}