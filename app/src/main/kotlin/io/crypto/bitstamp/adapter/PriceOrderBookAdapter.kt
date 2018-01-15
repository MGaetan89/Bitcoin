package io.crypto.bitstamp.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.inflate
import io.crypto.bitstamp.extension.toFormattedString
import io.crypto.bitstamp.model.PriceOrderBook
import io.crypto.bitstamp.model.TradingPair
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_price_order_book.amount_ask
import kotlinx.android.synthetic.main.adapter_price_order_book.amount_bid
import kotlinx.android.synthetic.main.adapter_price_order_book.ask
import kotlinx.android.synthetic.main.adapter_price_order_book.bid
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class PriceOrderBookAdapter(private val tradingPair: TradingPair) : RecyclerView.Adapter<PriceOrderBookAdapter.ViewHolder>() {
	private var orderBook = PriceOrderBook.EMPTY

	override fun getItemCount() = this.orderBook.asks.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val ask = this.orderBook.asks.getOrNull(position) ?: return
		val bid = this.orderBook.bids.getOrNull(position) ?: return

		holder.amount_ask.text = ask[1].toFormattedString(this.tradingPair.baseDecimals)
		holder.amount_bid.text = bid[1].toFormattedString(this.tradingPair.baseDecimals)
		holder.ask.text = ask[0].toFormattedString(this.tradingPair.counterDecimals)
		holder.bid.text = bid[0].toFormattedString(this.tradingPair.counterDecimals)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = parent.inflate(R.layout.adapter_price_order_book)

		return ViewHolder(view)
	}

	suspend fun updateOrderBook(orderBook: PriceOrderBook) {
		val diffResult = DiffUtil.calculateDiff(OrderBookDiffCallback(this.orderBook, orderBook), true)

		this.orderBook = orderBook

		launch(UI) {
			diffResult.dispatchUpdatesTo(this@PriceOrderBookAdapter)
		}
	}

	class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

	private class OrderBookDiffCallback(private val oldPriceOrderBook: PriceOrderBook, private val newPriceOrderBook: PriceOrderBook) : DiffUtil.Callback() {
		override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newPriceOrderBook.asks.contentEquals(this.oldPriceOrderBook.asks) && this.newPriceOrderBook.bids.contentEquals(this.oldPriceOrderBook.bids)
		}

		override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newPriceOrderBook.timestamp == this.oldPriceOrderBook.timestamp
		}

		override fun getNewListSize() = this.newPriceOrderBook.asks.size

		override fun getOldListSize() = this.oldPriceOrderBook.asks.size
	}
}
