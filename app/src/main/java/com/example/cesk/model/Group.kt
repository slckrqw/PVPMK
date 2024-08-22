package com.example.cesk.model

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import java.io.Serializable


data class Group(
    var name: String = "",
    var image: Image = Image(),
    val constructions: MutableList<Construction> = mutableListOf(),
    var index: Int = 0
):Serializable
