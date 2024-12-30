package com.example.cesk.view.tools_card

import androidx.lifecycle.ViewModel
import com.example.cesk.model.enums.FileAccessType
import com.example.cesk.model.state.ToolsCardState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ToolsCardViewModel:ViewModel(){

    private var _toolsCardState = MutableStateFlow(ToolsCardState())

    val toolsCardState: StateFlow<ToolsCardState> = _toolsCardState.asStateFlow()

    fun onToolsCardViewChange(){
        _toolsCardState.update {
            it.copy(
                toolsCardView = !it.toolsCardView
            )
        }
    }

    fun onFileDialogChange(){
        _toolsCardState.update {
            it.copy(
                fileDialog = !it.fileDialog
            )
        }
    }

    fun setFileAccessType(newType: FileAccessType){
        _toolsCardState.update {
            it.copy(
                fileAccessType = newType
            )
        }
    }
}