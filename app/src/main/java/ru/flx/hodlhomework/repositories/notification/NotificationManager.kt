package ru.flx.hodlhomework.repositories.notification

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed class NotificationEvent {
    data class ShowToast(
        val text: String
    ) : NotificationEvent()
}

class NotificationManager() {
    private val _events = MutableSharedFlow<NotificationEvent>()
    val events = _events.asSharedFlow()

    suspend fun notify(event: NotificationEvent) {
        _events.emit(event)
    }
}