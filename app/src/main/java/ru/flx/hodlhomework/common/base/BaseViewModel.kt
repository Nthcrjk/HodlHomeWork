package ru.flx.hodlhomework.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ru.flx.hodlhomework.common.exceptions.MempoolException
import ru.flx.hodlhomework.repositories.notification.NotificationEvent
import ru.flx.hodlhomework.repositories.notification.NotificationManager
import java.lang.IllegalArgumentException

abstract class BaseViewModel<S>(
    val notificationSystem: NotificationManager
): ViewModel() {

    protected val _state: MutableStateFlow<S> = initUiState()
    val state = _state.asStateFlow()


    protected suspend fun exceptionCatcher(e: Exception) {
        when (e) {
            is MempoolException -> {
                notificationSystem.notify(NotificationEvent.ShowToast(e.message))
            }
            is IllegalArgumentException -> {
                val msg = e.message ?: "Unknown error"
                notificationSystem.notify(NotificationEvent.ShowToast(msg))
            }
            else -> {
                notificationSystem.notify(NotificationEvent.ShowToast("Unknown error"))
            }
        }
    }

    protected abstract fun initUiState(): MutableStateFlow<S>


}