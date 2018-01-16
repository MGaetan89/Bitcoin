package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.setTextColorResource
import io.crypto.bitstamp.extension.toFormattedDateTime
import io.crypto.bitstamp.extension.toFormattedPercent
import io.crypto.bitstamp.extension.toFormattedString
import io.crypto.bitstamp.extension.toVariationColor
import io.crypto.bitstamp.model.Ticker
import io.crypto.bitstamp.model.TradingPair
import io.crypto.bitstamp.network.BitstampServices
import kotlinx.android.synthetic.main.fragment_price_overview.ask
import kotlinx.android.synthetic.main.fragment_price_overview.bid
import kotlinx.android.synthetic.main.fragment_price_overview.change
import kotlinx.android.synthetic.main.fragment_price_overview.change_percent
import kotlinx.android.synthetic.main.fragment_price_overview.date
import kotlinx.android.synthetic.main.fragment_price_overview.high_24
import kotlinx.android.synthetic.main.fragment_price_overview.last
import kotlinx.android.synthetic.main.fragment_price_overview.low_24
import kotlinx.android.synthetic.main.fragment_price_overview.volume
import kotlinx.android.synthetic.main.fragment_price_overview.vwap
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class PriceOverviewFragment : BaseFragment() {
	companion object {
		fun newInstance(tradingPair: TradingPair) = PriceOverviewFragment().apply {
			this.arguments = Bundle().apply {
				this.putParcelable(TradingPair.EXTRA, tradingPair)
			}
		}
	}

	private val tradingPair by lazy { this.arguments!!.getParcelable<TradingPair>(TradingPair.EXTRA) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_price_overview, container, false)
	}

	override fun onResume() {
		super.onResume()

		this.runPeriodically {
			listOf(launch {
				val ticker = BitstampServices.getTicker(tradingPair.urlSymbol)

				launch(UI) {
					updateViews(ticker)
				}
			})
		}
	}

	private fun updateViews(ticker: Ticker) {
		val change = ticker.last - ticker.open
		val changePercent = change / ticker.last
		val changeColor = change.toVariationColor()

		this.ask.text = ticker.ask.toFormattedString(this.tradingPair.counterDecimals)
		this.bid.text = ticker.bid.toFormattedString(this.tradingPair.counterDecimals)
		this.change.text = change.toFormattedString(this.tradingPair.counterDecimals)
		this.change.setTextColorResource(changeColor)
		this.change_percent.text = changePercent.toFormattedPercent()
		this.change_percent.setTextColorResource(changeColor)
		this.date.text = ticker.timestamp.toFormattedDateTime()
		this.high_24.text = ticker.high.toFormattedString(this.tradingPair.counterDecimals)
		this.last.text = ticker.last.toFormattedString(this.tradingPair.counterDecimals)
		this.low_24.text = ticker.low.toFormattedString(this.tradingPair.counterDecimals)
		this.volume.text = ticker.volume.toFormattedString(this.tradingPair.baseDecimals)
		this.vwap.text = ticker.volumeWeightedAveragePrice.toFormattedString(this.tradingPair.baseDecimals)
	}
}
