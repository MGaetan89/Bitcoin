package io.crypto.bitstamp.model

data class OrderBook(
		val asks: Array<Array<Float>>,
		val bids: Array<Array<Float>>,
		val timestamp: Long
) {
	companion object {
		val EMPTY = OrderBook(emptyArray(), emptyArray(), 0L)
	}
}
