package com.example.cesk.model

import android.net.Uri

data class Group(
    var name: String = "",
    var image: Uri? = null,
    val constructions: MutableList<Construction> = mutableListOf(Construction())
)
