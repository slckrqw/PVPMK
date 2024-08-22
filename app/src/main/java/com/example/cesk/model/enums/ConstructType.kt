package com.example.cesk.model.enums

import java.io.Serializable

enum class ConstructType(val value: String?):Serializable {
    NOTHING(null),
    WALL("Стена"),
    PLATE("Плита")
}