package com.example.cesk.plan_editor

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cesk.model.enums.ClickType
import com.example.cesk.model.enums.DialogType

class PlanEditorViewModel: ViewModel() {

    private var expandTools = mutableStateOf(false)
    private var addGroup = mutableStateOf(false)
    private var addConstruction = mutableStateOf(false)

    private var clickType = mutableStateOf(ClickType.ADD)
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

    fun setClickType(value: ClickType){
        clickType.value = value
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
    fun getClickType(): ClickType{
        return clickType.value
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

    fun increaseCanvasScale(){
        canvasScale.floatValue += 0.1f
    }

    fun decreaseCanvasScale(){
        canvasScale.floatValue -= 0.1f
    }
}