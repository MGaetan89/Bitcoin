package io.crypto.bitstamp.model

import io.crypto.bitstamp.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PriceTransactionTest {
	@Test
	fun type_buy() {
		val type = PriceTransaction.Type.BUY

		assertThat(type.colorRes).isEqualTo(R.color.bid)
		assertThat(type.textRes).isEqualTo(R.string.buy)
	}

	@Test
	fun type_sell() {
		val type = PriceTransaction.Type.SELL

		assertThat(type.colorRes).isEqualTo(R.color.ask)
		assertThat(type.textRes).isEqualTo(R.string.sell)
	}

	@Test
	fun typeObject_buy() {
		val transaction = PriceTransaction(0.00659689f, 1515853453L, 14423.98f, 45359346L, 0)

		assertThat(transaction.typeObject).isEqualTo(PriceTransaction.Type.BUY)
	}

	@Test
	fun typeObject_sell() {
		val transaction = PriceTransaction(0.16502357f, 1515853404L, 14423.45f, 45359222L, 1)

		assertThat(transaction.typeObject).isEqualTo(PriceTransaction.Type.SELL)
	}

	@Test
	fun typeObject_unknown() {
		val transaction = PriceTransaction(0.00569300f, 1515853451L, 14417.94f, 45359339L, 2)

		assertThat(transaction.typeObject).isNull()
	}
}
