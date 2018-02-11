package io.crypto.bitstamp.liveData

import android.arch.lifecycle.LiveData
import com.pusher.client.Pusher
import io.crypto.bitstamp.BuildConfig
import io.crypto.bitstamp.extension.parseJson
import io.crypto.bitstamp.model.OrderBook

class OrderBookLiveData(private val urlSymbol: String) : LiveData<Pair<String, OrderBook>>() {
	private val pusher = Pusher(BuildConfig.PUSHER_API_KEY)
	private val channelName: String
		get() = if (this.urlSymbol == "btcusd") "order_book" else "order_book_${this.urlSymbol}"

	override fun onActive() {
		this.pusher.connect()

		this.pusher.subscribe(this.channelName)
			.bind("data") { _, _, data ->
				this.postValue(this.urlSymbol to data.parseJson())
			}
	}

	override fun onInactive() {
		this.pusher.unsubscribe(this.channelName)

		this.pusher.disconnect()
	}
}
