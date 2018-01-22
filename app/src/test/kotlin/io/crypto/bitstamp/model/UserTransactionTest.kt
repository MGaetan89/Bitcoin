package io.crypto.bitstamp.model

import com.squareup.moshi.Moshi
import io.crypto.bitstamp.R
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Before
import org.junit.Test
import java.util.TimeZone

class UserTransactionTest {
	private class ReducedTransaction(
		val baseCurrency: String,
		val counterCurrency: String,
		val amount: Float,
		val value: Float,
		val fee: Float
	)

	@Before
	fun before() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
	}

	@Test
	fun transactionInfo() {
		val moshi = Moshi.Builder().build().adapter(UserTransaction::class.java)
		val data = listOf(
			ReducedTransaction("bch", "btc", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("bch", "eur", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("bch", "usd", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("btc", "eur", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("btc", "usd", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("eth", "btc", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("eth", "eur", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("eth", "usd", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("eur", "usd", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("ltc", "btc", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("ltc", "eur", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("ltc", "usd", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("xrp", "btc", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("xrp", "eur", 10.575f, 1057.5f, 1.23f),
			ReducedTransaction("xrp", "usd", 10.575f, 1057.5f, 1.23f)
		)

		data.forEach {
			val json = """{
				|"${it.baseCurrency}": ${it.amount},
				|"${it.counterCurrency}": ${it.value},
				|"${it.baseCurrency}_${it.counterCurrency}": ${it.value / it.amount},
				|"fee": ${it.fee},
				|"datetime": "",
				|"id": 0,
				|"orderId": 0,
				|"type": 0
				|}""".trimMargin()
			val transactionInfo = moshi.fromJson(json).transactionInfo
					?: return fail("Unable to create UserTransaction")

			assertThat(transactionInfo.amount).isEqualTo(it.amount)
			assertThat(transactionInfo.fee).isEqualTo(it.fee)
			assertThat(transactionInfo.price).isEqualTo(it.value / it.amount)
			assertThat(transactionInfo.urlSymbol).isEqualTo(it.baseCurrency + it.counterCurrency)
			assertThat(transactionInfo.value).isEqualTo(it.value)
		}
	}

	@Test
	fun transactionInfo_unknown() {
		val moshi = Moshi.Builder().build().adapter(UserTransaction::class.java)
		val transaction = ReducedTransaction("zzz", "zzz", 10.575f, 1057.5f, 1.23f)
		val json = """{
				|"${transaction.baseCurrency}": ${transaction.amount},
				|"${transaction.counterCurrency}": ${transaction.value},
				|"${transaction.baseCurrency}_${transaction.counterCurrency}": ${transaction.value / transaction.amount},
				|"fee": ${transaction.fee},
				|"datetime": "",
				|"id": 0,
				|"orderId": 0,
				|"type": 0
				|}""".trimMargin()

		assertThat(moshi.fromJson(json).transactionInfo).isNull()
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
