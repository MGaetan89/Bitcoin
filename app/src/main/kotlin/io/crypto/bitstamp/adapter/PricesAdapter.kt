package io.crypto.bitstamp.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.inflate
import io.crypto.bitstamp.extension.toFormattedString
import io.crypto.bitstamp.model.OrderBook
import io.crypto.bitstamp.model.TradingPair
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_price.ask
import kotlinx.android.synthetic.main.adapter_price.bid
import kotlinx.android.synthetic.main.adapter_price.name

class PricesAdapter(private val listener: OnPriceEventListener) :
	RecyclerView.Adapter<PricesAdapter.ViewHolder>() {
	interface OnPriceEventListener {
		fun onAskClicked(tradingPair: TradingPair)

		fun onBidClicked(tradingPair: TradingPair)

		fun onPriceClicked(tradingPair: TradingPair)
	}

	private val prices = mutableListOf<Triple<TradingPair, Float, Float>>()

	override fun getItemCount() = this.prices.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val price = this.prices.getOrNull(position) ?: return

		holder.ask.text = price.second.toFormattedString(price.first.counterDecimals)
		holder.bid.text = price.third.toFormattedString(price.first.counterDecimals)
		holder.name.text = price.first.name
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = parent.inflate(R.layout.adapter_price)

		return ViewHolder(view)
	}

	fun updateTicker(urlSymbol: String, orderBook: OrderBook) {
		val index = this.prices.indexOfFirst { it.first.urlSymbol == urlSymbol }
			.takeIf { it >= 0 } ?: return
		val ask = orderBook.asks[0][0]
		val bid = orderBook.bids[0][0]
		val tradingPair = this.prices[index].first

		this.prices[index] = Triple(tradingPair, ask, bid)

		this.notifyItemChanged(index)
	}

	fun updateTradingPairs(tradingPairs: List<TradingPair>) {
		val diffResult = DiffUtil.calculateDiff(
			PricesDiffCallback(this.prices.map { it.first }, tradingPairs),
			false
		)

		this.prices.clear()
		this.prices.addAll(tradingPairs.map { Triple(it, 0f, 0f) })

		diffResult.dispatchUpdatesTo(this)
	}

	inner class ViewHolder(override val containerView: View) :
		RecyclerView.ViewHolder(containerView), LayoutContainer, View.OnClickListener {
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

	private class PricesDiffCallback(
		private val oldItems: List<TradingPair>,
		private val newItems: List<TradingPair>
	) : DiffUtil.Callback() {
		override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true

		override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newItems.getOrNull(newItemPosition)?.urlSymbol == this.oldItems.getOrNull(
				oldItemPosition
			)?.urlSymbol
		}

		override fun getNewListSize() = this.newItems.size

		override fun getOldListSize() = this.oldItems.size
	}
}
