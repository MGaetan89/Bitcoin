package io.crypto.bitstamp.model

import com.squareup.moshi.Json

data class Ticker(
		val ask: Float,
		val bid: Float,
		val high: Float,
		val last: Float,
		val low: Float,
		val open: Float,
		val timestamp: Long,
		val volume: Float,
		@Json(name = "vwap") val volumeWeightedAveragePrice: Float
) {
	companion object {
		val EMPTY = Ticker(0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f)
	}

	fun isNewerThan(ticker: Ticker): Boolean {
		return ticker.timestamp < this.timestamp && (ticker.ask != this.ask || ticker.bid != this.bid)
	}
}
