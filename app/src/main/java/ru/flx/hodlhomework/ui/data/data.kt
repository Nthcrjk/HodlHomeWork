package ru.flx.hodlhomework.ui.data

import ru.flx.hodlhomework.repositories.rest_api.TransactionResponse


data class CoinTransaction(
    val txId: String,
    val isReceived: Boolean,
    val timestamp: Long,
    val amount: Long,
    val lastId: String
) {
    companion object  {
        fun fromTransactionResponse(response: TransactionResponse, myAddress: String) : CoinTransaction {
            response.apply {
                val totalSentFromMe = vin
                    .filter { it.prevout.scriptpubkey_address == myAddress }
                    .sumOf { it.prevout.value }

                val totalReceivedToMe = vout
                    .filter { it.scriptpubkey_address == myAddress }
                    .sumOf { it.value }

                val isReceived = totalReceivedToMe > 0 && totalSentFromMe == 0L

                val value = totalReceivedToMe - totalSentFromMe
                return CoinTransaction(
                    txid,
                    isReceived,
                    status.block_time,
                    amount = value,
                    vin.first().txid
                )
            }
        }
    }
}