package com.example.cesk.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.cesk.model.enums.ConstructType

data class Construction(
    var type: ConstructType = ConstructType.NOTHING,
    val tests: SnapshotStateList<Double> = mutableStateListOf(),
    var averageEndurance: Double = 0.0,
    var note: String = "",
    var point: Point = Point()
)
