package io.crypto.bitstamp.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.MapEntry
import org.junit.Test

class AccountBalanceTest {
	@Test
	fun balance() {
		val balance = AccountBalance(
			1f, 2f, 3f, 4f, 5f, 6f, 7f,
			8f, 9f, 10f, 11f, 12f, 13f, 14f,
			15f, 16f, 17f, 18f, 19f, 20f, 21f,
			22f, 23f, 24f, 25f, 26f, 27f, 28f,
			29f, 30f, 31f, 32f, 33f, 34f, 35f,
			36f
		)

		assertThat(balance.bchAvailable).isEqualTo(1f)
		assertThat(balance.bchBalance).isEqualTo(2f)
		assertThat(balance.bchReserved).isEqualTo(3f)
		assertThat(balance.bchBtcFee).isEqualTo(4f)
		assertThat(balance.bchEurFee).isEqualTo(5f)
		assertThat(balance.bchUsdFee).isEqualTo(6f)
		assertThat(balance.btcAvailable).isEqualTo(7f)
		assertThat(balance.btcBalance).isEqualTo(8f)
		assertThat(balance.btcReserved).isEqualTo(9f)
		assertThat(balance.btcEurFee).isEqualTo(10f)
		assertThat(balance.btcUsdFee).isEqualTo(11f)
		assertThat(balance.ethAvailable).isEqualTo(12f)
		assertThat(balance.ethBalance).isEqualTo(13f)
		assertThat(balance.ethReserved).isEqualTo(14f)
		assertThat(balance.ethBtcFee).isEqualTo(15f)
		assertThat(balance.ethEurFee).isEqualTo(16f)
		assertThat(balance.ethUsdFee).isEqualTo(17f)
		assertThat(balance.eurAvailable).isEqualTo(18f)
		assertThat(balance.eurBalance).isEqualTo(19f)
		assertThat(balance.eurReserved).isEqualTo(20f)
		assertThat(balance.eurUsdFee).isEqualTo(21f)
		assertThat(balance.ltcAvailable).isEqualTo(22f)
		assertThat(balance.ltcBalance).isEqualTo(23f)
		assertThat(balance.ltcReserved).isEqualTo(24f)
		assertThat(balance.ltcBtcFee).isEqualTo(25f)
		assertThat(balance.ltcEurFee).isEqualTo(26f)
		assertThat(balance.ltcUsdFee).isEqualTo(27f)
		assertThat(balance.usdAvailable).isEqualTo(28f)
		assertThat(balance.usdBalance).isEqualTo(29f)
		assertThat(balance.usdReserved).isEqualTo(30f)
		assertThat(balance.xrpAvailable).isEqualTo(31f)
		assertThat(balance.xrpBalance).isEqualTo(32f)
		assertThat(balance.xrpReserved).isEqualTo(33f)
		assertThat(balance.xrpBtcFee).isEqualTo(34f)
		assertThat(balance.xrpEurFee).isEqualTo(35f)
		assertThat(balance.xrpUsdFee).isEqualTo(36f)

		assertThat(balance.available).containsOnly(
			MapEntry.entry("BCH", 1f),
			MapEntry.entry("BTC", 7f),
			MapEntry.entry("ETH", 12f),
			MapEntry.entry("EUR", 18f),
			MapEntry.entry("LTC", 22f),
			MapEntry.entry("USD", 28f),
			MapEntry.entry("XRP", 31f)
		)

		assertThat(balance.balance).containsOnly(
			MapEntry.entry("BCH", 2f),
			MapEntry.entry("BTC", 8f),
			MapEntry.entry("ETH", 13f),
			MapEntry.entry("EUR", 19f),
			MapEntry.entry("LTC", 23f),
			MapEntry.entry("USD", 29f),
			MapEntry.entry("XRP", 32f)
		)

		assertThat(balance.fees).containsOnly(
			MapEntry.entry("BCHBTC", 4f),
			MapEntry.entry("BCHEUR", 5f),
			MapEntry.entry("BCHUSD", 6f),
			MapEntry.entry("BTCEUR", 10f),
			MapEntry.entry("BTCUSD", 11f),
			MapEntry.entry("ETHBTC", 15f),
			MapEntry.entry("ETHEUR", 16f),
			MapEntry.entry("ETHUSD", 17f),
			MapEntry.entry("EURUSD", 21f),
			MapEntry.entry("LTCBTC", 25f),
			MapEntry.entry("LTCEUR", 26f),
			MapEntry.entry("LTCUSD", 27f),
			MapEntry.entry("XRPBTC", 34f),
			MapEntry.entry("XRPEUR", 35f),
			MapEntry.entry("XRPUSD", 36f)
		)

		assertThat(balance.reserved).containsOnly(
			MapEntry.entry("BCH", 3f),
			MapEntry.entry("BTC", 9f),
			MapEntry.entry("ETH", 14f),
			MapEntry.entry("EUR", 20f),
			MapEntry.entry("LTC", 24f),
			MapEntry.entry("USD", 30f),
			MapEntry.entry("XRP", 33f)
		)
	}
}
