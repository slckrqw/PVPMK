package com.example.cesk.navigation

open class Screen (val route: String){
    object StartScreen: Screen("start_screen")
    object PlanEditor: Screen("plan_editor")
}