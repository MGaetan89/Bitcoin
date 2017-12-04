package io.bitcoin.model

import com.squareup.moshi.Json
import java.text.NumberFormat

data class TradingPair(
		@Json(name = "base_decimals") val baseDecimals: Int,
		@Json(name = "counter_decimals") val counterDecimals: Int,
		val description: String,
		@Json(name = "minimum_order") val minimumOrder: String,
		val name: String,
		val trading: String,
		@Json(name = "url_symbol") val urlSymbol: String
) {
	fun getCounterNumberFormat(): NumberFormat {
		return NumberFormat.getNumberInstance().also {
			it.maximumFractionDigits = this.counterDecimals
			it.minimumFractionDigits = this.counterDecimals
		}
	}

	fun getNumberFormat(): NumberFormat {
		return NumberFormat.getNumberInstance().also {
			it.maximumFractionDigits = this.baseDecimals
			it.minimumFractionDigits = this.baseDecimals
		}
	}

	override fun toString() = this.description

	fun toUrlSymbol() = this.urlSymbol.takeIf { it != "btcusd" }.orEmpty()
}
