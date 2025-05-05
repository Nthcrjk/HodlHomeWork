package ru.flx.hodlhomework.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.compose

@Composable
fun HomeRoute() {

    val viewModel = hiltViewModel<HomeViewModel>()
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.balance()
        viewModel.getTransactions()
    }

    HomeScreen(
        balanceAmount = state.value.balanceAmount,
        amountToSend = state.value.amountToSend,
        addressToSend = state.value.addressToSend,
        state.value.showDialog,
        state.value.transactionsList,
        onChangeAmountToSend = {
            viewModel.updateState(amountToSend = it)
        },
        onChangeAddressToSend = {
            viewModel.updateState(addressToSend = it)
        },
        onLoadMoreTransactions = {
            viewModel.getTransactions()
        },
        onSend = {
            viewModel.sendBtnClick()
        },
        onDismissDialog =  {

        },
        onTxIdClick = {

        }
    )
}