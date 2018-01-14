package io.crypto.bitstamp.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class OrderBookTest {
	@Test
	fun empty() {
		val empty = OrderBook.EMPTY

		assertThat(empty.asks).isEmpty()
		assertThat(empty.bids).isEmpty()
		assertThat(empty.timestamp).isEqualTo(0L)
	}
}
