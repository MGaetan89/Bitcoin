package io.bitcoin.network

import io.bitcoin.model.TradingPair
import retrofit2.Call
import retrofit2.http.GET

interface BitstampServices {
	@GET("v2/trading-pairs-info")
	fun getTradingPairs(): Call<List<TradingPair>>
}
