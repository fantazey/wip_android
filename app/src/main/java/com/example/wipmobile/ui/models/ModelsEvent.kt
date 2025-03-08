package com.example.wipmobile.ui.models

import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.UserStatus

sealed class ModelsEvent {
    data object Load: ModelsEvent()
    class SelectPage(val page: Int): ModelsEvent()
    data object ClearError: ModelsEvent()
    data object RefreshList: ModelsEvent()
    class ChangeModelGroupFilter(val values: List<ModelGroup>): ModelsEvent()
    class ChangeUserStatusFilter(val values: List<UserStatus>): ModelsEvent()
    class ChangeSearchQuery(val value: String): ModelsEvent()
    data object ApplyFilter: ModelsEvent()
}