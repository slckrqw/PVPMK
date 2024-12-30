package com.example.cesk.view.plan_editor

import androidx.lifecycle.ViewModel
import com.example.cesk.model.enums.DialogType
import com.example.cesk.model.enums.FileAccessType
import com.example.cesk.model.state.PlanEditorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlanEditorViewModel: ViewModel() {

    private var _planEditorState = MutableStateFlow(PlanEditorState())

    val planEditorState: StateFlow<PlanEditorState> = _planEditorState.asStateFlow()

    fun onConstructionDialogChange(){
        _planEditorState.update {
            it.copy(
                constructionDialog = !it.constructionDialog
            )
        }
    }

    fun onPointsVisibilityChange(){
        _planEditorState.update {
            it.copy(
                pointsVisibility = !it.pointsVisibility
            )
        }
    }

    fun setConstructionDialogType(newType: DialogType){
        _planEditorState.update {
            it.copy(
                constructionDialogType = newType
            )
        }
    }

    fun onGroupsCardViewChange(){
        _planEditorState.update {
            it.copy(
                groupsCardView = !it.groupsCardView
            )
        }
    }
}