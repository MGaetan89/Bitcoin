package io.crypto.bitstamp.network

import kotlinx.coroutines.experimental.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class BitstampServicesTest {
	@Test
	fun getTradingPairs() = runBlocking<Unit> {
		val tradingPairs = BitstampServices.getTradingPairs()

		assertThat(tradingPairs.size).isGreaterThan(0)
	}
}
