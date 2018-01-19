package io.crypto.bitstamp.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CanceledOrderTest {
	@Test
	fun empty() {
		val order = CanceledOrder.EMPTY

		assertThat(order.amount).isEqualTo(0f)
		assertThat(order.id).isEqualTo(0L)
		assertThat(order.price).isEqualTo(0f)
		assertThat(order.type).isEqualTo(0)
	}
}
