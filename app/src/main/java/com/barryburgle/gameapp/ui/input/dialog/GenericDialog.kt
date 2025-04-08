package com.barryburgle.gameapp.ui.input.dialog

class GenericDialog {
    companion object {
        fun getButtonTitle(stateString: String, addToState: String, buttonDescription: String) =
            if (stateString == null || stateString.isBlank()) buttonDescription else addToState + stateString
    }
}