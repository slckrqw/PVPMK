package com.example.cesk.model

import java.io.Serializable


data class Group(
    var name: String = "",
    var image: String = "null",
    val constructions: MutableList<Construction> = mutableListOf(),
    var index: Int = 0
):Serializable
