package io.crypto.bitstamp.model

import io.crypto.bitstamp.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.TimeZone

class UserTransactionTest {
	@Before
	fun before() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
	}

	@Test
	fun typeObject_deposit() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "2018-01-04 19:25:13", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 0, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.accent)
		assertThat(transaction.date).isEqualTo(1515093913L)
		assertThat(transaction.typeLabel).isEqualTo(R.string.deposit)
		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.DEPOSIT)
	}

	@Test
	fun typeObject_marketTrade() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "2018-01-04 16:34:59", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 2, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.text)
		assertThat(transaction.date).isEqualTo(1515083699L)
		assertThat(transaction.typeLabel).isEqualTo(R.string.sell)
		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.MARKET_TRADE)
	}

	@Test
	fun typeObject_marketTrade_buy() {
		val transaction = UserTransaction(1000f, 0.15f, 0f, 0f, 0f, 0f, 0f, "2017-12-13 08:08:25", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 2, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.bid)
		assertThat(transaction.date).isEqualTo(1513152505L)
		assertThat(transaction.typeLabel).isEqualTo(R.string.buy)
		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.MARKET_TRADE)
	}

	@Test
	fun typeObject_marketTrade_sell() {
		val transaction = UserTransaction(-1000f, 0.15f, 0f, 0f, 0f, 0f, 0f, "2017-12-12 22:52:47", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 2, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.ask)
		assertThat(transaction.date).isEqualTo(1513119167L)
		assertThat(transaction.typeLabel).isEqualTo(R.string.sell)
		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.MARKET_TRADE)
	}

	@Test
	fun typeObject_subAccountTransfer() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "2017-12-12 20:23:38", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 14, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.text)
		assertThat(transaction.date).isEqualTo(1513110218L)
		assertThat(transaction.typeLabel).isEqualTo(R.string.sub_account_transfer)
		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.SUB_ACCOUNT_TRANSFER)
	}

	@Test
	fun typeObject_withdrawal() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "2017-10-23 04:14:20", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 1, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.text)
		assertThat(transaction.date).isEqualTo(1508732060L)
		assertThat(transaction.typeLabel).isEqualTo(R.string.withdrawal)
		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.WITHDRAWAL)
	}

	@Test
	fun typeObject_unknown() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "2017-10-17 11:00:16", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 3, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.text)
		assertThat(transaction.date).isEqualTo(1508238016L)
		assertThat(transaction.typeLabel).isEqualTo(0)
		assertThat(transaction.typeObject).isNull()
	}
}
