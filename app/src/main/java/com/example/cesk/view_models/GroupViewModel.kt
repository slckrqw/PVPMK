package com.example.cesk.view_models

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import com.example.cesk.model.Group

class GroupViewModel: ViewModel() {
    private var groupList: MutableList<Group> = mutableListOf()
    private var currentIndex = mutableIntStateOf(0)

    fun setIndex(index: Int){
        currentIndex.intValue = index
    }
    fun getIndex(): Int{
        return currentIndex.intValue
    }

    fun setGroupList(value: MutableList<Group>){
        groupList = value
    }
    fun getGroupList(): List<Group>{
        return groupList
    }
    fun getCurrentGroup(): Group?{
        return groupList.find {
            it.index == currentIndex.intValue
        }
    }
    fun addGroup(
        name: String,
        image: String = "null"
    ){
        var index = (2..1000).random()
        if(groupList.isNotEmpty()){
            while(!groupList.none{
                    it.index == index
                }
            ){
                index = (2..1000).random()
            }
        }

        groupList.add(
            Group(name, image, index = index)
        )
    }

    fun editGroup(
        name: String,
        image: String = "null"
    ){
        getCurrentGroup()?.name = name
        getCurrentGroup()?.image = image
    }
    fun deleteGroup(){
        groupList.remove(
            groupList.find {
                it.index == currentIndex.intValue
            }
        )
        currentIndex.intValue = 0
    }
}