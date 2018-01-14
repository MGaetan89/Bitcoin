package io.crypto.bitstamp.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.inflate
import io.crypto.bitstamp.model.Ticker
import io.crypto.bitstamp.model.TradingPair
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_price.ask
import kotlinx.android.synthetic.main.adapter_price.bid
import kotlinx.android.synthetic.main.adapter_price.name
import kotlinx.android.synthetic.main.adapter_price.time
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Date

class PricesAdapter(private val listener: OnPriceEventListener) : RecyclerView.Adapter<PricesAdapter.ViewHolder>() {
	interface OnPriceEventListener {
		fun onAskClicked(tradingPair: TradingPair)

		fun onBidClicked(tradingPair: TradingPair)

		fun onPriceClicked(tradingPair: TradingPair)
	}

	private val dateFormat = DateFormat.getTimeInstance()
	private val prices = mutableListOf<Pair<TradingPair, Ticker>>()

	override fun getItemCount() = this.prices.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val price = this.prices.getOrNull(position) ?: return
		val tradingPair = price.first
		val ticker = price.second
		val numberFormat = NumberFormat.getNumberInstance().apply {
			this.maximumFractionDigits = tradingPair.counterDecimals
			this.minimumFractionDigits = tradingPair.counterDecimals
		}

		holder.ask.text = numberFormat.format(ticker.ask)
		holder.bid.text = numberFormat.format(ticker.bid)
		holder.time.text = this.dateFormat.format(Date(ticker.timestamp * 1000))
		holder.name.text = tradingPair.name
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = parent.inflate(R.layout.adapter_price)

		return ViewHolder(view)
	}

	suspend fun updateTicker(urlSymbol: String, ticker: Ticker) {
		val index = this.prices.indexOfFirst { it.first.urlSymbol == urlSymbol }.takeIf { it >= 0 }
				?: return
		val (tradingPair, oldTicker) = this.prices[index]

		if (ticker.isNewerThan(oldTicker)) {
			this.prices[index] = Pair(tradingPair, ticker)

			launch(UI) {
				notifyItemChanged(index)
			}
		}
	}

	suspend fun updateTradingPairs(tradingPairs: List<TradingPair>) {
		val diffResult = DiffUtil.calculateDiff(PricesDiffCallback(this.prices.map { it.first }, tradingPairs), false)

		this.prices.clear()
		this.prices.addAll(tradingPairs.map { it to Ticker.EMPTY })

		launch(UI) {
			diffResult.dispatchUpdatesTo(this@PricesAdapter)
		}
	}

	inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer, View.OnClickListener {
		init {
			this.ask.setOnClickListener(this)
			this.bid.setOnClickListener(this)
			this.containerView.setOnClickListener(this)
		}

		override fun onClick(view: View) {
			val tradingPair = prices[this.adapterPosition].first

			when (view.id) {
				R.id.ask -> listener.onAskClicked(tradingPair)
				R.id.bid -> listener.onBidClicked(tradingPair)
				else -> listener.onPriceClicked(tradingPair)
			}
		}
	}

	private class PricesDiffCallback(private val oldItems: List<TradingPair>, private val newItems: List<TradingPair>) : DiffUtil.Callback() {
		override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newItems[newItemPosition].name == this.oldItems[oldItemPosition].name
		}

		override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newItems[newItemPosition].name == this.oldItems[oldItemPosition].name
		}

		override fun getNewListSize() = this.newItems.size

		override fun getOldListSize() = this.oldItems.size
	}
}
