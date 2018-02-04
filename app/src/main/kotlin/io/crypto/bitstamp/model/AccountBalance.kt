package io.crypto.bitstamp.model

import com.squareup.moshi.Json

data class AccountBalance(
	@Json(name = "bch_available") val bchAvailable: Float,
	@Json(name = "bch_balance") val bchBalance: Float,
	@Json(name = "bch_reserved") val bchReserved: Float,
	@Json(name = "bchbtc_fee") val bchBtcFee: Float,
	@Json(name = "bcheur_fee") val bchEurFee: Float,
	@Json(name = "bchusd_fee") val bchUsdFee: Float,
	@Json(name = "btc_available") val btcAvailable: Float,
	@Json(name = "btc_balance") val btcBalance: Float,
	@Json(name = "btc_reserved") val btcReserved: Float,
	@Json(name = "btceur_fee") val btcEurFee: Float,
	@Json(name = "btcusd_fee") val btcUsdFee: Float,
	@Json(name = "eth_available") val ethAvailable: Float,
	@Json(name = "eth_balance") val ethBalance: Float,
	@Json(name = "eth_reserved") val ethReserved: Float,
	@Json(name = "ethbtc_fee") val ethBtcFee: Float,
	@Json(name = "etheur_fee") val ethEurFee: Float,
	@Json(name = "ethusd_fee") val ethUsdFee: Float,
	@Json(name = "eur_available") val eurAvailable: Float,
	@Json(name = "eur_balance") val eurBalance: Float,
	@Json(name = "eur_reserved") val eurReserved: Float,
	@Json(name = "eurusd_fee") val eurUsdFee: Float,
	@Json(name = "ltc_available") val ltcAvailable: Float,
	@Json(name = "ltc_balance") val ltcBalance: Float,
	@Json(name = "ltc_reserved") val ltcReserved: Float,
	@Json(name = "ltcbtc_fee") val ltcBtcFee: Float,
	@Json(name = "ltceur_fee") val ltcEurFee: Float,
	@Json(name = "ltcusd_fee") val ltcUsdFee: Float,
	@Json(name = "usd_available") val usdAvailable: Float,
	@Json(name = "usd_balance") val usdBalance: Float,
	@Json(name = "usd_reserved") val usdReserved: Float,
	@Json(name = "xrp_available") val xrpAvailable: Float,
	@Json(name = "xrp_balance") val xrpBalance: Float,
	@Json(name = "xrp_reserved") val xrpReserved: Float,
	@Json(name = "xrpbtc_fee") val xrpBtcFee: Float,
	@Json(name = "xrpeur_fee") val xrpEurFee: Float,
	@Json(name = "xrpusd_fee") val xrpUsdFee: Float
) {
	val available: Map<String, Float>
		get() = mapOf(
			"BCH" to this.bchAvailable,
			"BTC" to this.btcAvailable,
			"ETH" to this.ethAvailable,
			"EUR" to this.eurAvailable,
			"LTC" to this.ltcAvailable,
			"USD" to this.usdAvailable,
			"XRP" to this.xrpAvailable
		)

	val balance: Map<String, Float>
		get() = mapOf(
			"BCH" to this.bchBalance,
			"BTC" to this.btcBalance,
			"ETH" to this.ethBalance,
			"EUR" to this.eurBalance,
			"LTC" to this.ltcBalance,
			"USD" to this.usdBalance,
			"XRP" to this.xrpBalance
		)

	val fees: Map<String, Float>
		get() = mapOf(
			"BCHBTC" to this.bchBtcFee,
			"BCHEUR" to this.bchEurFee,
			"BCHUSD" to this.bchUsdFee,
			"BTCEUR" to this.btcEurFee,
			"BTCUSD" to this.btcUsdFee,
			"ETHBTC" to this.ethBtcFee,
			"ETHEUR" to this.ethEurFee,
			"ETHUSD" to this.ethUsdFee,
			"EURUSD" to this.eurUsdFee,
			"LTCBTC" to this.ltcBtcFee,
			"LTCEUR" to this.ltcEurFee,
			"LTCUSD" to this.ltcUsdFee,
			"XRPBTC" to this.xrpBtcFee,
			"XRPEUR" to this.xrpEurFee,
			"XRPUSD" to this.xrpUsdFee
		)

	val reserved: Map<String, Float>
		get() = mapOf(
			"BCH" to this.bchReserved,
			"BTC" to this.btcReserved,
			"ETH" to this.ethReserved,
			"EUR" to this.eurReserved,
			"LTC" to this.ltcReserved,
			"USD" to this.usdReserved,
			"XRP" to this.xrpReserved
		)
}
