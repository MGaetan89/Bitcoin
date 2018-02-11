package io.crypto.bitstamp.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewStub
import android.widget.Toast
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.PricesAdapter
import io.crypto.bitstamp.extension.startActivity
import io.crypto.bitstamp.model.TradingPair
import io.crypto.bitstamp.network.BitstampRepository
import io.crypto.bitstamp.viewModel.OrderBookViewModel
import io.crypto.bitstamp.viewModel.TradingPairsViewModel

class PricesActivity : BaseActivity(), PricesAdapter.OnPriceEventListener {
	private val adapter = PricesAdapter(this)

	override fun getContentResourceId() = R.layout.activity_prices

	override fun getSelectedTabId() = R.id.menu_prices

	override fun getToolbarTitle(): CharSequence = this.getString(R.string.prices)

	override fun onAskClicked(tradingPair: TradingPair) {
		Toast.makeText(this, "Ask ${tradingPair.name}", Toast.LENGTH_SHORT).show()
	}

	override fun onBidClicked(tradingPair: TradingPair) {
		Toast.makeText(this, "Bid ${tradingPair.name}", Toast.LENGTH_SHORT).show()
	}

	override fun onInflate(stub: ViewStub, inflated: View) {
		super.onInflate(stub, inflated)

		ViewModelProviders.of(this)
			.get(TradingPairsViewModel::class.java)
			.setRepository(BitstampRepository())
			.getTradingPairs()
			.observe(this, Observer {
				val tradingPairs = it.orEmpty()
				val urlSymbols = tradingPairs.map { it.urlSymbol }

				adapter.updateTradingPairs(tradingPairs)

				urlSymbols.forEach {
					ViewModelProviders.of(this)
						.get("OrderBookViewModel_$it", OrderBookViewModel::class.java)
						.setUrlSymbol(it)
						.getOrderBook()
						.observe(this, Observer {
							it?.let {
								adapter.updateTicker(it.first, it.second)
							}
						})
				}
			})

		(inflated as RecyclerView).let {
			it.adapter = this.adapter
			it.layoutManager = LinearLayoutManager(this)
			it.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
		}
	}

	override fun onPriceClicked(tradingPair: TradingPair) {
		this.startActivity<PriceActivity> {
			this.putExtra(TradingPair.EXTRA, tradingPair)
		}
	}
}
