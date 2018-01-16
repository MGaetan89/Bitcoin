package io.crypto.bitstamp.model

import io.crypto.bitstamp.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UserTransactionTest {
	@Test
	fun type_deposit() {
		val type = UserTransaction.Type.DEPOSIT

		assertThat(type.textRes).isEqualTo(R.string.deposit)
		assertThat(type.typeId).isEqualTo(0)
	}

	@Test
	fun type_marketTrade() {
		val type = UserTransaction.Type.MARKET_TRADE

		assertThat(type.textRes).isEqualTo(R.string.market_trade)
		assertThat(type.typeId).isEqualTo(2)
	}

	@Test
	fun type_subAccountTransfer() {
		val type = UserTransaction.Type.SUB_ACCOUNT_TRANSFER

		assertThat(type.textRes).isEqualTo(R.string.sub_account_transfer)
		assertThat(type.typeId).isEqualTo(14)
	}

	@Test
	fun type_withdrawal() {
		val type = UserTransaction.Type.WITHDRAWAL

		assertThat(type.textRes).isEqualTo(R.string.withdrawal)
		assertThat(type.typeId).isEqualTo(1)
	}

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
