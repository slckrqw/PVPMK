package com.example.cesk.plan_editor

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cesk.model.enums.DialogType

class PlanEditorViewModel: ViewModel() {

    private var expandTools = mutableStateOf(false)
    private var addGroup = mutableStateOf(false)
    private var addConstruction = mutableStateOf(false)
    private var groupSettings = mutableStateOf(false)
    private var groupsMenu = mutableStateOf(false)


    private var constructionDialogType = mutableStateOf(DialogType.ADD)
    private var groupDialogType = mutableStateOf(DialogType.ADD)

    private var canvasScale = mutableFloatStateOf(1.0f)

    fun setTools(value: Boolean){
        expandTools.value = value
    }
    fun setAddGroup(value: Boolean){
        addGroup.value = value
    }

    fun setAddConstruction(value: Boolean){
        addConstruction.value = value
    }

    fun setGroupSettings(value: Boolean){
        groupSettings.value = value
    }

    fun setGroupsMenu(value: Boolean){
        groupsMenu.value = value
    }

    fun setConstructionDialogType(value: DialogType){
        constructionDialogType.value = value
    }

    fun setGroupDialogType(value: DialogType){
        groupDialogType.value = value
    }

    fun getTools(): Boolean {
        return expandTools.value
    }

    fun getAddGroup(): Boolean {
        return addGroup.value
    }

    fun getAddConstruction(): Boolean {
        return addConstruction.value
    }

    fun getGroupSettings(): Boolean {
        return groupSettings.value
    }

    fun getGroupsMenu(): Boolean{
        return groupsMenu.value
    }

    fun getConstructionDialogType(): DialogType{
        return constructionDialogType.value
    }

    fun getGroupDialogType(): DialogType{
        return groupDialogType.value
    }

    fun getCanvasScale(): Float{
        return canvasScale.floatValue
    }
}