package com.example.cesk.model.enums

import java.io.Serializable

enum class ConstructionType(val value: String?):Serializable {
    NOTHING(null),
    WALL("Стена"),
    PLATE("Плита"),
    COLUMN("Колонна"),
    PYLON("Пилон"),
    MONOLITHIC("Монолитный блок"),
    FOUNDATION("Фундамент"),
    BEAM("Балка")
}