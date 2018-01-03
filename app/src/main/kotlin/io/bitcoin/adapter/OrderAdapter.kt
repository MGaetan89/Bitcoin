package io.bitcoin.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.bitcoin.R
import io.bitcoin.model.Order
import io.bitcoin.model.TradingPair
import java.text.NumberFormat

class OrderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	private val lastPrices = mutableMapOf<TradingPair, Double>()
	private val orders = mutableListOf<Order>()

	override fun getItemCount() = if (this.orders.isEmpty()) 0 else this.orders.size + 1

	override fun getItemViewType(position: Int) = if (position == this.orders.size) VIEW_TYPE_TOTAL else VIEW_TYPE_ORDER

	fun getUrlSymbols() = this.orders.map { it.tradingPair.toUrlSymbol() }

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (holder) {
			is OrderViewHolder -> {
				val order = this.orders[position]

				holder.bindTo(order, this.lastPrices[order.tradingPair])
			}
			is TotalViewHolder -> holder.bindTotal(this.orders, this.lastPrices)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return when (viewType) {
			VIEW_TYPE_ORDER -> OrderViewHolder.of(parent)
			VIEW_TYPE_TOTAL -> TotalViewHolder.of(parent)
			else -> throw IllegalArgumentException("Unknown viewType '$viewType'")
		}
	}

	fun removeOrder(order: Order) {
		val index = this.orders.indexOf(order)

		if (index >= 0) {
			this.orders.removeAt(index)

			this.notifyItemRemoved(index)
			this.notifyItemChanged(this.orders.size)
		}
	}

	fun updateOrders(orders: List<Order>) {
		this.orders.clear()
		this.orders.addAll(orders)

		this.notifyDataSetChanged()
	}

	fun updatePrice(tradingPair: TradingPair, price: Double) {
		this.lastPrices[tradingPair] = price

		this.orders.forEachIndexed { index, order ->
			if (order.tradingPair == tradingPair) {
				this.notifyItemChanged(index)
			}
		}

		this.notifyItemChanged(this.orders.size)
	}

	private companion object {
		private const val VIEW_TYPE_ORDER = 0
		private const val VIEW_TYPE_TOTAL = 1
	}

	class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val color: View = view.findViewById(R.id.color)
		private val gain: TextView = view.findViewById(R.id.gain)
		private val gainPercent: TextView = view.findViewById(R.id.gain_percent)
		private val id: TextView = view.findViewById(R.id.id)
		private val quantity: TextView = view.findViewById(R.id.quantity)
		private val tradingPair: TextView = view.findViewById(R.id.trading_pair)
		private val unitPrice: TextView = view.findViewById(R.id.unit_price)

		fun bindTo(order: Order, lastPrice: Double?) {
			val priceNumberFormat = order.tradingPair.getCounterNumberFormat()
			val quantityNumberFormat = order.tradingPair.getNumberFormat()

			if (lastPrice != null) {
				val gain = order.getGain(lastPrice)
				val gainPercent = order.getGainPercent(lastPrice)
				val colorResource = if (gain < 0.0) R.color.ask else R.color.bid
				val color = ContextCompat.getColor(this.gain.context, colorResource)

				this.color.setBackgroundResource(colorResource)
				this.gain.text = priceNumberFormat.format(gain)
				this.gain.setTextColor(color)
				this.gainPercent.text = percentFormat.format(gainPercent)
				this.gainPercent.setTextColor(color)
			} else {
				val color = ContextCompat.getColor(this.gain.context, R.color.text)

				this.color.setBackgroundResource(R.color.background)
				this.gain.text = null
				this.gain.setTextColor(color)
				this.gainPercent.text = null
				this.gainPercent.setTextColor(color)
			}

			this.id.text = this.id.resources.getString(R.string.order_id, order.id)
			this.quantity.text = quantityNumberFormat.format(order.quantity)
			this.tradingPair.text = order.tradingPair.toString()
			this.unitPrice.text = priceNumberFormat.format(order.unitPrice)
		}

		companion object {
			private val percentFormat: NumberFormat = NumberFormat.getPercentInstance().apply {
				this.maximumFractionDigits = 2
				this.minimumFractionDigits = 2
			}

			fun of(parent: ViewGroup): OrderViewHolder {
				val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_order, parent, false)

				return OrderViewHolder(view)
			}
		}
	}

	class TotalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val color: View = view.findViewById(R.id.color)
		private val currency: TextView = view.findViewById(R.id.currency)
		private val gain: TextView = view.findViewById(R.id.gain)
		private val label: TextView = view.findViewById(R.id.label)

		fun bindTotal(orders: List<Order>, lastPrices: Map<TradingPair, Double>) {
			val priceNumberFormat = NumberFormat.getNumberInstance().also {
				it.maximumFractionDigits = 2
				it.minimumFractionDigits = 2
			}
			val gain = orders.sumByDouble { lastPrices[it.tradingPair]?.let { lastPrice -> it.getGain(lastPrice) } ?: 0.0 }
			val colorResource = if (gain < 0.0) R.color.ask else R.color.bid
			val color = ContextCompat.getColor(this.gain.context, colorResource)

			this.color.setBackgroundResource(colorResource)
			this.currency.text = orders.firstOrNull()?.tradingPair?.name?.split("/")?.last()
			this.gain.text = priceNumberFormat.format(gain)
			this.gain.setTextColor(color)
			this.label.text = this.label.context.getString(R.string.total)
		}

		companion object {
			fun of(parent: ViewGroup): TotalViewHolder {
				val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_order_total, parent, false)

				return TotalViewHolder(view)
			}
		}
	}
}
