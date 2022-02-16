package com.example.spyfall.ui.viewmodel

import com.example.spyfall.ui.base.BaseViewModel
import com.example.spyfall.utils.generateRandomId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LinkViewModel @Inject constructor() : BaseViewModel() {

    fun generateGameId(): String {
        return generateRandomId()
    }
}
