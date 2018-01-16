package io.crypto.bitstamp.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UserTransactionTest {
	@Test
	fun typeObject_deposit() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 0, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.DEPOSIT)
	}

	@Test
	fun typeObject_marketTrade() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 2, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.MARKET_TRADE)
	}

	@Test
	fun typeObject_subAccountTransfer() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 14, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.SUB_ACCOUNT_TRANSFER)
	}

	@Test
	fun typeObject_withdrawal() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 1, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.typeObject).isEqualTo(UserTransaction.Type.WITHDRAWAL)
	}

	@Test
	fun typeObject_unknown() {
		val transaction = UserTransaction(0f, 0f, 0f, 0f, 0f, 0f, 0f, "", 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L, 0f, 0f, 0f, 0f, 0L, 3, 0f, 0f, 0f, 0f, 0f)

		assertThat(transaction.typeObject).isNull()
	}
}
