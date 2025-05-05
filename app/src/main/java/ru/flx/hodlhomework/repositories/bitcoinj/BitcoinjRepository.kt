package ru.flx.hodlhomework.repositories.bitcoinj

import android.util.Log
import okhttp3.RequestBody
import org.bitcoinj.base.Address
import org.bitcoinj.base.AddressParser
import org.bitcoinj.base.Coin
import org.bitcoinj.base.ScriptType
import org.bitcoinj.base.Sha256Hash
import org.bitcoinj.core.Transaction
import org.bitcoinj.core.TransactionInput
import org.bitcoinj.core.TransactionOutPoint
import org.bitcoinj.core.TransactionWitness
import org.bitcoinj.crypto.DumpedPrivateKey
import org.bitcoinj.params.SigNetParams
import org.bitcoinj.script.ScriptBuilder
import ru.flx.hodlhomework.BuildConfig
import ru.flx.hodlhomework.common.base.BaseInteractor
import ru.flx.hodlhomework.repositories.rest_api.ApiService
import ru.flx.hodlhomework.repositories.rest_api.UtxoResponse
import ru.flx.hodlhomework.ui.data.CoinTransaction

class BitcoinjRepository(private val restApi: ApiService): BaseInteractor() {


    suspend private fun getUtxoList(address: String): MutableList<UtxoResponse> {
        val utxos = mutableListOf<UtxoResponse>()
        var result = obtainResponse { restApi.jsonApi.getUtxoList(address) }
        utxos.addAll(result)
        return utxos
    }

    suspend fun getTransactionsList(pKey: String): MutableList<CoinTransaction> {
        val address = getAddressFromKey(pKey)

        val result = obtainResponse{ restApi.jsonApi.getStarterTransactionList(address) }
        return result.map { CoinTransaction.fromTransactionResponse(it, address) }.toMutableList()
    }

    suspend fun getTransactionByLastTxId(pKey: String, lastTxId: String): MutableList<CoinTransaction> {
        val address = getAddressFromKey(pKey)
        var result = obtainResponse{ restApi.jsonApi.getTransactionsFromTxId(address, lastTxId) }
        return result.map { CoinTransaction.fromTransactionResponse(it, address) }.toMutableList()
    }



    suspend fun getTxInfo(txId: String): CoinTransaction {
        val address = getAddressFromKey(BuildConfig.KEY)

        var result = CoinTransaction.fromTransactionResponse(
            obtainResponse { restApi.jsonApi.getTxInfo(txId) },
            address
        )
        return result
    }

    suspend fun getBalanceByKey(pKey: String): MutableList<UtxoResponse> {
        val address = getAddressFromKey(pKey)
        return getUtxoList(address)
    }

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun sendCoins(addressToSend: String, amountToSend: Long): String {
        val params = SigNetParams.get()
        var key = DumpedPrivateKey.fromBase58(params.network(), BuildConfig.KEY).key
        var address = key.toAddress(ScriptType.P2WPKH, params.network())

        var outAddress = AddressParser.getDefault(params.network()).parseAddress(addressToSend)
        val tx = Transaction()
        val utxoList = getUtxoList(address.toString())

        val balance = utxoList.sumOf { it.value }
        val feeRate = 2
        var fee =  20
        val inputPrice = 73
        val outputPrice = 33

        utxoList.sortBy { it.value }
        utxoList.reverse()

        val selectedUtxo = mutableListOf<UtxoResponse>()
        var total = 0L

        for (utho in utxoList) {
            selectedUtxo.add(utho)
            fee = fee + (inputPrice * feeRate)
            total = total + utho.value
            if (total >= amountToSend + fee) break
        }

        fee = fee + (outputPrice * feeRate)
        tx.addOutput(Coin.valueOf(amountToSend), outAddress)

        if ((total - amountToSend - fee) != 0L) {
            fee = fee + (outputPrice * feeRate)
            val change = total.minus(amountToSend).minus(fee)
            tx.addOutput(Coin.valueOf(change), address)
        }


        val script = ScriptBuilder.createP2PKHOutputScript(key.pubKeyHash)

        selectedUtxo.forEachIndexed{index, it ->
            val txid = Sha256Hash.wrap(it.txid)
            val vout = it.vout
            val tOutPoint = TransactionOutPoint(vout, txid)
            val input = TransactionInput(tx, byteArrayOf(), tOutPoint, Coin.valueOf(it.value))
            tx.addInput(input)
        }

        for (i in 0 until tx.inputs.size) {
            val txIn = tx.getInput(i.toLong())
            val signature = tx.calculateWitnessSignature(
                i,
                key,
                script,
                tx.inputs[i].value,
                Transaction.SigHash.ALL,
                false
            )
            txIn.witness = TransactionWitness.of(listOf(signature.encodeToBitcoin(), key.pubKey))
        }

        val hex = tx.serialize().toHexString()

        if (amountToSend + fee > balance) {
            throw IllegalArgumentException("Not enough balance")
        }

        return sendToMempool(hex)
    }

    private suspend fun sendToMempool(hex: String): String {
        val type = okhttp3.MediaType.parse("text/plain")
        val requestBody = RequestBody.create(type, hex)
        val result = obtainResponse{ restApi.textApi.sendTransaction(requestBody) }
        return result
    }

    private fun getAddressFromKey(key: String): String {
        val params = SigNetParams.get()
        var key = DumpedPrivateKey.fromBase58(params.network(), key).key
        return key.toAddress(ScriptType.P2WPKH, params.network()).toString()
    }
}