package io.crypto.bitstamp.model

data class CanceledOrder(
	val amount: Float,
	val id: Long,
	val price: Float,
	val type: Int
) {
	companion object {
		val EMPTY = CanceledOrder(0f, 0L, 0f, 0)
	}
}
