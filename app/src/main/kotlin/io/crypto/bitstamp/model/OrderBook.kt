package io.crypto.bitstamp.model

data class OrderBook(
	val asks: List<List<Float>>,
	val bids: List<List<Float>>
)
