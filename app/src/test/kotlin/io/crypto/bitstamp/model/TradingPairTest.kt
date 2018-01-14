package io.crypto.bitstamp.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TradingPairTest {
	@Test
	fun currency() {
		val tradingPair = TradingPair(8, 2, "Litecoin / U.S. dollar", "5.0 USD", "LTC/USD", TradingPair.Trading.Enabled, "ltcusd")

		assertThat(tradingPair.baseCurrency).isEqualTo("LTC")
		assertThat(tradingPair.counterCurrency).isEqualTo("USD")
	}

	@Test
	fun currency_emptyName() {
		val tradingPair = TradingPair(8, 2, "Ether / U.S. dollar", "5.0 USD", "", TradingPair.Trading.Enabled, "ethusd")

		assertThat(tradingPair.baseCurrency).isEmpty()
		assertThat(tradingPair.counterCurrency).isEmpty()
	}
}
