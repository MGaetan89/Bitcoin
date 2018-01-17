package io.crypto.bitstamp.model

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import com.squareup.moshi.Json
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.toTimestamp
import io.crypto.bitstamp.extension.toVariationColor

data class UserTransaction(
		val bch: Float?,
		@Json(name = "bch_btc") val bchBtc: Float?,
		@Json(name = "bch_eur") val bchEur: Float?,
		@Json(name = "bch_usd") val bchUsd: Float?,
		val btc: Float?,
		@Json(name = "btc_eur") val btcEur: Float?,
		@Json(name = "btc_usd") val btcUsd: Float?,
		val datetime: String,
		val eth: Float?,
		@Json(name = "eth_btc") val ethBtc: Float?,
		@Json(name = "eth_eur") val ethEur: Float?,
		@Json(name = "eth_usd") val ethUsd: Float?,
		val eur: Float?,
		@Json(name = "eur_usd") val eurUsd: Float?,
		val fee: Float,
		val id: Long,
		val ltc: Float?,
		@Json(name = "ltc_btc") val ltcBtc: Float?,
		@Json(name = "ltc_eur") val ltcEur: Float?,
		@Json(name = "ltc_usd") val ltcUsd: Float?,
		@Json(name = "order_id") val orderId: Long,
		val type: Int,
		val usd: Float?,
		val xrp: Float?,
		@Json(name = "xrp_btc") val xrpBtc: Float?,
		@Json(name = "xrp_eur") val xrpEur: Float?,
		@Json(name = "xrp_usd") val xrpUsd: Float?
) {
	enum class Type {
		DEPOSIT,
		MARKET_TRADE,
		SUB_ACCOUNT_TRANSFER,
		WITHDRAWAL
	}

	@get:ColorRes
	val color: Int
		get() {
			return when (this.typeObject) {
				Type.DEPOSIT -> R.color.accent
				Type.MARKET_TRADE -> transactionInfo?.amount?.toVariationColor() ?: R.color.text
				else -> R.color.text
			}
		}

	val date get() = this.datetime.toTimestamp()

	val transactionInfo: TransactionInfo?
		get() = this.bchBtc?.let { TransactionInfo(this.bch!!, it, this.fee, this.btc!!, "bchbtc") }
				?: this.bchEur?.let { TransactionInfo(this.bch!!, it, this.fee, this.eur!!, "bcheur") }
				?: this.bchUsd?.let { TransactionInfo(this.bch!!, it, this.fee, this.usd!!, "bchusd") }
				?: this.btcEur?.let { TransactionInfo(this.btc!!, it, this.fee, this.eur!!, "btceur") }
				?: this.btcUsd?.let { TransactionInfo(this.btc!!, it, this.fee, this.usd!!, "btcusd") }
				?: this.ethBtc?.let { TransactionInfo(this.eth!!, it, this.fee, this.btc!!, "ethbtc") }
				?: this.ethEur?.let { TransactionInfo(this.eth!!, it, this.fee, this.eur!!, "etheur") }
				?: this.ethUsd?.let { TransactionInfo(this.eth!!, it, this.fee, this.usd!!, "ethusd") }
				?: this.eurUsd?.let { TransactionInfo(this.eur!!, it, this.fee, this.usd!!, "eurusd") }
				?: this.ltcBtc?.let { TransactionInfo(this.ltc!!, it, this.fee, this.btc!!, "ltcbtc") }
				?: this.ltcEur?.let { TransactionInfo(this.ltc!!, it, this.fee, this.eur!!, "ltceur") }
				?: this.ltcUsd?.let { TransactionInfo(this.ltc!!, it, this.fee, this.usd!!, "ltcusd") }
				?: this.xrpBtc?.let { TransactionInfo(this.xrp!!, it, this.fee, this.btc!!, "xrpbtc") }
				?: this.xrpEur?.let { TransactionInfo(this.xrp!!, it, this.fee, this.eur!!, "xrpeur") }
				?: this.xrpUsd?.let { TransactionInfo(this.xrp!!, it, this.fee, this.usd!!, "xrpusd") }

	@get:StringRes
	val typeLabel: Int
		get() {
			return when (this.typeObject) {
				Type.DEPOSIT -> R.string.deposit
				Type.MARKET_TRADE -> if (this.transactionInfo?.amount ?: 0f > 0f) R.string.buy else R.string.sell
				Type.SUB_ACCOUNT_TRANSFER -> R.string.sub_account_transfer
				Type.WITHDRAWAL -> R.string.withdrawal
				else -> 0
			}
		}

	val typeObject: Type?
		get() = when (this.type) {
			0 -> Type.DEPOSIT
			1 -> Type.WITHDRAWAL
			2 -> Type.MARKET_TRADE
			14 -> Type.SUB_ACCOUNT_TRANSFER
			else -> null
		}

	class TransactionInfo(val amount: Float, val price: Float, val fee: Float, val value: Float, val urlSymbol: String)
}
