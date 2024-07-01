package com.example.cesk.model

data class Construction(
    var type: ConstructType = ConstructType.STANDARD,
    val tests: MutableList<Double> = mutableListOf(33.0,23.0,33.0),
    val averageEndurance: Double = 0.0
)
