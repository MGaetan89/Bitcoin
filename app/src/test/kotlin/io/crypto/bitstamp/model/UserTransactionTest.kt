package io.crypto.bitstamp.model

import io.crypto.bitstamp.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UserTransactionTest {
	@Test
	fun typeObject_deposit() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 0, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.accent)
		assertThat(transaction.typeLabel).isEqualTo(R.string.deposit)
		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.DEPOSIT)
	}

	@Test
	fun typeObject_marketTrade() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 2, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.text)
		assertThat(transaction.typeLabel).isEqualTo(R.string.sell)
		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.MARKET_TRADE)
	}

	@Test
	fun typeObject_marketTrade_buy() {
		val transaction = UserTransaction(1000f, 0.15f, 0f, 0f, 0f, 0f, 0f, "", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 2, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.bid)
		assertThat(transaction.typeLabel).isEqualTo(R.string.buy)
		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.MARKET_TRADE)
	}

	@Test
	fun typeObject_marketTrade_sell() {
		val transaction = UserTransaction(-1000f, 0.15f, 0f, 0f, 0f, 0f, 0f, "", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 2, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.ask)
		assertThat(transaction.typeLabel).isEqualTo(R.string.sell)
		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.MARKET_TRADE)
	}

	@Test
	fun typeObject_subAccountTransfer() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 14, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.text)
		assertThat(transaction.typeLabel).isEqualTo(R.string.sub_account_transfer)
		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.SUB_ACCOUNT_TRANSFER)
	}

	@Test
	fun typeObject_withdrawal() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 1, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.text)
		assertThat(transaction.typeLabel).isEqualTo(R.string.withdrawal)
		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.WITHDRAWAL)
	}

	@Test
	fun typeObject_unknown() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 3, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.color).isEqualTo(R.color.text)
		assertThat(transaction.typeLabel).isEqualTo(0)
		assertThat(transaction.typeObject).isNull()
	}
}
