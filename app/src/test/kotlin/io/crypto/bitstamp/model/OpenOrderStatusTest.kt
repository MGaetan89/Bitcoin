package io.crypto.bitstamp.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class OpenOrderStatusTest {
	@Test
	fun empty() {
		assertThat(OpenOrderStatus.EMPTY.status).isNull()
	}
}
