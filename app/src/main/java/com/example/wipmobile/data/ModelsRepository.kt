package com.example.wipmobile.data

import android.util.Log
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.source.remote.ModelRemoteDataSource
import javax.inject.Inject

class ModelsRepository @Inject constructor(
    private val remoteDataSource: ModelRemoteDataSource
) {

    suspend fun getModels(): Array<Model> {
        val models = remoteDataSource.getModels()
        Log.i("model repo", "models loaded" + models.size)
        return models
    }
}