package com.example.cesk.model

import com.example.cesk.model.enums.ConstructionType
import java.io.Serializable

data class Construction(
    var type: ConstructionType = ConstructionType.NOTHING,
    var tests: MutableList<Double> = mutableListOf(),
    var averageEndurance: Double = 0.0,
    var note: String = "",
    var point: Point = Point()
): Serializable
