package com.example.cesk.reusable_interface.dialogs

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ConstructionDialogViewModel: ViewModel() {

    private var enduranceAddDialog = mutableStateOf(false)
    private var constructionDeleteDialog = mutableStateOf(false)
    private var typeMenuSwitch = mutableStateOf(false)

    fun setEnduranceAddDialog(value: Boolean){
        enduranceAddDialog.value = value
    }

    fun setConstructionDeleteDialog(value: Boolean){
        constructionDeleteDialog.value = value
    }

    fun setTypeMenuSwitch(value: Boolean){
        typeMenuSwitch.value = value
    }

    fun getEnduranceAddDialog(): Boolean{
        return enduranceAddDialog.value
    }

    fun getConstructionDeleteDialog(): Boolean {
        return constructionDeleteDialog.value
    }

    fun getTypeMenuSwitch(): Boolean {
        return typeMenuSwitch.value
    }
}