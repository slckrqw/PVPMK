package com.example.cesk.model.state

import com.example.cesk.model.enums.FileAccessType

data class ToolsCardState(
    var fileDialog: Boolean = false,
    var toolsCardView: Boolean = false,
    var fileAccessType: FileAccessType = FileAccessType.SAVE,
)
