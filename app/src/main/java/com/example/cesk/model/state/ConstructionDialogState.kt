package com.example.cesk.model.state

import androidx.compose.runtime.mutableStateOf

data class ConstructionDialogState(
    var enduranceAddDialog: Boolean = false,
    var constructionDeleteDialog: Boolean = false,
    var typeMenuSwitch: Boolean = false,
)
