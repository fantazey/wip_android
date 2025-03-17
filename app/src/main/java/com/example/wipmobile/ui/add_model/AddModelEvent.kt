package com.example.wipmobile.ui.add_model

import com.example.wipmobile.data.dto.AddModelFormData
import com.example.wipmobile.data.model.Model

sealed class AddModelEvent {
    class SaveModel(
        val formData: AddModelFormData,
        val successCallback: (model: Model) -> Unit,
        val errorCallback: () -> Unit
    ) : AddModelEvent()

    data object LoadData : AddModelEvent()
    data object ClearError : AddModelEvent()
}