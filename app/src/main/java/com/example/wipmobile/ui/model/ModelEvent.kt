package com.example.wipmobile.ui.model

import android.graphics.Bitmap
import com.example.wipmobile.data.dto.ModelFormData
import com.example.wipmobile.data.dto.ModelProgressFormData
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelImage
import com.example.wipmobile.data.model.ModelProgress


sealed class ModelEvent {
    class Select(val model: Model, val tab: Int = 0) : ModelEvent()
    class SelectTab(val tab: Int) : ModelEvent()
    data object ClearError : ModelEvent()
    data object Refresh : ModelEvent()

    class UploadImages(val model: Model, val images: List<Bitmap>, val resetCallback: () -> Unit) :
        ModelEvent()
    class DeleteImage(
        val model: Model,
        val images: List<ModelImage>,
        val successCallback: () -> Unit
    ) : ModelEvent()

    class UpdateModel(val model: Model, val data: ModelFormData, val successCallback: () -> Unit) :
        ModelEvent()

    class SelectModelProgress(val model: Model, val progress: ModelProgress) : ModelEvent()
    class CreateModelProgress(
        val model: Model,
        val data: ModelProgressFormData,
        val successCallback: () -> Unit
    ) : ModelEvent()
    class UpdateModelProgress(
        val model: Model,
        val modelProgress: ModelProgress,
        val data: ModelProgressFormData,
        val successCallback: () -> Unit
    ) : ModelEvent()
    class DeleteModelProgress(
        val model: Model,
        val progress: ModelProgress,
        val successCallback: () -> Unit
    ) : ModelEvent()

}