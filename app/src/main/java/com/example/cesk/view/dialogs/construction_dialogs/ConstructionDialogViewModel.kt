package com.example.cesk.view.dialogs.construction_dialogs

import androidx.lifecycle.ViewModel
import com.example.cesk.model.enums.ConstructionType
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

    fun onConstructionTypeChange(newType: ConstructionType){
        _constructionDialogState.update {
            it.copy(
                constructionType = newType
            )
        }
    }

    fun onConstructionNoteChange(newNote: String){
        _constructionDialogState.update {
            it.copy(
                constructionNote = newNote
            )
        }
    }

    fun getAverageEndurance(): Double
    {
        return _constructionDialogState.value.testsList.let{
            it.sum()/it.size
        }
    }
}