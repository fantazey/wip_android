package com.example.wipmobile.ui.model

import com.example.wipmobile.data.model.Model


sealed class ModelEvent {
    class Select(val model: Model): ModelEvent()
    data object ClearError: ModelEvent()
    data object Refresh: ModelEvent()
}