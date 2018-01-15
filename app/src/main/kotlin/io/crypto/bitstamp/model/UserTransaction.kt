package io.crypto.bitstamp.model

import android.support.annotation.StringRes
import com.squareup.moshi.Json
import io.crypto.bitstamp.R

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
		val eur: Float,
		@Json(name = "eur_usd") val eurUsd: Float?,
		val fee: Float,
		val id: Long,
		val ltc: Float?,
		@Json(name = "ltc_btc") val ltcBtc: Float?,
		@Json(name = "ltc_eur") val ltcEur: Float?,
		@Json(name = "ltc_usd") val ltcUsd: Float?,
		@Json(name = "order_id") val orderId: Long,
		val type: Int,
		val usd: Float,
		val xrp: Float?,
		@Json(name = "xrp_btc") val xrpBtc: Float?,
		@Json(name = "xrp_eur") val xrpEur: Float?,
		@Json(name = "xrp_usd") val xrpUsd: Float?
) {
	enum class Type(@StringRes val textRes: Int) {
		DEPOSIT(R.string.deposit),
		WITHDRAWAL(R.string.withdrawal),
		MARKET_TRADE(R.string.market_trade),
		SUB_ACCOUNT_TRANSFER(R.string.sub_account_transfer)
	}

	val typeObject: Type?
		get() = when (this.type) {
			0 -> Type.DEPOSIT
			1 -> Type.WITHDRAWAL
			2 -> Type.MARKET_TRADE
			14 -> Type.SUB_ACCOUNT_TRANSFER
			else -> null
		}
}
