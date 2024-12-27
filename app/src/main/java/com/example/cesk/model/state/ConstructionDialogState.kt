package com.example.cesk.model.state

import androidx.compose.runtime.mutableStateOf

data class ConstructionDialogState(
    val enduranceAddDialog: Boolean = false,
    val constructionDeleteDialog: Boolean = false,
    val typeMenuSwitch: Boolean = false,
)
