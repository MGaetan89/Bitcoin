package io.bitcoin.extension

import io.bitcoin.model.Prices
import io.bitcoin.model.TradingPair
import org.json.JSONArray
import org.json.JSONObject

fun String.toPrices(): Prices {
	val json = JSONObject(this)
	val ask = json.getJSONArrayOrNull("asks").firstJSONArrayOrNull()?.getDouble(0)
	val bid = json.getJSONArrayOrNull("bids").firstJSONArrayOrNull()?.getDouble(0)
	return Prices(ask, bid)
}

fun String.toTradingPair(channelName: String, tradingPairs: List<TradingPair>): TradingPair? {
	val urlSymbol = this.replaceFirst(channelName, "").trimStart('_').takeIf { it.isNotEmpty() } ?: "btcusd"

	return tradingPairs.firstOrNull { it.urlSymbol == urlSymbol }
}

private fun JSONArray?.firstJSONArrayOrNull() = if (this != null && this.length() > 0) this.getJSONArray(0) else null

private fun JSONObject.getJSONArrayOrNull(name: String) = if (this.has(name)) this.getJSONArray(name) else null
