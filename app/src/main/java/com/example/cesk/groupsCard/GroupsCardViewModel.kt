package com.example.cesk.groupsCard

import androidx.lifecycle.ViewModel
import com.example.cesk.model.state.GroupsCardState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GroupsCardViewModel: ViewModel() {
    private var _groupsCardState = MutableStateFlow(GroupsCardState())

    val groupsCardState: StateFlow<GroupsCardState> = _groupsCardState.asStateFlow()

    fun onGroupDialogChange(){
        _groupsCardState.update {
            it.copy(
                groupDialog = !it.groupDialog
            )
        }
    }

    fun onGroupOptionsChange(){
        _groupsCardState.update {
            it.copy(
                groupOptions = !it.groupOptions
            )
        }
    }
}