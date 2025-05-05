package ru.flx.hodlhomework.repositories.rest_api

data class UtxoResponse(
    val txid: String,
    val vout: Long,
    val status: UtxoStatus,
    val value: Long
)

data class UtxoStatus(
    val confirmed: Boolean,
    val block_height: Long,
    val block_hash: String,
    val block_time: Long
)

data class TransactionResponse(
    val txid: String,
    val version: Int,
    val locktime: Int,
    val vin: List<Vin>,
    val vout: List<Vout>,
    val size: Int,
    val weight: Int,
    val sigops: Int,
    val fee: Int,
    val status: UtxoStatus
)

data class Vin(
    val txid: String,
    val vout: Int,
    val prevout: Prevout,
    val scriptsig: String,
    val scriptsig_asm: String,
    val witness: List<String>,
    val is_coinbase: Boolean,
    val sequence: Long
)

data class Prevout(
    val scriptpubkey: String,
    val scriptpubkey_asm: String,
    val scriptpubkey_type: String,
    val scriptpubkey_address: String,
    val value: Long
)

data class Vout(
    val scriptpubkey: String,
    val scriptpubkey_asm: String,
    val scriptpubkey_type: String,
    val scriptpubkey_address: String,
    val value: Long
)
