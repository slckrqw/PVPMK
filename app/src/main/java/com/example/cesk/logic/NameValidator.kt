package com.example.cesk.logic

fun validate(
    word: String,
    limit: Int = 20
):Boolean{
    return word.length !in 1..limit
}