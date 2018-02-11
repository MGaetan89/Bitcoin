package io.crypto.bitstamp.network

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.crypto.bitstamp.model.TradingPair
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BitstampRepository {
	fun getTradingPairs(): LiveData<List<TradingPair>> {
		val tradingPairs = MutableLiveData<List<TradingPair>>()

		BitstampServices.api.getTradingPairs().enqueue(object : Callback<List<TradingPair>> {
			override fun onFailure(call: Call<List<TradingPair>>, t: Throwable) = Unit

			override fun onResponse(
				call: Call<List<TradingPair>>,
				response: Response<List<TradingPair>>
			) {
				if (response.isSuccessful) {
					val sortedTradingPairs = response.body().orEmpty().sortedBy { it.name }

					tradingPairs.value = sortedTradingPairs
				}
			}
		})

		return tradingPairs
	}
}
