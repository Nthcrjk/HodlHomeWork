package ru.flx.hodlhomework.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

abstract class BaseViewModel<S>(): ViewModel() {

    protected val _state: MutableStateFlow<S> = initUiState()
    val state = _state.asStateFlow()


    protected fun exceptionCatcher(e: Exception) {

    }

    fun popToBackStack() {

    }

    protected abstract fun initUiState(): MutableStateFlow<S>


}