package ru.flx.hodlhomework.ui.navigation

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.flx.hodlhomework.common.base.BaseViewModel
import ru.flx.hodlhomework.repositories.notification.NotificationManager
import javax.inject.Inject

data class UiState(
    val isLoading: Boolean = false
)



@HiltViewModel
class HomeWorkNavGraphViewModel @Inject constructor(
    notificationManager: NotificationManager
): BaseViewModel<UiState>(notificationManager) {

    override fun initUiState(): MutableStateFlow<UiState> {
        return MutableStateFlow(UiState())
    }

}

