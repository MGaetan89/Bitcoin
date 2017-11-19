package io.bitcoin.extension

import io.bitcoin.model.CurrencyPair
import io.bitcoin.model.Prices
import org.json.JSONArray
import org.json.JSONObject

fun String.toCurrencyPair(channel: String = ""): CurrencyPair {
	val currencyPair = this.replaceFirst(channel, "").trimStart('_')
	return if (currencyPair.isEmpty()) {
		CurrencyPair("BTC", "USD")
	} else {
		val length = currencyPair.length
		val left = currencyPair.substring(0, minOf(3, length))
		val right = currencyPair.substring(minOf(3, length), minOf(6, length))
		CurrencyPair(left.toUpperCase(), right.toUpperCase())
	}
}

fun String.toPrices(): Prices {
	val json = JSONObject(this)
	val ask = json.getJSONArrayOrNull("asks").firstJSONArrayOrNull()?.getDouble(0)
	val bid = json.getJSONArrayOrNull("bids").firstJSONArrayOrNull()?.getDouble(0)
	return Prices(ask, bid)
}

private fun JSONArray?.firstJSONArrayOrNull() = if (this != null && this.length() > 0) this.getJSONArray(0) else null

private fun JSONObject.getJSONArrayOrNull(name: String) = if (this.has(name)) this.getJSONArray(name) else null
