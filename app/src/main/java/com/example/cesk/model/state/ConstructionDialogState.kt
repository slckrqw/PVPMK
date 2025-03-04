package com.example.cesk.model.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.cesk.model.enums.ConstructionType

data class ConstructionDialogState(
    val enduranceAddDialog: Boolean = false,
    val constructionDeleteDialog: Boolean = false,
    val typeMenuSwitch: Boolean = false,
    val constructionType: ConstructionType = ConstructionType.NOTHING,
    val constructionNote: String = "",
    val testsList: SnapshotStateList<Double> = mutableStateListOf()
)
