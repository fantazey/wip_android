package com.example.wipmobile.ui.models

sealed class ModelsEvent {
    data object ModelsLoad: ModelsEvent()
    data object ModelsLoaded: ModelsEvent()
    class ModelsSelectPage(val page: Int): ModelsEvent()
}