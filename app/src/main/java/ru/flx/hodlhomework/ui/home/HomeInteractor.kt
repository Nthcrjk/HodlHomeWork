package ru.flx.hodlhomework.ui.home

import ru.flx.hodlhomework.common.base.BaseInteractor
import ru.flx.hodlhomework.repositories.bitcoinj.BitcoinjRepository
import ru.flx.hodlhomework.repositories.rest_api.ApiService
import ru.flx.hodlhomework.repositories.rest_api.TransactionResponse
import ru.flx.hodlhomework.repositories.rest_api.UtxoResponse
import ru.flx.hodlhomework.ui.data.CoinTransaction

class HomeInteractor(
    private val bitcoinjRepository: BitcoinjRepository,
    private val restApi: ApiService
): BaseInteractor() {
    
    suspend fun send(addressToSend: String, amountToSend: Long): CoinTransaction {
        var result = bitcoinjRepository.sendCoins(addressToSend, amountToSend)
        return bitcoinjRepository.getTxInfo("result")
    }

    suspend fun getBalance(address: String): Long {
        val response = bitcoinjRepository.getBalanceByKey(address)
        return response.sumOf { it.value }
    }

    suspend fun getTransactions(address: String): MutableList<CoinTransaction> {
        return bitcoinjRepository.getTransactionsList(address)
    }

    suspend fun getTransactionsByLastTxId(address: String, lastTxId: String): MutableList<CoinTransaction> {
        return bitcoinjRepository.getTransactionByLastTxId(address, lastTxId)
    }

}