package io.crypto.bitstamp.model

data class PriceOrderBook(
		val asks: Array<Array<Float>>,
		val bids: Array<Array<Float>>,
		val timestamp: Long
) {
	companion object {
		val EMPTY = PriceOrderBook(emptyArray(), emptyArray(), 0L)
	}
}
