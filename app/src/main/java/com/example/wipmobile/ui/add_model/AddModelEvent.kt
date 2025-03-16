package com.example.wipmobile.ui.add_model

import com.example.wipmobile.data.dto.AddModelFormData

sealed class AddModelEvent {
    class SaveModel(
        val formData: AddModelFormData,
        val successCallback: () -> Unit,
        val errorCallback: () -> Unit
    ) : AddModelEvent()

    data object LoadData : AddModelEvent()
    data object ClearError : AddModelEvent()
}