package io.crypto.bitstamp.model

data class OpenOrderStatus(
	val status: String?
) {
	companion object {
		val EMPTY = OpenOrderStatus(null)
	}
}
