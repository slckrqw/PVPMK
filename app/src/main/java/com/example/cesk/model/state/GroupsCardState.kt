package com.example.cesk.model.state

import com.example.cesk.model.enums.DialogType

data class GroupsCardState(
    val groupDialog: Boolean = false,
    val groupOptions: Boolean = false,
    var groupDialogType: DialogType = DialogType.ADD
)
