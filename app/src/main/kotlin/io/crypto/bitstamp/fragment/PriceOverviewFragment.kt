package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.setTextColorResource
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
import java.text.DateFormat
import java.text.NumberFormat

class PriceOverviewFragment : BaseFragment() {
	companion object {
		fun newInstance(tradingPair: TradingPair) = PriceOverviewFragment().apply {
			this.arguments = Bundle().apply {
				this.putParcelable(TradingPair.EXTRA, tradingPair)
			}
		}
	}

	private val dateFormat = DateFormat.getDateTimeInstance()
	private val percentFormat by lazy {
		NumberFormat.getPercentInstance().also {
			it.maximumFractionDigits = 2
			it.minimumFractionDigits = 2
		}
	}
	private val priceFormat by lazy {
		NumberFormat.getNumberInstance().also {
			it.maximumFractionDigits = this.tradingPair.counterDecimals
			it.minimumFractionDigits = this.tradingPair.counterDecimals
		}
	}
	private val tradingPair by lazy { this.arguments!!.getParcelable<TradingPair>(TradingPair.EXTRA) }
	private val volumeFormat by lazy {
		NumberFormat.getNumberInstance().also {
			it.maximumFractionDigits = this.tradingPair.baseDecimals
			it.minimumFractionDigits = this.tradingPair.baseDecimals
		}
	}

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
		val changeColor = when {
			change < 0 -> R.color.ask
			change > 0 -> R.color.bid
			else -> R.color.text
		}

		this.ask.text = this.priceFormat.format(ticker.ask)
		this.bid.text = this.priceFormat.format(ticker.bid)
		this.change.text = this.priceFormat.format(change)
		this.change.setTextColorResource(changeColor)
		this.change_percent.text = this.percentFormat.format(changePercent)
		this.change_percent.setTextColorResource(changeColor)
		this.date.text = this.dateFormat.format(ticker.timestamp * 1000)
		this.high_24.text = this.priceFormat.format(ticker.high)
		this.last.text = this.priceFormat.format(ticker.last)
		this.low_24.text = this.priceFormat.format(ticker.low)
		this.volume.text = this.volumeFormat.format(ticker.volume)
		this.vwap.text = this.volumeFormat.format(ticker.volumeWeightedAveragePrice)
	}
}
