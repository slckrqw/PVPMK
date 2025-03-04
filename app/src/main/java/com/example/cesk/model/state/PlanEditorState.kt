package com.example.cesk.model.state

import com.example.cesk.model.enums.DialogType

data class PlanEditorState(
    var constructionDialog: Boolean = false,
    var pointsVisibility: Boolean = false,
    var groupsCardView: Boolean = false,
    var constructionDialogType: DialogType = DialogType.ADD,
    var canvasScale: Float = 1.0f
)
