package com.example.cesk.reusable_interface.dialogs.constructionDialog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cesk.model.state.ConstructionDialogState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update

class ConstructionDialogViewModel: ViewModel() {

    var constructionDialogState = MutableStateFlow(ConstructionDialogState())

    val typeSwitch: Boolean
        get() = constructionDialogState.value.typeMenuSwitch
    val deleteDialog: Boolean
        get() = constructionDialogState.value.constructionDeleteDialog
    val enduranceAdd: Boolean
        get() = constructionDialogState.value.enduranceAddDialog

    fun onConstructionDelete(){
        val current = deleteDialog

        constructionDialogState.update {
            it.copy(
                constructionDeleteDialog = !current
            )
        }
    }

    fun onTypeSwitch(){
        val current = typeSwitch

        constructionDialogState.update {
            it.copy(
                typeMenuSwitch = !current
            )
        }
    }

    fun onEnduranceAdd(){
        val current = enduranceAdd

        constructionDialogState.update {
            it.copy(
                enduranceAddDialog = !current
            )
        }
    }
}