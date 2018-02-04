package io.crypto.bitstamp.activity

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewStub
import android.widget.Toast
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.PricesAdapter
import io.crypto.bitstamp.extension.startActivity
import io.crypto.bitstamp.model.Ticker
import io.crypto.bitstamp.model.TradingPair
import io.crypto.bitstamp.network.BitstampServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PricesActivity : BaseActivity(), PricesAdapter.OnPriceEventListener {
	private val adapter = PricesAdapter(this)
	private val urlSymbols = mutableListOf<String>()

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

	override fun onResume() {
		super.onResume()

		this.requestTradingPairs()
	}

	private fun requestTicker() {
		this.urlSymbols.forEach {
			BitstampServices.api.getTicker(it).enqueue(object : Callback<Ticker> {
				override fun onFailure(call: Call<Ticker>, t: Throwable) {
					val context = this@PricesActivity
					Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
				}

				override fun onResponse(call: Call<Ticker>, response: Response<Ticker>) {
					if (response.isSuccessful) {
						val ticker = response.body() ?: return

						adapter.updateTicker(it, ticker)
					} else {
						response.errorBody()?.string()?.let { message ->
							val context = this@PricesActivity
							Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
						}
					}
				}
			})
		}
	}

	private fun requestTradingPairs() {
		BitstampServices.api.getTradingPairs().enqueue(object : Callback<List<TradingPair>> {
			override fun onFailure(call: Call<List<TradingPair>>, t: Throwable) = Unit

			override fun onResponse(
				call: Call<List<TradingPair>>,
				response: Response<List<TradingPair>>
			) {
				if (response.isSuccessful) {
					val tradingPairs = response.body()?.sortedBy { it.name } ?: return

					urlSymbols.clear()
					urlSymbols.addAll(tradingPairs.map { it.urlSymbol })

					adapter.updateTradingPairs(tradingPairs)

					requestTicker()
				}
			}
		})
	}
}
