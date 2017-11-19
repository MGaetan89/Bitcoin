package io.bitcoin.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CurrencyPairTest {
	@Test
	fun getDecimalsCount() {
		assertThat(CurrencyPair("", "").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("BTC", "").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("BTC", "EUR").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("BTC", "USD").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("BTC", "CHF").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("EUR", "").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("EUR", "USD").getDecimalsCount()).isEqualTo(5)
		assertThat(CurrencyPair("EUR", "CHF").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("ETH", "").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("ETH", "BTC").getDecimalsCount()).isEqualTo(8)
		assertThat(CurrencyPair("ETH", "EUR").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("ETH", "USD").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("ETH", "CHF").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("LTC", "").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("LTC", "BTC").getDecimalsCount()).isEqualTo(8)
		assertThat(CurrencyPair("LTC", "EUR").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("LTC", "USD").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("LTC", "CHF").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("USD", "").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("USD", "BTC").getDecimalsCount()).isEqualTo(8)
		assertThat(CurrencyPair("USD", "EUR").getDecimalsCount()).isEqualTo(5)
		assertThat(CurrencyPair("USD", "CHF").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("XRP", "").getDecimalsCount()).isEqualTo(2)
		assertThat(CurrencyPair("XRP", "BTC").getDecimalsCount()).isEqualTo(8)
		assertThat(CurrencyPair("XRP", "EUR").getDecimalsCount()).isEqualTo(5)
		assertThat(CurrencyPair("XRP", "USD").getDecimalsCount()).isEqualTo(5)
		assertThat(CurrencyPair("XRP", "CHF").getDecimalsCount()).isEqualTo(2)
	}

	@Test
	fun reciprocal() {
		assertThat(CurrencyPair("BTC", "").reciprocal()).isEqualTo(CurrencyPair("", "BTC"))
		assertThat(CurrencyPair("BTC", "EUR").reciprocal()).isEqualTo(CurrencyPair("EUR", "BTC"))
	}

	@Test
	fun testToString() {
		assertThat(CurrencyPair("", "").toString()).isEqualTo(" / ")
		assertThat(CurrencyPair("btc", "").toString()).isEqualTo("btc / ")
		assertThat(CurrencyPair("BTC", "").toString()).isEqualTo("BTC / ")
		assertThat(CurrencyPair("", "usd").toString()).isEqualTo(" / usd")
		assertThat(CurrencyPair("", "USD").toString()).isEqualTo(" / USD")
		assertThat(CurrencyPair("btc", "usd").toString()).isEqualTo("btc / usd")
		assertThat(CurrencyPair("BTC", "USD").toString()).isEqualTo("BTC / USD")
	}

	@Test
	fun toTag() {
		assertThat(CurrencyPair("", "").toTag()).isEqualTo("")
		assertThat(CurrencyPair("BTC", "EUR").toTag()).isEqualTo("btceur")
		assertThat(CurrencyPair("BTC", "USD").toTag()).isEqualTo("")
		assertThat(CurrencyPair("EUR", "USD").toTag()).isEqualTo("eurusd")
		assertThat(CurrencyPair("ETH", "BTC").toTag()).isEqualTo("ethbtc")
		assertThat(CurrencyPair("ETH", "EUR").toTag()).isEqualTo("etheur")
		assertThat(CurrencyPair("ETH", "USD").toTag()).isEqualTo("ethusd")
		assertThat(CurrencyPair("LTC", "BTC").toTag()).isEqualTo("ltcbtc")
		assertThat(CurrencyPair("LTC", "EUR").toTag()).isEqualTo("ltceur")
		assertThat(CurrencyPair("LTC", "USD").toTag()).isEqualTo("ltcusd")
		assertThat(CurrencyPair("XRP", "BTC").toTag()).isEqualTo("xrpbtc")
		assertThat(CurrencyPair("XRP", "EUR").toTag()).isEqualTo("xrpeur")
		assertThat(CurrencyPair("XRP", "USD").toTag()).isEqualTo("xrpusd")
	}
}
