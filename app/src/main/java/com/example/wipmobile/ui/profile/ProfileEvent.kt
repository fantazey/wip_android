package com.example.wipmobile.ui.profile

import com.example.wipmobile.data.dto.ModelGroupFormData
import com.example.wipmobile.data.dto.StatusFormData
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.UserStatus

sealed class ProfileEvent {
    data object ClearError: ProfileEvent()
    data object LoadData: ProfileEvent()

    class CreateStatus(
        val form: StatusFormData,
        val successCallback: () -> Unit
    ) : ProfileEvent()

    class UpdateStatus(
        val status: UserStatus,
        val form: StatusFormData,
        val successCallback: () -> Unit
    ) : ProfileEvent()

    class DeleteStatus(
        val status: UserStatus,
        val successCallback: () -> Unit
    ) : ProfileEvent()

    class CreateModelGroup(
        val form: ModelGroupFormData,
        val successCallback: () -> Unit
    ) : ProfileEvent()

    class UpdateModelGroup(
        val group: ModelGroup,
        val form: ModelGroupFormData,
        val successCallback: () -> Unit
    ) : ProfileEvent()

    class DeleteModelGroup(
        val group: ModelGroup,
        val successCallback: () -> Unit
    ) : ProfileEvent()
}