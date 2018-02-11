package io.crypto.bitstamp.viewModel

import android.arch.lifecycle.ViewModel
import io.crypto.bitstamp.liveData.OrderBookLiveData

class OrderBookViewModel : ViewModel() {
	private var orderBook: OrderBookLiveData? = null
	private var urlSymbol = ""

	fun getOrderBook(): OrderBookLiveData {
		if (this.orderBook == null) {
			this.orderBook = OrderBookLiveData(this.urlSymbol)
		}

		return this.orderBook!!
	}

	fun setUrlSymbol(urlSymbol: String) = this.apply {
		this.urlSymbol = urlSymbol
	}
}
