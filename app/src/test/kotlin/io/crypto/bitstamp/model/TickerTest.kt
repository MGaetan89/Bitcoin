package io.crypto.bitstamp.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TickerTest {
	@Test
	fun empty() {
		val empty = Ticker.EMPTY

		assertThat(empty.ask).isEqualTo(0f)
		assertThat(empty.bid).isEqualTo(0f)
		assertThat(empty.high).isEqualTo(0f)
		assertThat(empty.last).isEqualTo(0f)
		assertThat(empty.low).isEqualTo(0f)
		assertThat(empty.open).isEqualTo(0f)
		assertThat(empty.timestamp).isEqualTo(0L)
		assertThat(empty.volume).isEqualTo(0f)
		assertThat(empty.volumeWeightedAveragePrice).isEqualTo(0f)
	}

	@Test
	fun isNewerThan() {
		val ticker1 = Ticker(14237.82f, 14221.87f, 14619.10f, 14237.82f, 13480.22f, 13829.28f, 1515864042L, 8709.87603340f, 14080.23f)
		val ticker2 = Ticker(14270.84f, 14225.81f, 14619.10f, 14270.84f, 13480.22f, 13829.28f, 1515864126L, 8713.67625161f, 14080.44f)

		assertThat(ticker1.isNewerThan(ticker2)).isFalse()
		assertThat(ticker2.isNewerThan(ticker1)).isTrue()
	}

	@Test
	fun isNewerThan_sameAsk() {
		val ticker1 = Ticker(14237.82f, 14221.87f, 14619.10f, 14237.82f, 13480.22f, 13829.28f, 1515864042L, 8709.87603340f, 14080.23f)
		val ticker2 = Ticker(14237.82f, 14225.81f, 14619.10f, 14270.84f, 13480.22f, 13829.28f, 1515864126L, 8713.67625161f, 14080.44f)

		assertThat(ticker1.isNewerThan(ticker2)).isFalse()
		assertThat(ticker2.isNewerThan(ticker1)).isTrue()
	}

	@Test
	fun isNewerThan_sameAskBid() {
		val ticker1 = Ticker(14237.82f, 14221.87f, 14619.10f, 14237.82f, 13480.22f, 13829.28f, 1515864042L, 8709.87603340f, 14080.23f)
		val ticker2 = Ticker(14237.82f, 14221.87f, 14619.10f, 14270.84f, 13480.22f, 13829.28f, 1515864126L, 8713.67625161f, 14080.44f)

		assertThat(ticker1.isNewerThan(ticker2)).isFalse()
		assertThat(ticker2.isNewerThan(ticker1)).isFalse()
	}

	@Test
	fun isNewerThan_sameBid() {
		val ticker1 = Ticker(14237.82f, 14221.87f, 14619.10f, 14237.82f, 13480.22f, 13829.28f, 1515864042L, 8709.87603340f, 14080.23f)
		val ticker2 = Ticker(14270.84f, 14221.87f, 14619.10f, 14270.84f, 13480.22f, 13829.28f, 1515864126L, 8713.67625161f, 14080.44f)

		assertThat(ticker1.isNewerThan(ticker2)).isFalse()
		assertThat(ticker2.isNewerThan(ticker1)).isTrue()
	}

	@Test
	fun isNewerThan_sameTime() {
		val ticker1 = Ticker(14237.82f, 14221.87f, 14619.10f, 14237.82f, 13480.22f, 13829.28f, 1515864042L, 8709.87603340f, 14080.23f)
		val ticker2 = Ticker(14270.84f, 14225.81f, 14619.10f, 14270.84f, 13480.22f, 13829.28f, 1515864042L, 8713.67625161f, 14080.44f)

		assertThat(ticker1.isNewerThan(ticker2)).isFalse()
		assertThat(ticker2.isNewerThan(ticker1)).isFalse()
	}
}
