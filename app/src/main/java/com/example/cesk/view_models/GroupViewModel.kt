package com.example.cesk.view_models

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.cesk.model.Group
import com.example.cesk.model.Image

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
        uri: String? = null
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

        val image = Image(
            id = if(!uri.isNullOrEmpty()){
                1
            }else 0,
            uri = uri
        )

        groupList.add(
            Group(name, image, index = index)
        )
    }

    fun editGroup(
        name: String,
        uri: String? = null
    ){
        getCurrentGroup()?.name = name

        getCurrentGroup()?.let{
            it.image.uri = uri
            it.image.id = if(uri.isNullOrEmpty()){
                  0
            }else 1
        }
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