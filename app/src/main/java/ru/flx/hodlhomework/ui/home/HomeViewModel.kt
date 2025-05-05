package ru.flx.hodlhomework.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.flx.hodlhomework.BuildConfig
import ru.flx.hodlhomework.common.base.BaseViewModel
import ru.flx.hodlhomework.repositories.notification.NotificationManager
import ru.flx.hodlhomework.repositories.rest_api.TransactionResponse
import ru.flx.hodlhomework.ui.data.CoinTransaction
import javax.inject.Inject


data class HomeUiState(
    val balanceAmount: Long? = null,
    val amountToSend: Long? = null,
    val addressToSend: String = "",
    val transactionsList: MutableList<CoinTransaction> = mutableListOf<CoinTransaction>(),
    val showDialog: DialogUiState = DialogUiState(),
)

data class DialogUiState(
    val isShowDialog: Boolean = false,
    val txId: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    val homeInteractor: HomeInteractor,
    notificationManager: NotificationManager
): BaseViewModel<HomeUiState>(notificationManager) {

    fun updateState(
        balanceAmount: Long? = null,
        amountToSend: Long? = null,
        addressToSend: String? = null,
    ) {
        _state.update {
            it.copy(
                balanceAmount = balanceAmount ?: it.balanceAmount,
                amountToSend = amountToSend ?: it.amountToSend,
                addressToSend = addressToSend ?: it.addressToSend
            )
        }
    }

    fun clearAmountToSend() {
        _state.update {
            it.copy(
                amountToSend = null,
            )
        }
    }

    fun showDialog(txId: String) {
        _state.update {
            it.copy(
                showDialog = DialogUiState(
                    true,
                    txId
                )
            )
        }
    }

    fun dismissDialog() {
        _state.update {
            it.copy(
                showDialog = DialogUiState()
            )
        }
    }

    fun sendBtnClick() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var reuslt = homeInteractor.send(
                    addressToSend = state.value.addressToSend.toString(),
                    amountToSend = state.value.amountToSend ?: 0,
                )
                var newBalance = state.value.balanceAmount?.plus(reuslt.amount)
                updateState(balanceAmount = newBalance)

                var newList = mutableListOf(reuslt)
                newList.addAll(state.value.transactionsList)
                _state.update {
                    it.copy(
                        transactionsList = newList
                    )
                }
                showDialog(reuslt.txId)
            } catch (e: Exception) {
                exceptionCatcher(e)
            }
        }
    }

    fun balance() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = homeInteractor.getBalance(BuildConfig.KEY)
                updateState(balanceAmount = result)
            } catch (e: Exception) {
                exceptionCatcher(e)
            }
        }
    }

    fun getTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (state.value.transactionsList.isEmpty()) {
                    val result = homeInteractor.getTransactions(address = BuildConfig.KEY)
                    _state.update {
                        it.copy(transactionsList = result)
                    }
                } else {
                    val result = homeInteractor.getTransactionsByLastTxId(address = BuildConfig.KEY, state.value.transactionsList.last().lastId)
                    val newList = mutableListOf<CoinTransaction>()
                    newList.addAll(state.value.transactionsList)
                    newList.addAll(result)
                    _state.update {
                        it.copy(transactionsList = newList)
                    }
                }
            } catch (e: Exception) {
                exceptionCatcher(e)
            }
        }
    }

    override fun initUiState(): MutableStateFlow<HomeUiState> {
        return MutableStateFlow(HomeUiState())
    }
}