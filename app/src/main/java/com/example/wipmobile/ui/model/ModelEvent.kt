package com.example.wipmobile.ui.model

import android.graphics.Bitmap
import com.example.wipmobile.data.model.Model


sealed class ModelEvent {
    class Select(val model: Model) : ModelEvent()
    data object ClearError : ModelEvent()
    data object Refresh : ModelEvent()
    class UploadImages(val model: Model, val images: List<Bitmap>, val resetCallback: () -> Unit) :
        ModelEvent()
}