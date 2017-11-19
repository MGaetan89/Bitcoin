package io.bitcoin.extension

import io.bitcoin.model.CurrencyPair
import io.bitcoin.model.Prices
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StringExtensionTest {
	@Test
	fun toCurrencyPair() {
		val channel = "order_book"
		assertThat("".toCurrencyPair()).isEqualTo(CurrencyPair("BTC", "USD"))
		assertThat("btc".toCurrencyPair()).isEqualTo(CurrencyPair("BTC", ""))
		assertThat("btceur".toCurrencyPair()).isEqualTo(CurrencyPair("BTC", "EUR"))
		assertThat("order_book".toCurrencyPair()).isEqualTo(CurrencyPair("ORD", "ER_"))
		assertThat("order_book_".toCurrencyPair()).isEqualTo(CurrencyPair("ORD", "ER_"))
		assertThat("order_book_btc".toCurrencyPair()).isEqualTo(CurrencyPair("ORD", "ER_"))
		assertThat("order_book_btceur".toCurrencyPair()).isEqualTo(CurrencyPair("ORD", "ER_"))
		assertThat("live_trades".toCurrencyPair()).isEqualTo(CurrencyPair("LIV", "E_T"))
		assertThat("live_trades_".toCurrencyPair()).isEqualTo(CurrencyPair("LIV", "E_T"))
		assertThat("live_trades_btc".toCurrencyPair()).isEqualTo(CurrencyPair("LIV", "E_T"))
		assertThat("live_trades_btceur".toCurrencyPair()).isEqualTo(CurrencyPair("LIV", "E_T"))
		assertThat("".toCurrencyPair(channel)).isEqualTo(CurrencyPair("BTC", "USD"))
		assertThat("btc".toCurrencyPair(channel)).isEqualTo(CurrencyPair("BTC", ""))
		assertThat("btceur".toCurrencyPair(channel)).isEqualTo(CurrencyPair("BTC", "EUR"))
		assertThat("order_book".toCurrencyPair(channel)).isEqualTo(CurrencyPair("BTC", "USD"))
		assertThat("order_book_".toCurrencyPair(channel)).isEqualTo(CurrencyPair("BTC", "USD"))
		assertThat("order_book_btc".toCurrencyPair(channel)).isEqualTo(CurrencyPair("BTC", ""))
		assertThat("order_book_btceur".toCurrencyPair(channel)).isEqualTo(CurrencyPair("BTC", "EUR"))
		assertThat("live_trades".toCurrencyPair(channel)).isEqualTo(CurrencyPair("LIV", "E_T"))
		assertThat("live_trades_".toCurrencyPair(channel)).isEqualTo(CurrencyPair("LIV", "E_T"))
		assertThat("live_trades_btc".toCurrencyPair(channel)).isEqualTo(CurrencyPair("LIV", "E_T"))
		assertThat("live_trades_btceur".toCurrencyPair(channel)).isEqualTo(CurrencyPair("LIV", "E_T"))
	}

	@Test
	fun toPrices() {
		assertThat("{}".toPrices()).isEqualTo(Prices(null, null))
		assertThat("{asks: [], bids: []}".toPrices()).isEqualTo(Prices(null, null))
		assertThat("{asks: [[1234.56, 0]], bids: [[7890.12, 1]]}".toPrices()).isEqualTo(Prices(1234.56, 7890.12))
		assertThat("{asks: [[1234.56, 0], [3456.78, 2]], bids: [[7890.12, 1], [9012.34, 3]]}".toPrices()).isEqualTo(Prices(1234.56, 7890.12))
	}
}
