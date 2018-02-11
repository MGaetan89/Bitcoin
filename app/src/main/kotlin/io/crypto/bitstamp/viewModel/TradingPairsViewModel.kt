package io.crypto.bitstamp.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.crypto.bitstamp.model.TradingPair
import io.crypto.bitstamp.network.BitstampRepository

class TradingPairsViewModel : ViewModel() {
	private lateinit var repository: BitstampRepository
	private var tradingPairs: LiveData<List<TradingPair>>? = null

	fun getTradingPairs(): LiveData<List<TradingPair>> {
		if (this.tradingPairs == null) {
			this.tradingPairs = this.repository.getTradingPairs()
		}

		return this.tradingPairs!!
	}

	fun setRepository(repository: BitstampRepository) = this.apply {
		this.repository = repository
	}
}
