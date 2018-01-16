package io.crypto.bitstamp.network

import kotlinx.coroutines.experimental.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class BitstampServicesTest {
	@Test
	fun getOrderBook() = runBlocking<Unit> {
		val orderBook = BitstampServices.getOrderBook("btcusd")

		assertThat(orderBook.asks).isNotEmpty()
		assertThat(orderBook.bids).isNotEmpty()
		assertThat(orderBook.timestamp).isGreaterThan(0L)
	}

	@Test
	fun getTicker() = runBlocking<Unit> {
		val ticker = BitstampServices.getTicker("xrpeur")

		assertThat(ticker.ask).isGreaterThan(0f)
		assertThat(ticker.bid).isGreaterThan(0f)
		assertThat(ticker.high).isGreaterThan(0f)
		assertThat(ticker.last).isGreaterThan(0f)
		assertThat(ticker.low).isGreaterThan(0f)
		assertThat(ticker.open).isGreaterThan(0f)
		assertThat(ticker.timestamp).isGreaterThan(0L)
		assertThat(ticker.volume).isGreaterThan(0f)
		assertThat(ticker.volumeWeightedAveragePrice).isGreaterThan(0f)
	}

	@Test
	fun getTradingPairs() = runBlocking<Unit> {
		val tradingPairs = BitstampServices.getTradingPairs()

		assertThat(tradingPairs.size).isGreaterThan(0)
	}

	@Test
	fun getTransactions() = runBlocking<Unit> {
		val transactions = BitstampServices.getTransactions("ltcbtc")

		assertThat(transactions.size).isGreaterThan(0)
	}
}
