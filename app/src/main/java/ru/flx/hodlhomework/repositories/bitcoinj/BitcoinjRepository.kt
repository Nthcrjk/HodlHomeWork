package ru.flx.hodlhomework.repositories.bitcoinj

import android.util.Log
import okhttp3.RequestBody
import org.bitcoinj.base.AddressParser
import org.bitcoinj.base.Coin
import org.bitcoinj.base.ScriptType
import org.bitcoinj.base.Sha256Hash
import org.bitcoinj.core.Transaction
import org.bitcoinj.core.TransactionOutPoint
import org.bitcoinj.core.TransactionWitness
import org.bitcoinj.crypto.DumpedPrivateKey
import org.bitcoinj.params.SigNetParams
import org.bitcoinj.script.ScriptBuilder
import ru.flx.hodlhomework.BuildConfig
import ru.flx.hodlhomework.common.base.BaseInteractor
import ru.flx.hodlhomework.repositories.rest_api.ApiService
import ru.flx.hodlhomework.repositories.rest_api.TransactionResponse
import ru.flx.hodlhomework.repositories.rest_api.UtxoResponse
import ru.flx.hodlhomework.ui.data.CoinTransaction
import kotlin.text.get
import kotlin.toString

class BitcoinjRepository(private val restApi: ApiService): BaseInteractor() {


    suspend private fun getUtxoList(address: String): MutableList<UtxoResponse> {
        val utxos = mutableListOf<UtxoResponse>()
        var result = obtainResponse { restApi.jsonApi.getUtxoList(address) }
        utxos.addAll(result)
        return utxos
    }

    suspend fun getTransactionsList(pKey: String): MutableList<CoinTransaction> {
        val params = SigNetParams.get()
        var key = DumpedPrivateKey.fromBase58(params.network(), pKey).key
        var address = key.toAddress(ScriptType.P2WPKH, params.network()).toString()

        val result = obtainResponse{ restApi.jsonApi.getStarterTransactionList(address) }
        return result.map { CoinTransaction.fromTransactionResponse(it, address) }.toMutableList()
    }

    suspend fun getTransactionByLastTxId(pKey: String, lastTxId: String): MutableList<CoinTransaction> {
        val params = SigNetParams.get()
        var key = DumpedPrivateKey.fromBase58(params.network(), pKey).key
        var address = key.toAddress(ScriptType.P2WPKH, params.network()).toString()
        var result = obtainResponse{ restApi.jsonApi.getTransactionsFromTxId(address, lastTxId) }
        return result.map { CoinTransaction.fromTransactionResponse(it, address) }.toMutableList()
    }



    suspend fun getTxInfo(txId: String): CoinTransaction {
        val params = SigNetParams.get()
        var key = DumpedPrivateKey.fromBase58(params.network(), BuildConfig.KEY).key
        var address = key.toAddress(ScriptType.P2WPKH, params.network()).toString()

        var result = CoinTransaction.fromTransactionResponse(
            obtainResponse { restApi.jsonApi.getTxInfo(txId) },
            address
        )
        return result
    }

    suspend fun getBalanceByKey(pKey: String): MutableList<UtxoResponse> {
        val params = SigNetParams.get()
        var key = DumpedPrivateKey.fromBase58(params.network(), pKey).key
        var address = key.toAddress(ScriptType.P2WPKH, params.network())
        return getUtxoList(address.toString())
    }

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun sendCoins(addressToSend: String, amountToSend: Long): String {

        val params = SigNetParams.get()

        var privateKey = DumpedPrivateKey.fromBase58(params.network(), "cNW98n9gD35fDn99SNYzMx6WWAAxmx9a9ntx5wFQyCjCjsJV7oUq").key
        var address = privateKey.toAddress(ScriptType.P2WPKH, params.network())


        var outAddress = AddressParser.getDefault(params.network()).parseAddress(addressToSend)

        val tx = Transaction()

        val utxoList = getUtxoList(address.toString())

        val balance = utxoList.sumOf { it.value }
        val fee = 500L
        if (amountToSend + fee > balance) {
            throw IllegalArgumentException("Недостаточно баланса")
        }

        /*
                utxoList.forEachIndexed { index, it ->
                    val txid = Sha256Hash.wrap(it.txid)
                    val tOutPoint = TransactionOutPoint(it.vout, txid)
                    //val input = TransactionInput(tx, byteArrayOf(), tOutPoint, Coin.valueOf(it.value))

                    val scriptCode = ScriptBuilder.createP2PKHOutputScript(privateKey.pubKeyHash)

                    tx.addSignedInput(
                        tOutPoint,
                        scriptCode,
                        Coin.valueOf(it.value),
                        privateKey
                    )

        /*
                    val signature = tx.calculateWitnessSignature(
                        index,
                        privateKey,
                        scriptCode,
                        Coin.valueOf(it.value),
                        Transaction.SigHash.ALL,
                        false
                    )
                    tx.getInput(index.toLong()).withWitness(TransactionWitness.of(listOf(signature.encodeToBitcoin(), privateKey.pubKey)))
        */
                }
        */
        Log.e("gaf",tx.inputs.toString())
        Log.e("gaf",tx.outputs.toString())

        utxoList.first().let { it ->
            val txid = Sha256Hash.wrap(it.txid)
            val vout = it.vout

            val tOutPoint = TransactionOutPoint(vout, txid)
            val script = ScriptBuilder.createOutputScript(address)

            val change = it.value.minus(amountToSend).minus(fee)

            tx.addOutput(Coin.valueOf(amountToSend), outAddress)
            tx.addOutput(Coin.valueOf(change), address)

            tx.addSignedInput(
                tOutPoint,
                script,
                Coin.valueOf(it.value),
                privateKey
            )
            tx.hashForWitnessSignature(
                0,
                script,
                Coin.valueOf(it.value),
                Transaction.SigHash.ALL,
                false
            )
        }

        val hex = tx.serialize().toHexString()

        val type = okhttp3.MediaType.parse("text/plain")
        val requestBody = RequestBody.create(type, hex)
        restApi.textApi.sendTransaction(requestBody)


        // var outAdress = Address.fromString(SigNetParams.get(), "cNW98n9gD35fDn99SNYzMx6WWAAxmx9a9ntx5wFQyCjCjsJV7oUq")
        //val tOutput = tx.addOutput(Coin.valueOf(1000), outAdress)
        /*
                val tx = Transaction()

                val tInput = tx.addSignedInput(
                    tOutPoint,
                    script,
                    Coin.valueOf(1000),
                    privateKey
                )

                */


        /*
        val tx = Transaction()
        val outPoint = Sha256Hash.wrap(utxo.txid)
        val script = ScriptBuilder.createOutputScript(address)
        */
        Log.e("gaf", hex)
        Log.e("gaf", privateKey.toString())
        Log.e("gaf", privateKey.privKey.toString())
        Log.e("gaf", address.toString())


        return sendToMempool(hex)
    }

    private suspend fun sendToMempool(hex: String): String {
        val type = okhttp3.MediaType.parse("text/plain")
        val requestBody = RequestBody.create(type, hex)
        val result = obtainResponse{ restApi.textApi.sendTransaction(requestBody) }
        return result
    }
}