package com.example.cesk.reusable_interface.dialogs.constructionDialog

import androidx.lifecycle.ViewModel
import com.example.cesk.model.state.ConstructionDialogState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConstructionDialogViewModel: ViewModel() {

    private var _constructionDialogState = MutableStateFlow(ConstructionDialogState())

    val constructionDialogState: StateFlow<ConstructionDialogState> = _constructionDialogState.asStateFlow()


    fun onConstructionDelete(){
        _constructionDialogState.update {
            it.copy(
                constructionDeleteDialog = !it.constructionDeleteDialog
            )
        }
    }

    fun onTypeSwitch(){
        _constructionDialogState.update {
            it.copy(
                typeMenuSwitch = !it.typeMenuSwitch
            )
        }
    }

    fun onEnduranceAdd(){
        _constructionDialogState.update {
            it.copy(
                enduranceAddDialog = !it.enduranceAddDialog
            )
        }
    }
}