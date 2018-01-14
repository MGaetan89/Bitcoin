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
import kotlinx.android.synthetic.main.adapter_price_order_book.amount_ask
import kotlinx.android.synthetic.main.adapter_price_order_book.amount_bid
import kotlinx.android.synthetic.main.adapter_price_order_book.ask
import kotlinx.android.synthetic.main.adapter_price_order_book.bid
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class PriceOrderBookAdapter(private val tradingPair: TradingPair) : RecyclerView.Adapter<PriceOrderBookAdapter.ViewHolder>() {
	private var orderBook = OrderBook.EMPTY

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

	suspend fun updateOrderBook(orderBook: OrderBook) {
		val diffResult = DiffUtil.calculateDiff(OrderBookDiffCallback(this.orderBook, orderBook), true)

		this.orderBook = orderBook

		launch(UI) {
			diffResult.dispatchUpdatesTo(this@PriceOrderBookAdapter)
		}
	}

	class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

	private class OrderBookDiffCallback(private val oldOrderBook: OrderBook, private val newOrderBook: OrderBook) : DiffUtil.Callback() {
		override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newOrderBook.asks.contentEquals(this.oldOrderBook.asks) && this.newOrderBook.bids.contentEquals(this.oldOrderBook.bids)
		}

		override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newOrderBook.timestamp == this.oldOrderBook.timestamp
		}

		override fun getNewListSize() = this.newOrderBook.asks.size

		override fun getOldListSize() = this.oldOrderBook.asks.size
	}
}
