package com.example.cesk.model

import com.example.cesk.model.enums.ConstructType
import java.io.Serializable

data class Construction(
    var type: ConstructType = ConstructType.NOTHING,
    val tests: MutableList<Double> = mutableListOf(),
    var averageEndurance: Double = 0.0,
    var note: String = "",
    var point: Point = Point()
): Serializable
